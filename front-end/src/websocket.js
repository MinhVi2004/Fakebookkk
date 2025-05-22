import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

let stompClient = null;

export const connectWebSocket = (onMessageReceived) => {
  const socket = new SockJS('http://localhost:8080/ws-chat');
  stompClient = Stomp.over(socket);

  stompClient.connect({}, () => {
    stompClient.subscribe('/topic/public', (msg) => {
      onMessageReceived(JSON.parse(msg.body));
    });
  });
};

export const sendMessage = (message) => {
  if (stompClient && stompClient.connected) {
    stompClient.send('/app/chat.sendMessage', {}, JSON.stringify(message));
  }
};
