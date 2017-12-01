package model;

public class Message {
    private Integer targetid;
    private User owner;
    private String message;
    private User target;
    private boolean isPrivate;

    public Message(User owner, String message, User target, Boolean isPrivate) {
        this.owner = owner;
        this.targetid = target.getId();
        this.message = message;
        this.target = target;
        this.isPrivate = isPrivate;
    }

    public Message(User owner, String message, User target) {
        this.owner = owner;
        this.targetid = target.getId();
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

    public Message() {
        this.owner = new User("Annonymous");
        this.message = "";
        this.isPrivate = false;
        this.target = new User("Name");
    }

    public User getOwner() {
        return owner;
    }

    public String getMessage() {
        return message;
    }

    public Integer getTargetid(){
        return  targetid;
    }

    public void setTargetid(int id){
        this.targetid = id;
    }

    public void setTarget(User user){
        this.target = user;
    }

    public User getTarget() {
        return target;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (isPrivate != message1.isPrivate) return false;
        if (!owner.equals(message1.owner)) return false;
        if (!message.equals(message1.message)) return false;
        return target.equals(message1.target);
    }

    @Override
    public int hashCode() {
        int result = owner.hashCode();
        result = 31 * result + message.hashCode();
        result = 31 * result + target.hashCode();
        result = 31 * result + (isPrivate ? 1 : 0);
        return result;
    }
}
