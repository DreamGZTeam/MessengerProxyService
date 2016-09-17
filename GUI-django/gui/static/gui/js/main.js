$('#navbar').on('click', 'p', function(){
    bot();
});

function bot() {
    $.ajax({
        url : "bot", // the endpoint
        type : "GET", // http method
        data : {'bot' : 'Telegram'},
        success : function(json){
            console.log(json);
            $('#contact_list').prepend(json);
        }
    });
};

function history(username) {
    $.ajax({
        url : 'bot/' + username,
        type : 'GET',
        data : {'user' : username, 'm_token' : m_token},
        success : function(json){
            console.log(json);
            $('#history_list').prepend(json);
        }
    });
};