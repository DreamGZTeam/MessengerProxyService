from django.db import models

# Create your models here.
class Bot(models.Model):
    system = models.CharField(max_length=20)
    name = models.CharField(max_length=64)

class User(models.Model):
    name = models.CharField(max_length=64)
    chatrooms = models.IntegerField(default=0)
