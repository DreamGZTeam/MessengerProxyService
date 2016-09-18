import datetime
import json
from django.shortcuts import render
from suds.client import Client
from django.http import HttpRequest, HttpResponse, JsonResponse
#from .models import Bot, User
# Create your views here.

def main(request):
    url = 'http://localhost:8080/MessengerProxyService_Web/MessengerProxyService?wsdl'
    client = Client(url, faults=False)
    client.set_options(service='MessengerProxyServiceImplService', port='MessengerProxyServiceImplPort')
    if request.method == 'GET':
        a = client.service.getProtocols()
        bot_list = []
        for bot in a[1]:
            bot_list.append(bot)
        return render(request, 'main.html', {'bot_list': bot_list})


def bot(request):
    url = 'http://localhost:8080/MessengerProxyService_Web/MessengerProxyService?wsdl'
    client = Client(url, faults=False)
    client.set_options(service='MessengerProxyServiceImplService', port='MessengerProxyServiceImplPort')
    if request.method == 'GET':
        bot_name = request.GET.get('bot') 
        b = client.service.getMessengers(bot_name)
        bot_id = [i[1] for i in b[1]]
        c = client.service.getContacts(bot_id)
        contacts = []
        if c[1] != []:
            for ii in c[1]:
                contacts.append(ii[1])
            return render(request, '_contact_list.html', {'contacts': contacts})        
            return HttpResponse(request, {'contacts': 'К сожалению контактов нет'})
#            json.dumps({'contacts' : contacts}),
#        return render(request, '_contact_list.html', {'contacts': contacts})
#            content_type="application/json"
#        )

#def bot1(request):
#    return HttpResponse(
#       json.dumps({'contacts' : 'ok'}),
#        content_type="application/json"
#        )

def history(request):
    url = 'http://localhost:8080/MessengerProxyService_Web/MessengerProxyService?wsdl'
    client = Client(url, faults=False)
    client.set_options(service='MessengerProxyServiceImplService', port='MessengerProxyServiceImplPort')
    if request.method == 'GET':
        history = []
        bot_name = request.GET.get('bot')
        username = request.GET.get('user')
        b = client.service.getMessengers(bot_name)
        bot_id = [i[1] for i in b[1]]
        c = client.service.getContacts(bot_id)
        for ii in c[1]:
            if ii.firstName == username:
                user_id = ii.id
        h = client.service.getHistory(bot_id, user_id)
        for i0 in h[1]:
            history.append(i0) 
        return render(request, '_contact_history.html', {'history_list': history})

def message(request):
    if request.method == "POST":
        text = []
        text.append(request.POST.get('text'))
        if text != []:
            return render(request,'_contact_history.html', {'history_list': text})
        else:
            return HttpResponse(
                json.dumps({'text' : 'ok'}),
                content_type="application/json"
                )