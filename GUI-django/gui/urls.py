from django.conf.urls import url
from gui import views

app_name = 'kpi'
urlpatterns = [
    url(r'^$', views.main, name='main'),
    url(r'^bot', views.bot, name='bot'),
    url(r'^user', views.history, name='history')
    ]