from suds.client import Client

url = 'http://localhost:8080/MessengerProxyService/MessengerProxyService?wsdl'
client = Client(url, faults=False)
client.set_options(service='MessengerProxyServiceImplService')
print (client)
a = client.service.getProtocols()
print(a[1])
b = client.service.getMessengers('Telegram')
#            getChats(xs:string arg0)
 #           getHistory(xs:string arg0, xs:string arg1)
  #          getMessengers(xs:string arg0)
   #         getProtocols()
    #        sendTextMessage(xs:string arg0, xs:string arg1, xs:string arg2)
     #       setInteractive(xs:string arg0, xs:boolean arg1)
#d = client.service.getHistory(bb, ii[0])   
b = client.service.getMessengers('Telegram')
b = b[1]
messeger_id = b[0].id
c = client.service.getChats(messeger_id)
print(c[1])
#c = c[1]
#messeger_chats = c[0].id
messeger_chats = [i.id for i in c[1]]
print(messeger_chats)
d = client.service.getHistory(messeger_id,messeger_chats)
print(d[1])
hist = []
for messeg in d[1]:
    hist.append([messeg.date, messeg.direction, messeg.text])
print(hist)
mess_name = 'TelegramBot_1'
a = client.service.getProtocols()
for i in a[1]:
    b = client.service.getMessengers(i)
    for ii in b[1]:
    	print(ii)
    	if ii.name == mess_name:
            messeger_id = ii.id
print(messeger_id)