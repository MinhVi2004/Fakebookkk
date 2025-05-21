import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

let stompClient = null;

export const connectNotification = (userId, onNotification) => {
  const socket = new SockJS('http://localhost:8080/ws');
  stompClient = Stomp.over(socket);

  stompClient.connect({}, () => {
    stompClient.subscribe(`/topic/friend-accepted/${userId}`, (message) => {
      const notification = JSON.parse(message.body);
      onNotification(notification);
    });
  });
};

export const disconnectNotification = () => {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
};
