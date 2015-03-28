from django.db import models

class Ding(models.Model):
    created_at = models.DateTimeField(auto_now_add=True)
    sent = models.BooleanField(default=False)


class User(models.Model):
    mac = models.CharField(max_length=32, unique=True, null=True, blank=True)
    facebook_id = models.CharField(max_length=64, unique=True, null=True, blank=True)
    likes = models.ManyToManyField('FacebookObject')
    friends = models.ManyToManyField('self')
    picture = models.URLField()
    name = models.CharField(max_length=128)
    nearby = models.ManyToManyField('self', symmetrical=True)
    near_edison = models.BooleanField(default=False)


class FacebookObject(models.Model):
    object_id = models.CharField(max_length=64, primary_key=True)
