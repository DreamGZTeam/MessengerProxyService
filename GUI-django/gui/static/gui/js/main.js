$(document).ready( function () {
    // Выбор бота
    $('#navbar').one('click', 'p', function(){

    });
    // Отправка сообщения
    $('#message_form').on('submit', function(){
        event.preventDefault();
        var messeger_active = $('#messeger_list .active').id;
        var contact_active = $('#contact_list .active').id;
        console.log('bot = '+ bot_active + 'contact_active = '+ contact_active);
        console.log("form submitted! " + $('#message textarea').val());
        message($('#message textarea').val(), messeger_active, contact_active);
    });
});

//Получаем список контактов бота
function messeger(bot_name) {
    $(this).addClass('bot_active')
    $.ajax({
        url : "bot", // the endpoint
        type : "GET", // http method
        data : {'bot' : bot_name},
        success : function(json){
            $('#contact_list .col-md-12').remove();
            $('#contact_list').append(json);
            $('#contact_list').on('click', 'p', function(){
                    $('#contact_list p').css('background-color', '');
                    $('#contact_list p').removeClass('contact_active')
                    var username = this.id;
                    history(username, bot_name);
                    $(this).css({'background-color': '#4D5AEA', 'border-radius' : '5px'});
                    $(this).addClass('contact_active')
                });
        }
    });
};

//получаем историю по выбранному контакту
function history(username, bot_name) {
//    console.log('History called for ' + username);
    $.ajax({
        url : 'user',
        type : 'GET',
        data : { 'bot' : bot_name, 'user' : username},
        success : function(json){
            $('#history_list .col-md-12').remove();
            $('#history_list').append(json);
        }
    });
};

//Отправка сообщения
function message(message,messeger_active, contact_active) {
    event.preventDefault();
    $.ajax({
        url : 'message',
        type : 'post',
        data : { 'text' : message, 'messeger_active' : messeger_active, contact_active: contact_active},
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