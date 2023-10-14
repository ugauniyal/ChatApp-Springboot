// Declare stompClient and socket in a broader scope
let stompClient;
let socket;

// Function to get the channelName from the URL
function getChannelNameFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);
    const channelName = urlParams.get('channelName');
    console.log('Channel Name:', channelName); // Debugging
    return channelName;
}

// Function to display a received message in the chat interface
function displayMessage(message) {
    const chatMessages = document.getElementById('chat-messages');
    const messageDiv = document.createElement('div');
    const messageDiv1 = document.createElement('div');

    if (!message || !message.userModel || !message.userModel.username) {
        console.error('Invalid message format:', message);
        return;
    }

    const username = message.userModel.username;

    messageDiv1.textContent = `${username}: ${message.messageText}`;
    chatMessages.appendChild(messageDiv1);
}

// Function to send a message to the WebSocket server
function sendMessage() {
    const channelName = getChannelNameFromUrl();
    console.log('Before connecting to WebSocket, channelName:', channelName);

    const messageInput = document.getElementById('message-input');
    const message = messageInput.value.trim(); // Trim whitespace

    if (message) {
        // Check if the WebSocket connection is established
        if (stompClient && stompClient.connected) {
            // Send the message to the server as a JSON string
            stompClient.send(`/app/chat/${channelName}.sendMessage`, {}, JSON.stringify({ message_text: message }), function () {
                // After the message is sent, clear the input field
                messageInput.value = '';
            });
        } else {
            console.error('WebSocket connection not established');
            messageInput.value = '';
        }
    }
}

// Function to fetch and display old messages
async function fetchAndDisplayOldMessages() {
    try {
        const channelName = getChannelNameFromUrl();
        const response = await fetch(`/chat/messages/${channelName}`);
        if (response.ok) {
            const oldMessages = await response.json();

            const chatMessages = document.getElementById('chat-messages');
            const messageInput = document.getElementById('message-input');
            chatMessages.innerHTML = ''; // Clear the existing messages
            messageInput.value = '';

            // Display old messages after they are fetched from the server
            oldMessages.forEach(displayMessage);
        } else {
            console.error('Failed to fetch old messages:', response.status);
        }
    } catch (error) {
        console.error('Error fetching old messages:', error);
    }
}

// Connect to the WebSocket server after obtaining the channelName
function connectToWebSocket() {
    const channelName = getChannelNameFromUrl();

    const wsUrl = `http://localhost:8080/chat?channelName=${channelName}`;

    console.log('WebSocket URL:', wsUrl);
    socket = new SockJS(wsUrl);
    stompClient = Stomp.over(socket);

    // Subscribe to the specific channel topic
    stompClient.connect({}, function (frame) {
        console.log('WebSocket connection opened:', frame);

        // Now that the WebSocket is connected, fetch and display old messages
        fetchAndDisplayOldMessages();
    });
}

// Call the connectToWebSocket function to initiate the WebSocket connection
connectToWebSocket();
