from django.conf.urls import url
from gui import views

app_name = 'kpi'
urlpatterns = [
    url(r'^$', views.main, name='main'),
    url(r'^messeger', views.messeger, name='messeger'),
    url(r'^history', views.history, name='history'),
    url(r'^message_send', views.message_send, name='message_send'),
    url(r'^interactive', views.interactive, name='interactive')
    ]