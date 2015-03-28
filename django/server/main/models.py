from django.db import models

class Ding(models.Model):
    created_at = models.DateTimeField(auto_now_add=True)
    sent = models.BooleanField(default=False)
