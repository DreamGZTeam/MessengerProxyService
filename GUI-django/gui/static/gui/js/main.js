$(document).ready(function () {
// Выбор бота
    $('#menu_bot').on('click', 'p', function(){
        var s_bot = this.id;
        bot_active(s_bot);
        window.s_bot = s_bot;
    });

// Выбор мессейджера
    $('#menu_messeger').on('click', 'p', function(){
        var s_messeger = this.id;
        messeger(s_bot, s_messeger);
        window.s_messeger = s_messeger;
    });
    $('#contact_list').on('click', 'p', function(){
        var s_contact = this.id;
        history(s_bot, s_messeger, s_contact);
        window.s_contact = s_contact;
    });

    $('#message').on('submit', function(){
        var message_text = $('textarea').text;
        console.log(message_text)
        message_send(s_bot, s_messeger, s_contact, message_text);
    });
});


//получаем историю по выбранному контакту
function history(s_bot, s_messeger, s_contact) {
    console.log('History called for ' + s_contact);
    $.ajax({
        url : 'history',
        type : 'GET',
        data : { 'bot_name' : s_bot, 'messeger_name' : s_messeger, 'contact_name' : s_contact},
        success : function(json){
            $('#history_list .col-md-12').remove();
            $('#history_list').append(json);
        }
    });
};

function bot_active(s_bot){
    $('#menu_bot p').css({'background-color' : ''});
    $(s_bot).css({'background-color': '#4D5AEA', 'border-radius' : '5px'});
};

function messeger(s_bot,s_messeger) {
    $('#menu_messeger p').css({'background-color' : ''});
    $(s_messeger).css({'background-color': '#4D5AEA', 'border-radius' : '5px'});
    console.log('ajax call ' + s_bot + ' ' + s_messeger)
    $.ajax({
        url : "messeger", // the endpoint
        type : "GET", // http method
        data : {'bot_name': s_bot, 'messeger_name' : s_messeger},
        success : function(json){
            $('#contact_list .col-md-12').remove();
            $('#contact_list').append(json);
        }
    }); 
};
//Отправка сообщения

function message_send(s_bot, s_messeger, s_contact, message_text) {
    event.preventDefault();
    $.ajax({
        url : 'message_send',
        type : 'post',
        data : { 'bot_name' : s_bot, 'messeger_name' : s_messeger, 'contact_name' : s_contact, 'text' : message_text},
        success : function(json){
            console.log('Success ' + json['text']);
            $('#history_list').append(json);
            $('#message textarea').val('');
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