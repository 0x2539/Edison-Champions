# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('main', '0002_ding_sent'),
    ]

    operations = [
        migrations.CreateModel(
            name='FacebookLike',
            fields=[
                ('object_id', models.CharField(max_length=64, serialize=False, primary_key=True)),
            ],
            options={
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='User',
            fields=[
                ('mac', models.CharField(max_length=32, serialize=False, primary_key=True)),
                ('picture', models.URLField()),
                ('name', models.CharField(max_length=128)),
                ('friends', models.ManyToManyField(related_name='friends_rel_+', to='main.User')),
                ('likes', models.ManyToManyField(to='main.FacebookLike')),
            ],
            options={
            },
            bases=(models.Model,),
        ),
    ]
