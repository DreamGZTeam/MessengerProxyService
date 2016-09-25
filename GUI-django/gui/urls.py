from django.conf.urls import url
from gui import views

app_name = 'kpi'
urlpatterns = [
    url(r'^$', views.main, name='main'),
    url(r'^protocol', views.protocol, name='protocol'),
    url(r'^messeger', views.messeger, name='messeger'),
    url(r'^history', views.history, name='history'),
    url(r'^message_send', views.message_send, name='message_send'),
    url(r'^handler_list', views.handler_list, name='handler_list'),
    url(r'^set_interactive', views.set_interactive, name='set_interactive')
    ]