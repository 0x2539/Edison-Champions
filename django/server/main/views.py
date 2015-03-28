from django.shortcuts import render
from django.http import JsonResponse
from django.views.generic import View
from django.views.decorators.csrf import csrf_exempt
from django.conf import settings
from django.utils import timezone
from .models import Ding

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
    pass

class LevelView(View):
    def get(self, request):
        num_dings = Ding.objects.all().count()
        oldest_ding = Ding.objects.order_by('created_at')[0]
        num_minutes = (timezone.now() - oldest_ding.created_at).total_seconds() / 60

        level = settings.DING_BOOST * num_dings - settings.TIME_DECAY * num_minutes

        return JsonResponse({
            "level": level,
        })
