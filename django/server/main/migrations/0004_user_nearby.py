# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('main', '0003_facebooklike_user'),
    ]

    operations = [
        migrations.AddField(
            model_name='user',
            name='nearby',
            field=models.ManyToManyField(related_name='nearby_rel_+', to='main.User'),
            preserve_default=True,
        ),
    ]
