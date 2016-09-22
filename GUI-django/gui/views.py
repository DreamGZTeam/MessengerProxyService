import datetime
import json
from django.shortcuts import render
from suds.client import Client
from suds.sax.date import datetime
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
        messeger_list = []
        for bot in a[1]:
            bot_list.append(bot)
            b = client.service.getMessengers(bot)
            for messegers in b[1]:
                messeger_list.append(messegers) 
        return render(request, 'main.html', {'bot_list': bot_list, 'messeger_list': messeger_list })


def messeger(request):
    url = 'http://localhost:8080/MessengerProxyService/MessengerProxyService?wsdl'
    client = Client(url, faults=False)
    client.set_options(service='MessengerProxyServiceImplService', port='MessengerProxyServiceImplPort')
    if request.method == 'GET':
        messeger_name = request.GET.get('messeger_name') 
        bot_name = request.GET.get('bot_name') 
        b = client.service.getMessengers(bot_name)
        for i in b[1]:
            if i.name == messeger_name:
                messeger_id = i.id
        c = client.service.getChats(messeger_id)
        contacts = []
        if c[1] != []:
            for ii in c[1]:
                contacts.append(ii.name)
            return render(request, '_contact_list.html', {'contacts': contacts})        
        else:
            return HttpResponse(
                json.dumps({'text': 'К сожалению для этого мессейджера нет сообщений'}),
                content_type="application/json")


def history(request):
    url = 'http://localhost:8080/MessengerProxyService/MessengerProxyService?wsdl'
    client = Client(url, faults=False)
    client.set_options(service='MessengerProxyServiceImplService', port='MessengerProxyServiceImplPort')
    if request.method == 'GET':
        messeger_name = request.GET.get('messeger_name') 
        bot_name = request.GET.get('bot_name') 
        contact_name = request.GET.get('contact_name')
        b = client.service.getMessengers(bot_name)
        for i in b[1]:
            if i.name == messeger_name:
                messeger_id = i.id
        c = client.service.getChats(messeger_id)
        for i in c[1]:
            if i.name == contact_name:
                contact_id = i.id
        d = client.service.getHistory(messeger_id, contact_id)
        hist = []
        for history_list in d[1]:
            history_time = str(history_list.message.date)
            hist.append([history_list.message.direction, history_time, history_list.message.text])
        return render(request, '_contact_history.html', {'history_list': hist})

def message_send(request):
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