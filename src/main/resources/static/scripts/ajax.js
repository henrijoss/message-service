$(document).ready(function() {
    timer = setTimeout(loadMessages, 1000);
});
var timer;

function loadMessages() {
    $.ajax({
        url : "/ajax/messages.json?lastClientMessage=" + lastClientMessage,
        success : function(messages) {
            if (messages && messages.length) {
                lastClientMessage = messages[0].date;
                $(".chat-message").first().before(createMessageList(messages));
                $(".ajaxNew").slideDown(600, "swing", function() {
                    $(this).removeClass("ajaxNew");
                });
            }
            timer = setTimeout(loadMessages, 1000);
        },
        error : function() {
            timer = setTimeout(loadMessages, 1000);
        },
        cache : false
    })
}
function createMessageList(messages) {
    var messageList = "";
    for (i in messages) {
        messageList += "<section class='ajaxNew chat-message' style='display:none'>";
        messageList += "<p class='name'><strong>";
        messageList += messages[i].user.username;
        messageList += "</strong></p>";
        messageList += "<p class='massage'>";
        messageList += messages[i].text;
        messageList += "</p>";
        messageList += "<time class='timestamp'><i>";
        messageList += messages[i].date;
        messageList += "</i></time>";
        messageList += "</section>";
    }
    return messageList;
}