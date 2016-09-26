$(document).ready(function () {
// Выбор бота
    $('#menu_protocol').on('click', 'p', function(){
        var s_protocol = this;
        window.s_protocol = s_protocol;
        protocol(s_protocol);
    });

// Выбор мессейджера
    $('#menu_messeger').on('click', 'p', function(){
        var s_messeger = this;
        window.s_messeger = s_messeger;
        messeger(s_protocol, s_messeger);
    });
// Выбор контакта
    $('#contact_list').on('click', 'p', function(){
        var s_contact = this;
        window.s_contact = s_contact;
        history(s_protocol, s_messeger, s_contact);
    });
//Отправка сообщения
    $('#message_form').on('submit', function(){
        var message_text = $('textarea').val();
        message_send(s_protocol, s_messeger, s_contact, message_text);
    });
//Выбор хендлера
    $('#menu_handler').on('click', 'p', function(){
        var s_handler = this;
        window.s_handler = s_handler;
        handler(s_protocol, s_messeger, s_handler);
        if (s_handler.id ='Auth') {
            $('#token').removeClass('hidden');
            $('#token').addClass('active');
        } else {
            $('#token').removeClass('active');
            $('#token').addClass('hidden');
        }
    });
    $('#inc').on('click', 'a', function(){
        if ($('#prop').hasClass('hidden')) {
            $('#prop').removeClass('hidden');
            $('#prop').addClass('show');            
        } else{
            $('#prop').removeClass('show');
            $('#prop').addClass('hidden');
        }
    });

    $('#token').on('click', function(){
        token(s_protocol, s_messeger, s_handler);
    }); 
});

// делаем протокол активным и отображаем доступные для него мессейджеры
function protocol(s_protocol){
    $('#menu_messeger').children().remove();
    $('#menu_protocol p').css({'background-color' : ''});
    $(s_protocol).css({'background-color': '#4D5AEA', 'border-radius' : '5px'});
    $.ajax({
        url : "protocol", 
        type : "GET", 
        data : {'protocol_name': s_protocol.id},
        success : function(json){
            $('#menu_messeger').append(json);
        }
    });
    $('#history_list .col-md-12').remove();
    $('#contact_list .col-md-12').remove();
    $('#menu_handler').children().remove();
};

// делаем мессейджер активным и получаем контакты для него
function messeger(s_protocol,s_messeger) {
    $('#menu_messeger p').css({'background-color' : ''});
    $(s_messeger).css({'background-color': '#4D5AEA', 'border-radius' : '5px'});
    $.ajax({
        url : "messeger", 
        type : "GET", 
        data : {'protocol_name': s_protocol.id, 'messeger_name' : s_messeger.id},
        success : function(json){
            $('#contact_list .col-md-12').remove();
            $('#contact_list').append(json);
        }
    });
    $.ajax({
        url : "handler_list", 
        type : "GET", 
        data : {'protocol_name': s_protocol.id, 'messeger_name' : s_messeger.id},
        success : function(json){
            $('#menu_handler').children().remove();
            $('#menu_handler').append(json);
        }
    });
    $('#history_list .col-md-12').remove(); 
};

//Отправка сообщения
function message_send(s_protocol, s_messeger, s_contact, message_text) {
    event.preventDefault();
    $.ajax({
        url : 'message_send',
        type : 'post',
        data : { 'protocol_name' : s_protocol.id, 'messeger_name' : s_messeger.id, 'contact_name' : s_contact.id, 'text' : message_text},
        success : function(json){
            $('textarea').val('');
            history(s_protocol, s_messeger, s_contact);
        }
    });
};

//получаем историю по выбранному контакту
function history(s_protocol, s_messeger, s_contact) {
    $.ajax({
        url : 'history',
        type : 'GET',
        data : { 'protocol_name' : s_protocol.id, 'messeger_name' : s_messeger.id, 'contact_name' : s_contact.id},
        success : function(json){
            $('#history_list .col-md-12').remove();
            $('#history_list').append(json);
            $('#contact_list p').css({'background-color' : ''});
            $(s_contact).css({'background-color': '#2871af', 'border-radius' : '5px'});
        }

    });
    //var height = $('#history_list').height();
    $('#history_list').animate({'scrollTop':9999}, 'slow');

       //var timeID = setInterval(history(s_protocol, s_messeger, s_contact), 1000);
};

// Меняем состояние хендлера
function handler(s_protocol,s_messeger, s_handler) {
    $('#menu_handler p').css({'background-color' : ''});
    $(s_handler).css({'background-color': '#4D5AEA', 'border-radius' : '5px'});
    $.ajax({
        url : "set_interactive", 
        type : "GET", 
        data : {'protocol_name': s_protocol.id, 'messeger_name' : s_messeger.id, 'handler_name' : s_handler.id},
        success : function(json){
            $('#handler_status button').text(json['text']);
        }
    });
};

// Получаем токен для хендлера
function token(s_protocol,s_messeger, s_handler) {
    $('#menu_handler p').css({'background-color' : ''});
    $(s_handler).css({'background-color': '#4D5AEA', 'border-radius' : '5px'});
    $.ajax({
        url : "token", 
        type : "GET", 
        data : {'protocol_name': s_protocol.id, 'messeger_name' : s_messeger.id, 'handler_name' : s_handler.id},
        success : function(json){
            alert('Токен для текущего имени = ' + json['token']);
        }
    });
};

// Стандартную функция передачи токена защиты для форм
$(function() {


    // This function gets cookie with a given name
    function getCookie(name) {
        var cookieValue = null;
        if (document.cookie && document.cookie != '') {
            var cookies = document.cookie.split(';');
            for (var i = 0; i < cookies.length; i++) {
                var cookie = jQuery.trim(cookies[i]);
                // Does this cookie string begin with the name we want?
                if (cookie.substring(0, name.length + 1) == (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }
    var csrftoken = getCookie('csrftoken');

    /*
    The functions below will create a header with csrftoken
    */

    function csrfSafeMethod(method) {
        // these HTTP methods do not require CSRF protection
        return (/^(GET|HEAD|OPTIONS|TRACE)$/.test(method));
    }
    function sameOrigin(url) {
        // test that a given url is a same-origin URL
        // url could be relative or scheme relative or absolute
        var host = document.location.host; // host + port
        var protocol = document.location.protocol;
        var sr_origin = '//' + host;
        var origin = protocol + sr_origin;
        // Allow absolute or scheme relative URLs to same origin
        return (url == origin || url.slice(0, origin.length + 1) == origin + '/') ||
            (url == sr_origin || url.slice(0, sr_origin.length + 1) == sr_origin + '/') ||
            // or any other URL that isn't scheme relative or absolute i.e relative.
            !(/^(\/\/|http:|https:).*/.test(url));
    }

    $.ajaxSetup({
        beforeSend: function(xhr, settings) {
            if (!csrfSafeMethod(settings.type) && sameOrigin(settings.url)) {
                // Send the token to same-origin, relative URLs only.
                // Send the token only if the method warrants CSRF protection
                // Using the CSRFToken value acquired earlier
                xhr.setRequestHeader("X-CSRFToken", csrftoken);
            }
        }
    });

});