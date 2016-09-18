// получаем список контактов
$(document).ready( function () {
    $('#navbar').on('click', 'p', function(){
        var bot_id = this.id;
        bot(bot_id);
    });
});

function bot(bot_name) {
//    console.log('Bot '+ bot_name + ' called')
    $.ajax({
        url : "bot", // the endpoint
        type : "GET", // http method
        data : {'bot' : bot_name},
        success : function(json){
            $('#contact_list').append(json);
            $('#contact_list').on('click', 'p', function(){
                    var username = this.id;
                    history(username, bot_name);
                });
        }
    });
};

function history(username, bot_name) {
//    console.log('History called for ' + username);
    $.ajax({
        url : 'user',
        type : 'GET',
        data : { 'bot' : bot_name, 'user' : username},
        success : function(json){
            $('#history_list').append(json);
        }
    });
};