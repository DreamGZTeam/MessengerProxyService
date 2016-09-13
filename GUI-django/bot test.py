from suds.client import Client

url = 'http://localhost:8080/MessengerProxyService_Web/MessengerProxyService?wsdl'
client = Client(url, faults=False)
client.set_options(service='MessengerProxyServiceImplService')

a = client.service.getProtocols()
b = client.service.getMessengers(a[1])
bb = '181000542:AAHLrSjDPKhJQUe8AWrC3RZjQcXIT46_Y2E'
c = client.service.getContacts(bb)
#cc = 'Kirill'
#d = client.service.getHistory(bb, cc)
print(client)
print(a[1])
print(b[1])
print(c)
#print(d[1])