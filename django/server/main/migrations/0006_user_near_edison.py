# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('main', '0005_auto_20150328_1648'),
    ]

    operations = [
        migrations.AddField(
            model_name='user',
            name='near_edison',
            field=models.BooleanField(default=False),
            preserve_default=True,
        ),
    ]
