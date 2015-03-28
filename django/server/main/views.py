from django.shortcuts import render, get_object_or_404
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
    @json_endpoint
    @csrf_exempt
    def post(self, request, params):
        if params.get('mac') is None or not User.objects.filter(mac=params['mac'], near_edison=True).exists():
            raise Http404

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

        if len(nearby) > 0:
            user.nearby.clear()
            for mac in nearby:
                nearby_user, _ = User.objects.get_or_create(mac=mac)
                user.nearby.add(nearby_user)

        if len(friends) > 0:
            user.friends.clear()
            for facebook_id in friends:
                friend, _ = User.objects.get_or_create(facebook_id=facebook_id)
                user.friends.add(friend)

        if len(likes) > 0:
            user.likes.clear()
            for facebook_object in likes:
                object, _ = FacebookObject.objects.get_or_create(object_id=facebook_object)
                user.likes.add(object)

        if facebook_id:
            user.facebook_id = facebook_id

        if name:
            user.name = name
        user.picture = picture
        user.save()

        return JsonResponse({})

    def get(self, request):
        user = get_object_or_404(User, mac=request.GET.get('mac'))
        return JsonResponse({
            "mac": user.mac,
            "facebook_id": user.facebook_id,
            "name": user.name,
            "picture": user.picture,
            "friends": [ friend.mac for friend in user.friends.all() ],
            "nearby": [ user.mac for user in user.nearby.all() ],
            "likes": [ object.object_id for object in user.likes.all() ],
        })


class UsersNearby(View):
    def get(self, request):
        if 'mac' not in request.GET:
            raise Http404

        users = list(User.objects.filter(near_edison=True))
        current_user = User.objects.get(mac=request.GET['mac'])
        nearby = []

        for user in users:
            if user.id == current_user.id:
                continue
            user_friends = set(user.friends.all().exclude(facebook_id=None).values_list('facebook_id', flat=True))
            current_user_friends = set(current_user.friends.all().exclude(facebook_id=None).values_list('facebook_id', flat=True))

            try:
                num_mutual_friends = user.mutual_friends(user.facebook_id)
            except:
                num_mutual_friends = 0

            print "Mutual friends:", num_mutual_friends

            friends = False
            if current_user.facebook_id in user_friends:
                friends = True

            user_likes = set(user.likes.all().values_list('object_id', flat=True))
            current_user_likes = set(current_user.likes.all().values_list('object_id', flat=True))
            likes_in_common = current_user_likes.intersection(user_likes)

            score = num_mutual_friends * settings.RELATED_FRIEND_RATING \
                + len(likes_in_common) * settings.LIKE_RATING \
                + int(friends) * settings.FRIEND_RATING

            nearby.append({
                "id": user.facebook_id,
                "name": user.name,
                "pictureUrl": user.picture,
                "score": score,
                "mutualFriends": num_mutual_friends,
                "mutualLikes": len(likes_in_common),
            })

        nearby.sort(key=lambda user: user['score'], reverse=True)
        return JsonResponse(nearby, safe=False)


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
