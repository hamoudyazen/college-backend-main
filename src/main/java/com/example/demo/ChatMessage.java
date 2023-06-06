package com.example.demo;

import java.util.Date;
import java.util.List;

public class ChatMessage {
    private String chatID;
    private String user1ID;
    private String user2ID;
    private MessagesArray messagesArray;

    public ChatMessage() {
    }

    public ChatMessage(String chatID, String user1ID, String user2ID, MessagesArray messagesArray) {
        this.chatID = chatID;
        this.user1ID = user1ID;
        this.user2ID = user2ID;
        this.messagesArray = messagesArray;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getUser1ID() {
        return user1ID;
    }

    public void setUser1ID(String user1ID) {
        this.user1ID = user1ID;
    }

    public String getUser2ID() {
        return user2ID;
    }

    public void setUser2ID(String user2ID) {
        this.user2ID = user2ID;
    }

    public MessagesArray getMessagesArray() {
        return messagesArray;
    }

    public void setMessagesArray(MessagesArray messagesArray) {
        this.messagesArray = messagesArray;
    }

    public static class MessagesArray {
        private User1IDMessages user1IDMessages;
        private User2IDMessages user2IDMessages;

        public MessagesArray() {
        }

        public User1IDMessages getUser1IDMessages() {
            return user1IDMessages;
        }

        public void setUser1IDMessages(User1IDMessages user1IDMessages) {
            this.user1IDMessages = user1IDMessages;
        }

        public User2IDMessages getUser2IDMessages() {
            return user2IDMessages;
        }

        public void setUser2IDMessages(User2IDMessages user2IDMessages) {
            this.user2IDMessages = user2IDMessages;
        }
    }

    public static class User1IDMessages {
        private List<MessageDetails> message;

        public User1IDMessages() {
        }

        public List<MessageDetails> getMessage() {
            return message;
        }

        public void setMessage(List<MessageDetails> message) {
            this.message = message;
        }
    }

    public static class User2IDMessages {
        private List<MessageDetails> message;

        public User2IDMessages() {
        }

        public List<MessageDetails> getMessage() {
            return message;
        }

        public void setMessage(List<MessageDetails> message) {
            this.message = message;
        }
    }

    public static class MessageDetails {
        private String message;
        private Date date;
        private String senderID;

        public MessageDetails() {
        }

        public MessageDetails(String message, Date date,String senderID) {
            this.message = message;
            this.date = date;
            this.senderID=senderID;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getSenderID() {
            return senderID;
        }

        public void setSenderID(String senderID) {
            this.senderID = senderID;
        }
    }
}
