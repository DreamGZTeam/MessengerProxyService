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
        bot_name = request.POST.get('bot')
         
        b = client.service.getMessengers(bot_name)
        bot_id = [i[1] for i in b[1]]
        c = client.service.getContacts(bot_id)
        contacts = []
        for ii in c[1]:
            contacts.append(ii[1])
        return render(request, '_contact_list.html', {'contacts': contacts})        
#        return HttpResponse(
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
        username = request.POST.get('user')
        bb = request.POST.get('m_token')
        h = client.service.getHistory(bb, username)
        for i in h[1]:
            history.append(i[1]) 
        return render(request, '_contact_history.html', {'history': history})