# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('main', '0010_user_yos'),
    ]

    operations = [
        migrations.AddField(
            model_name='facebookobject',
            name='name',
            field=models.CharField(default='', max_length=128),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='facebookobject',
            name='picture',
            field=models.URLField(default=''),
            preserve_default=False,
        ),
        migrations.AlterField(
            model_name='user',
            name='yos',
            field=models.ManyToManyField(related_name='yos_received', to='main.User'),
            preserve_default=True,
        ),
    ]
