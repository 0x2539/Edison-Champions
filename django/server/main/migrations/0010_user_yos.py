# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('main', '0009_remove_user_friends'),
    ]

    operations = [
        migrations.AddField(
            model_name='user',
            name='yos',
            field=models.ManyToManyField(to='main.User'),
            preserve_default=True,
        ),
    ]
