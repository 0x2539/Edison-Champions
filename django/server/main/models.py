from django.db import models
import facebook
import requests
import json

class Ding(models.Model):
    created_at = models.DateTimeField(auto_now_add=True)
    sent = models.BooleanField(default=False)


class User(models.Model):
    mac = models.CharField(max_length=32, unique=True, null=True, blank=True)
    facebook_id = models.CharField(max_length=64, unique=True, null=True, blank=True)
    likes = models.ManyToManyField('FacebookObject')
    picture = models.URLField()
    name = models.CharField(max_length=128)
    nearby = models.ManyToManyField('self', symmetrical=True)
    near_edison = models.BooleanField(default=False)
    access_token = models.CharField(max_length=512)

    def mutual_friends(self, other_id):
        graph = facebook.GraphAPI(access_token=self.access_token)
        friends = graph.get_connections(
            str(other_id), '',
            fields="context.fields(mutual_friends)"
        )['context']['mutual_friends']

        # TODO: return True if the users are friends
        return friends['summary']['total_count'], False

    def populate_fields(self):
        graph = facebook.GraphAPI(access_token=self.access_token)

        # populate likes
        likes_page = graph.get_connections('me', 'likes')
        likes = []
        while True:
            likes.extend([object['id'] for object in likes_page['data']])
            if len(likes_page['data']) == 0:
                break

            try:
                likes_page = json.loads(requests.get(likes_page['paging']['next']).content)
            except:
                break

        self.likes.clear()
        for object_id in likes:
            object, _ = FacebookObject.objects.get_or_create(object_id=object_id)
            self.likes.add(object)

        # populate name and picture
        info = graph.get_connections('me', '')
        self.name = "{} {}".format(info['first_name'], info['last_name'])
        self.picture = graph.get_connections('me', 'picture')['url']
        self.save()


class FacebookObject(models.Model):
    object_id = models.CharField(max_length=64, primary_key=True)
