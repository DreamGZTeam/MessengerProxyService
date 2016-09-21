from django.conf.urls import url
from gui import views

app_name = 'kpi'
urlpatterns = [
    url(r'^$', views.main, name='main'),
    url(r'^messeger', views.messeger, name='messeger'),
    url(r'^user', views.history, name='history'),
    url(r'^message', views.message, name='message'),
    url(r'^interactive', views.interactive, name='interactive')
    ]