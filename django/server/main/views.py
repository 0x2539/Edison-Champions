from django.shortcuts import render
from django.http import JsonResponse, Http404
from django.views.generic import View
from django.views.decorators.csrf import csrf_exempt
from django.conf import settings
from django.utils import timezone
from django.db import transaction
from .models import Ding, User, FacebookObject
import time
from .util import json_endpoint

def index(request):
    return JsonResponse({})

class DingView(View):
    @csrf_exempt
    def post(self, request):
        Ding.objects.create()
        return JsonResponse({})

    def get(self, request):
        dings = list(Ding.objects.filter(sent=False))
        Ding.objects.all().update(sent=True)

        return JsonResponse([
            ding.created_at
            for ding in dings
        ], safe=False)


class UserView(View):
    @json_endpoint
    @csrf_exempt
    @transaction.atomic
    def post(self, request, params):
        mac = params['mac']
        nearby = params.get('nearby', [])
        friends = params.get('friends', [])
        likes = params.get('likes', [])
        name = params.get('name', '')
        picture = params.get('picture', '')
        facebook_id = params.get('facebook_id')

        user, _ = User.objects.get_or_create(mac=mac)

        user.nearby.clear()
        for mac in nearby:
            nearby_user, _ = User.objects.get_or_create(mac=mac)
            user.nearby.add(nearby_user)

        user.friends.clear()
        for facebook_id in friends:
            friend, _ = User.objects.get_or_create(facebook_id=facebook_id)
            user.friends.add(friend)

        user.likes.clear()
        for facebook_object in likes:
            object, _ = FacebookObject.objects.get_or_create(object_id=facebook_object)
            user.likes.add(object)

        user.facebook_id = facebook_id
        user.name = name
        user.picture = picture
        user.save()

        return JsonResponse({})

    def get(self, request):
        try:
            user = User.objects.get(mac=request.GET['mac'])
        except:
            raise Http404
        return JsonResponse({
            "mac": user.mac,
            "facebook_id": user.facebook_id,
            "name": user.name,
            "picture": user.picture,
            "friends": [ friend.mac for friend in user.friends.all() ],
            "nearby": [ user.mac for user in user.nearby.all() ],
            "likes": [ object.object_id for object in user.likes.all() ],
        })


class UsersNearEdisonView(View):
    @json_endpoint
    @transaction.atomic
    def post(self, request, params):
        users = params.get('mac', [])
        User.objects.update(near_edison=False)
        for mac in users:
            user, _ = User.objects.get_or_create(mac=mac)
            user.near_edison = True
            user.save()

        return JsonResponse({})


class LevelView(View):
    def get(self, request):
        ding_times = map(
            lambda created_at: int(time.mktime(created_at.timetuple()) / 60),
            Ding.objects.all().order_by('created_at').values_list('created_at', flat=True)
        )

        level = 0
        prev_ding_time = None

        for ding_time in ding_times:
            if prev_ding_time:
                level = max(0, level - settings.TIME_DECAY * (ding_time - prev_ding_time))
            level = min(100, level + settings.DING_BOOST)
            prev_ding_time = ding_time

        now_timestamp_minutes = int(time.mktime(timezone.now().timetuple()) / 60)
        if level > 0:
            level = max(0, level - (now_timestamp_minutes - ding_times[-1]) * settings.TIME_DECAY)

        return JsonResponse({
            "level": level,
        })
