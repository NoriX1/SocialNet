package model;

public class Message {
    private User owner;
    private String message;
    private User target;
    private boolean isPrivate;

    public Message(User owner, String message, User target, Boolean isPrivate) {
        this.owner = owner;
        this.message = message;
        this.target = target;
        this.isPrivate = isPrivate;
    }

    public Message(User owner, String message, User target) {
        this.owner = owner;
        this.message = message;
        this.target = target;
        this.isPrivate = true;
    }

    public Message(User owner, String message) {
        this.owner = owner;
        this.message = message;
        this.isPrivate = false;
    }

    public Message(String message) {
        this.owner = new User("Annonymous");
        this.message = message;
        this.isPrivate = false;
    }

    public User getOwner() {
        return owner;
    }

    public String getMessage() {
        return message;
    }

    public User getTarget() {
        return target;
    }

    public boolean isPrivate() {
        return isPrivate;
    }
}
