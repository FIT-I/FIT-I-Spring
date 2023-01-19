importScripts('https://www.gstatic.com/firebasejs/5.9.2/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/5.9.2/firebase-messaging.js');

// Initialize Firebase
let firebaseConfig = {
    apiKey: "AIzaSyBRJfqvIBHZ9GafjSf5xw7D3Of5MhlBVlY",
    authDomain: "fit-i-b4618.firebaseapp.com",
    projectId: "fit-i-b4618",
    storageBucket: "fit-i-b4618.appspot.com",
    messagingSenderId: "549805716441",
    appId: "1:549805716441:web:8414c5a176309158011fc8",
    measurementId: "G-NEGJ3L3XVX"
};
firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();