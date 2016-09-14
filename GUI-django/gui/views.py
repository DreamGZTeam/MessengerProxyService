import datetime
from django.shortcuts import render
from .models import Bot, User
from suds.client import Client

# Create your views here.

def main(request):
    url = 'http://localhost:8080/MessengerProxyService_Web/MessengerProxyService?wsdl'
    client = Client(url, faults=False)
    client.set_options(service='MessengerProxyServiceImplService', port='MessengerProxyServiceImplPort')
    a = client.service.getProtocols()
    bot_list = []
    cont_id = []
    history_list = []
    for bot in a[1]:
        bot_list.append(bot)

    b = client.service.getMessengers(a[1])
    mes_id = [i[1] for i in b[1]]
    c = client.service.getContacts(mes_id)
    
    for ii in c[1]:
        cont_id.append(ii[1])
        d = client.service.getHistory(mes_id, ii[0])
        for mess in d[1]:
            history_list.append(mess) 
    return render(request, 'main.html', {'cont_id': cont_id, 'bot_list': bot_list, 'history_list': history_list})

def bot(request):
    bots = Bot.objects.all()
    return render(request, 'bot.html', { 'bots': bots } )
