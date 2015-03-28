# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('main', '0006_user_near_edison'),
    ]

    operations = [
        migrations.AddField(
            model_name='user',
            name='facebook_id',
            field=models.CharField(max_length=64, unique=True, null=True, blank=True),
            preserve_default=True,
        ),
        migrations.AddField(
            model_name='user',
            name='id',
            field=models.AutoField(auto_created=True, primary_key=True, default=None, serialize=False, verbose_name='ID'),
            preserve_default=False,
        ),
        migrations.AlterField(
            model_name='user',
            name='mac',
            field=models.CharField(max_length=32, unique=True, null=True, blank=True),
            preserve_default=True,
        ),
    ]
