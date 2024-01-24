const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/api/v1/websocket',
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/greetings', (greeting) => {
        console.log('Received: ' + greeting.body)
        showGreeting(greeting.body.content);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    try {
        console.log("Connecting to WS...");
        stompClient.activate();
    } catch (e) {
        console.log("Error connecting to WS: " + e)
    }
}

function disconnect() {
    try {
        stompClient.deactivate();
        setConnected(false);
        console.log("Disconnected");
    } catch (e) {
        console.log("Error disconnecting from WS: " + e)
    }
}

function sendName() {
    stompClient.publish({
        destination: "/app/hello",
        // body: JSON.stringify({'name': $("#name").val()})
        body: $("#name").val()
    });
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#send" ).click(() => sendName());
});
