import datetime
from django.shortcuts import render
from .models import Bot, User

# Create your views here.

def main(request):
    return render(request, 'main.html')

def bot(request):
    bots = Bot.objects.all()
    return render(request, 'bot.html', { 'bots': bots } )
