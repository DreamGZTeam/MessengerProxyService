import datetime
import json
from django.shortcuts import render
from suds.client import Client
from django.http import HttpRequest, HttpResponse, JsonResponse
#from .models import Bot, User
# Create your views here.

def main(request):
    url = 'http://localhost:8080/MessengerProxyService/MessengerProxyService?wsdl'
    client = Client(url, faults=False)
    client.set_options(service='MessengerProxyServiceImplService', port='MessengerProxyServiceImplPort')
    if request.method == 'GET':
        a = client.service.getProtocols()
        bot_list = []
        for bot in a[1]:
            bot_list.append(bot)
        bot_len = 12 // len(bot_list)
        return render(request, 'main.html', {'bot_list': bot_list, 'bot_len': bot_len })


def messeger(request):
    url = 'http://localhost:8080/MessengerProxyService/MessengerProxyService?wsdl'
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
    url = 'http://localhost:8080/MessengerProxyService/MessengerProxyService?wsdl'
    client = Client(url, faults=False)
    client.set_options(service='MessengerProxyServiceImplService', port='MessengerProxyServiceImplPort')
    if request.method == 'GET':
        messeger_name = request.GET.get('messeger')
        chat_name = request.GET.get('user')
        a = client.service.getProtocols()
        for i in a[1]:
            b = client.service.getMessengers(i)
            for ii in b[1]:
                if ii.name == messeger_name:
                    messeger_id = ii.id
        c = client.service.getChats(messeger_id)
        for i in c[1]:
            if i.name == chat_name
                chat_id = i.id
        d = client.service.getHistory(messeger_id, chat_id)
        hist = []
        for history_list in d[1]:
            history_time = datetime.datetime.strptime(history_list.date, '%Y-%m-%d %H:%M:%S')
            history_list.append([history_list.date, history_time, history_list.text])
        return render(request, '_contact_history.html', {'history_list': history})

def message(request):
    url = 'http://localhost:8080/MessengerProxyService/MessengerProxyService?wsdl'
    client = Client(url, faults=False)
    client.set_options(service='MessengerProxyServiceImplService', port='MessengerProxyServiceImplPort')
    if request.method == "POST":
        text = []
        text = request.POST.get('text')
        messeger_id = request.POST.get('messeger_active')
        contact_id = request.POST.get('contact_active')
        if text != []:
            callback = client.service.sendTextMessage(messeger_id, contact_id, text)
            return HttpResponse(
                json.dumps({'text' : callback}),
                content_type="application/json"
                )

def interactive(request):
    url = 'http://localhost:8080/MessengerProxyService/MessengerProxyService?wsdl'
    client = Client(url, faults=False)
    client.set_options(service='MessengerProxyServiceImplService', port='MessengerProxyServiceImplPort')
    if request.method == "GET":
        messeger_name = request.GET.get('messeger')
        a = client.service.getProtocols()
        for i in a[1]:
            b = client.service.getMessengers(i)
            for ii in b[1]:
                if ii.name == messeger_name:
                    messeger_id = ii.id
        callback = client.service.setInteractive(messeger_id, True)
        return HttpResponse(
                json.dumps({'text' : callback}),
                content_type="application/json"
                )