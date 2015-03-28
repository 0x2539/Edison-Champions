from django.shortcuts import render
from django.http import JsonResponse
from django.views.generic import View
from django.views.decorators.csrf import csrf_exempt
from django.conf import settings
from django.utils import timezone
from django.db import transaction
from .models import Ding, User, FacebookObject
from .util import json_endpoint

def index(request):
    return JsonResponse({
    })

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

        user, _ = User.objects.get_or_create(mac=mac)

        user.nearby.clear()
        for mac in nearby:
            nearby_user, _ = User.objects.get_or_create(mac=mac)
            user.nearby.add(nearby_user)

        user.friends.clear()
        for mac in friends:
            friend, _ = User.objects.get_or_create(mac=mac)
            user.friends.add(friend)

        user.likes.clear()
        for facebook_object in likes:
            object, _ = FacebookObject.objects.get_or_create(object_id=facebook_object)
            user.likes.add(object)

        user.name = name
        user.picture = picture
        user.save()

        return JsonResponse({})

    def get(self, request):
        user = User.objects.get(mac=request.GET['mac'])
        return JsonResponse({
            "mac": user.mac,
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
        num_dings = Ding.objects.all().count()
        oldest_ding = Ding.objects.order_by('created_at')[0]
        num_minutes = (timezone.now() - oldest_ding.created_at).total_seconds() / 60

        level = settings.DING_BOOST * num_dings - settings.TIME_DECAY * num_minutes

        return JsonResponse({
            "level": min(100, max(0, level)),
        })
