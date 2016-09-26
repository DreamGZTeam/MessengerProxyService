import datetime
import json
import os
from django.shortcuts import render
from suds.client import Client
from suds.sax.date import datetime
from django.http import HttpRequest, HttpResponse, JsonResponse
#from .models import Bot, User
# Create your views here.
f = open('conf.txt', 'r')

url = f.read()
client = Client(url, faults=False)
client.set_options(service='MessengerProxyServiceImplService', port='MessengerProxyServiceImplPort')

def main(request):
    if request.method == 'GET':
        a = client.service.getProtocols()
        protocol_list = []
        messeger_list = []
        for protocol in a[1]:
            protocol_list.append(protocol)
        return render(request, 'main.html', {'protocol_list': protocol_list})


def protocol(request):
    if request.method == 'GET':
        a = client.service.getProtocols()
        protocol_list = []
        messeger_list = []
        protocol_name = request.GET.get('protocol_name')
        b = client.service.getMessengers(protocol_name)
        for messegers in b[1]:
            messeger_list.append(messegers)
        return render(request, '_messeger_list.html', {'messeger_list': messeger_list })


def messeger(request):
    if request.method == 'GET':
        messeger_name = request.GET.get('messeger_name') 
        protocol_name = request.GET.get('protocol_name') 
        b = client.service.getMessengers(protocol_name)
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
                json.dumps({'text': 'К сожалению для этого мессейджера нет контактов'}),
                content_type="application/json")


def history(request):
    if request.method == 'GET':
        messeger_name = request.GET.get('messeger_name') 
        protocol_name = request.GET.get('protocol_name') 
        contact_name = request.GET.get('contact_name')
        b = client.service.getMessengers(protocol_name)
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
            if hasattr(history_list, 'contact'):
                Name = history_list['contact'].firstName
                time = str(history_list.message.date.strftime('%Y-%m-%d %H:%M:%S')) 
                history_time = str(time + ' ' + Name)
            else:
                history_time = str(history_list.message.date.strftime('%Y-%m-%d %H:%M:%S'))
            hist.append([history_list.message.direction, history_time, history_list.message.text])
        hist.sort(key=lambda t: t[1])
#        hist.reverse()
        return render(request, '_contact_history.html', {'history_list': hist})

def message_send(request):
    if request.method == 'POST':
        text = request.POST.get('text')
        messeger_name = request.POST.get('messeger_name') 
        protocol_name = request.POST.get('protocol_name') 
        contact_name = request.POST.get('contact_name')
        b = client.service.getMessengers(protocol_name)
        for i in b[1]:
            if i.name == messeger_name:
                messeger_id = i.id
        c = client.service.getChats(messeger_id)
        for i in c[1]:
            if i.name == contact_name:
                contact_id = i.id
        if text != []:
            client.service.sendTextMessage(messeger_id, contact_id, text)
            return HttpResponse(
                json.dumps({'text' : 'ok'}),
                content_type="application/json"
                )


def handler_list(request):
    if request.method == 'GET':
        messeger_name = request.GET.get('messeger_name') 
        protocol_name = request.GET.get('protocol_name') 
        b = client.service.getMessengers(protocol_name)
        handler_list = []
        for i in b[1]:
            if i.name == messeger_name:
                messeger_id = i.id
        c = client.service.getHandlers(messeger_id)
        for i in c[1]:
            handler_list.append(i.name)
        return render(request, '_handler_list.html', {'handler_list': handler_list})

def set_interactive(request):
    if request.method == 'GET':
        messeger_name = request.GET.get('messeger_name') 
        protocol_name = request.GET.get('protocol_name')
        handler_name = request.GET.get('handler_name') 
        b = client.service.getMessengers(protocol_name)
        handler_list = []
        for i in b[1]:
            if i.name == messeger_name:
                messeger_id = i.id
        c = client.service.getHandlers(messeger_id)
        for i in c[1]:
            if i.name == handler_name:
                set_status = i.active
                handler_id = i.id
        if set_status == False:
            client.service.setHandlerMode(messeger_id, handler_id, True)
            result = 'ON'
        if set_status == True:
            client.service.setHandlerMode(messeger_id, handler_id, False)
            result = 'OFF'
        return HttpResponse(
                json.dumps({'text' : result}),
                content_type="application/json"
                )

def token(request):
    if request.method == 'GET':
        messeger_name = request.GET.get('messeger_name') 
        protocol_name = request.GET.get('protocol_name')
        handler_name = request.GET.get('handler_name') 
        b = client.service.getMessengers(protocol_name)
        for i in b[1]:
            if i.name == messeger_name:
                messeger_id = i.id
        c = client.service.getHandlers(messeger_id)
        for i in c[1]:
            if i.name == handler_name:
                handler_id = i.id
        token = client.service.generateAuthToken(messeger_id, handler_id)
        return HttpResponse(
                    json.dumps({'token' : token[1]}),
                    content_type="application/json"
                    )