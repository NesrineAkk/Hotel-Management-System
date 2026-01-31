package model.services;

import data.DataManager;
import model.message.Observer;
import model.message.Subject;
import model.message.MessageModel;

import java.util.ArrayList;

public class MessageService implements Subject {

    private ArrayList<Observer> observers = new ArrayList<>();
    private ArrayList<MessageModel> messages = new ArrayList<>();
    private DataManager manager = new DataManager();

    public MessageService() {
        this.messages = (ArrayList<MessageModel>) manager.loadMessages();
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
        this.notifyObservers();
    }

    @Override
    public void removeObserver(Observer o) {
        int i = observers.indexOf(o);
        if (i >= 0) {
            observers.remove(i);
        }
    }

    public void notifyObservers() {
        for (int i = 0; i < observers.size(); i++) {
            Observer observer = (Observer) observers.get(i);
            observer.update(messages);
        }
    }

    public void listChanged() {
        notifyObservers();
    }

    public void updateList(MessageModel message) {
        messages.add(message);
        manager.saveMessages(messages);
        listChanged();
    }

    public int messagesCount() {
        return messages.size();
    }

    public ArrayList<MessageModel> getAllMessages() {
        return messages;
    }
}