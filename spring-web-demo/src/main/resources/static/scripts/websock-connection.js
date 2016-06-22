var stompClient = null;

function connect() {
    var socket = new SockJS('/hello');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/greetings', function(greeting){
            newMessage(JSON.parse(greeting.body).content);
        });
    });
}

function newMessage(message) {
    console.log(message)
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendContent() {
    var content = document.getElementById('content').value;
    stompClient.send("/app/hello", {}, JSON.stringify({ 'content': content}));
}