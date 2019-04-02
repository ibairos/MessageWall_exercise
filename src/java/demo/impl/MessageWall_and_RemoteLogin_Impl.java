package demo.impl;

import demo.spec.Message;
import demo.spec.MessageWall;
import demo.spec.RemoteLogin;
import demo.spec.UserAccess;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageWall_and_RemoteLogin_Impl implements RemoteLogin, MessageWall {

    private List<Message> messages;
    private HashMap<String, String> auth;
    
    public MessageWall_and_RemoteLogin_Impl() {
        messages = new ArrayList<Message>();
        initHashMap();
    }

    @Override
    public UserAccess_Impl connect(String usr, String passwd) {
        if (loginCorrect(usr, passwd)) {
            return new UserAccess_Impl(this, usr);         
        } else {
           return null;      
        }
    }

    @Override
    public void put(String user, String msg) {
        messages.add(new Message_Impl(user, msg));
    }

    @Override
    public boolean delete(String user, int index) {
        if (messages.size() > index) {
            messages.remove(index);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Message getLast() {
        if (!messages.isEmpty()) {
            return messages.get(messages.size() - 1);
        } else {
            return new Message_Impl("No user", "No message");
        }
    }

    @Override
    public int getNumber() {
        return messages.size();
    }

    @Override
    public List<Message> getAllMessages() {
        return messages;
    }
    
    private void initHashMap() {
        auth = new HashMap<String, String>();
        auth.put("ecomm", "ecomm");
        auth.put("John", "John11");
        auth.put("Molly", "Molly22");
        auth.put("user", "password");
    }
    
    private boolean loginCorrect(String user, String password) {
        if (auth.get(user) != null && auth.get(user).equals(password)) {
            return true;
        } else {
            return false;        
        }
    }

    
}







