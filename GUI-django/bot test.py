from suds.client import Client

url = 'http://localhost:8080/MessengerProxyService/MessengerProxyService?wsdl'
client = Client(url, faults=False)
client.set_options(service='MessengerProxyServiceImplService')
#a = client.service.getProtocols()
#b = client.service.getMessengers(a[1])
print(client)
bot_name = 'Telegram'
messeger_name = 'GZGZ2Bot'

url = 'http://localhost:8080/MessengerProxyService/MessengerProxyService?wsdl'
client = Client(url, faults=False)
client.set_options(service='MessengerProxyServiceImplService', port='MessengerProxyServiceImplPort')

messeger_name = 'GZGZ2Bot'
bot_name = 'Telegram'
contact_name = 'GZGroup'
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
for i in d[1]:
    print(i.message)
#c = client.service.getChats(meeseger_id)
#contacts = []
#if c[1] != []:
#    for ii in c[1]:
#        contacts.append(ii[1])
#    print(contacts)



"""a = client.service.getProtocols()
bot_list = []
contacts_list = []
for bot in a[1]:
    b = client.service.getMessengers(bot)
    for mes in b[1]:
        if mes.name == messeger_name:
            c = client.service.getChats(mes.id)
            for contacts in c[1]:
                contacts_list.append(contacts.name)
print (contacts_list)"""