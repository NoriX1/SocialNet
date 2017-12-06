package model;

import javax.persistence.*;

@Entity
@Table(name = "messages")
public class Message {
    @Transient
    private User target;
    @Transient
    private User owner;
    @Column
    @Id
    private Integer id;
    @Column(name = "owner")
    private Integer ownerid;
    @Column
    private String message;
    @Column(name = "target")
    private Integer targetid;
    @Column
    private boolean isPrivate;

    public Message() {
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTargetid() {
        return targetid;
    }

    public void setTargetid(Integer targetid) {
        this.targetid = targetid;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Integer getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(Integer ownerid) {
        this.ownerid = ownerid;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Message(User owner, String message, User target, boolean isPrivate) {
        this.owner = owner;
        this.message = message;
        this.target = target;
        this.isPrivate = isPrivate;
    }

    public Message(User owner, String message) {
        this.owner = owner;
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message1 = (Message) o;

        if (isPrivate != message1.isPrivate) return false;
        if (id != null ? !id.equals(message1.id) : message1.id != null) return false;
        if (ownerid != null ? !ownerid.equals(message1.ownerid) : message1.ownerid != null) return false;
        if (message != null ? !message.equals(message1.message) : message1.message != null) return false;
        if (targetid != null ? !targetid.equals(message1.targetid) : message1.targetid != null) return false;
        if (target != null ? !target.equals(message1.target) : message1.target != null) return false;
        return owner != null ? owner.equals(message1.owner) : message1.owner == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (ownerid != null ? ownerid.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (targetid != null ? targetid.hashCode() : 0);
        result = 31 * result + (isPrivate ? 1 : 0);
        result = 31 * result + (target != null ? target.hashCode() : 0);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }
}
