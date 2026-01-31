package model.message;

public class MessageModel{
    private String id;
    private String username;
    private String title;
    private String content;
    private String isOpened;
    private String date;
    private String messageType;
    private String senderId;



    public MessageModel(String id, String userName, String title, String content, String date, String messageType, String senderId) {
        this.id = id;
        this.username = userName; // both senderid or username throw an exception if they are empty or null
        this.title = title; // throws an exception if the title/topic is too long or too short
        this.content = content; // throws an exception if the content is too long or too short
        this.date = date;
        this.messageType = messageType; // throws an exception if message type doesnt follow the enum
        this.senderId = senderId;
    }



    public String getId() {
        return id;
    }

    public String getUserName() {
        return username;
    }


    public String getTitle() {
        return title;
    }


    public String getContent() {
        return content;
    }

    public String isOpened() {
        return isOpened;
    }

    public void setOpened(String opened) {
        isOpened = opened;
    }

    public String getDate() {
        return date;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getSenderId() {
        return senderId;
    }

    public String toString(){
        return "Sender: " + username + "\nType: " + messageType + "\nTitle: " + title + "\nContent: " + content + "\nDate: " + date;
    }

}
