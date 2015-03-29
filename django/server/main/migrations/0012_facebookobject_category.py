# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('main', '0011_auto_20150329_0346'),
    ]

    operations = [
        migrations.AddField(
            model_name='facebookobject',
            name='category',
            field=models.CharField(default='', max_length=128),
            preserve_default=False,
        ),
    ]
