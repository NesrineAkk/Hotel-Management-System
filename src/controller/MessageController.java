package controller;

import model.message.MessageModel;
import model.services.MessageService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

public class MessageController {

    private MessageService messageService;

    public MessageController() {
        this.messageService = new MessageService();
    }

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    public boolean sendMessageToAdmin(String senderId, String username, String title, String content) {
        try {
            if (senderId == null || senderId.trim().isEmpty()) {
                throw new IllegalArgumentException("Sender ID cannot be empty");
            }

            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Username cannot be empty");
            }

            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty");
            }

            if (title.length() > 100) {
                throw new IllegalArgumentException("Title is too long (max 100 characters)");
            }

            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("Content cannot be empty");
            }

            if (content.length() > 1000) {
                throw new IllegalArgumentException("Content is too long (max 1000 characters)");
            }

            // Generate unique ID
            String messageId = UUID.randomUUID().toString();

            // Get current date and time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String date = LocalDateTime.now().format(formatter);

            // Create message model
            MessageModel message = new MessageModel(
                    messageId,
                    username,
                    title,
                    content,
                    date,
                    "USER_TO_ADMIN",
                    senderId
            );

            // Save message
            messageService.updateList(message);

            return true;

        } catch (IllegalArgumentException e) {
            System.err.println("Validation error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void send(String messageId, String username, String title, String content,
                     String date, String messageType, String senderId) {
        try {
            MessageModel message = new MessageModel(
                    messageId,
                    username,
                    title,
                    content,
                    date,
                    messageType,
                    senderId
            );

            messageService.updateList(message);

        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public boolean sendAdminReply(String recipientUserId, String recipientUsername,
                                  String title, String content) {
        try {
            // Validate inputs
            if (recipientUserId == null || recipientUserId.trim().isEmpty()) {
                throw new IllegalArgumentException("Recipient user ID cannot be empty");
            }

            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty");
            }

            if (content == null || content.trim().isEmpty()) {
                throw new IllegalArgumentException("Content cannot be empty");
            }

            // Generate unique ID
            String messageId = UUID.randomUUID().toString();

            // Get current date and time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String date = LocalDateTime.now().format(formatter);

            // Create admin reply message
            MessageModel message = new MessageModel(
                    messageId,
                    "Admin",  // Admin username
                    title,
                    content,
                    date,
                    "ADMIN_TO_USER",
                    recipientUserId  // Store which user this reply is for
            );

            // Save message
            messageService.updateList(message);

            return true;

        } catch (Exception e) {
            System.err.println("Error sending admin reply: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<MessageModel> getUserMessages(String userId) {
        ArrayList<MessageModel> allMessages = messageService.getAllMessages();
        ArrayList<MessageModel> userMessages = new ArrayList<>();

        for (MessageModel message : allMessages) {
            if (message.getSenderId().equals(userId)) {
                userMessages.add(message);
            }
        }

        return userMessages;
    }

    public ArrayList<MessageModel> getMessages() {
        return messageService.getAllMessages();
    }

    public int getMessageCount() {
        return messageService.messagesCount();
    }

    public int getUnreadMessagesCount() {
        ArrayList<MessageModel> allMessages = messageService.getAllMessages();
        int count = 0;

        for (MessageModel message : allMessages) {
            if (message.isOpened() == null || !message.isOpened().equals("true")) {
                count++;
            }
        }

        return count;
    }

    public void markAsRead(String messageId) {
        ArrayList<MessageModel> allMessages = messageService.getAllMessages();

        for (MessageModel message : allMessages) {
            if (message.getId().equals(messageId)) {
                message.setOpened("true");
                break;
            }
        }
    }

    public ArrayList<MessageModel> getConversation(String userId) {
        ArrayList<MessageModel> allMessages = messageService.getAllMessages();
        ArrayList<MessageModel> conversation = new ArrayList<>();

        for (MessageModel message : allMessages) {

            if (message.getSenderId().equals(userId)) {
                conversation.add(message);
            }
        }

        conversation.sort((m1, m2) -> {
            try {
                return m1.getDate().compareTo(m2.getDate());
            } catch (Exception e) {
                return 0;
            }
        });

        return conversation;
    }
}