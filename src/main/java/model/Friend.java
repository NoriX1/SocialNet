package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "friendlist")
public class Friend {
    @Column
    @Id
    Integer id;
    @Column
    Integer who;
    @Column
    Integer whom;

    public Friend() {
    }

    public Friend(Integer id, Integer who, Integer whom) {
        this.id = id;
        this.who = who;
        this.whom = whom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getWho() {
        return who;
    }

    public void setWho(Integer who) {
        this.who = who;
    }

    public Integer getWhom() {
        return whom;
    }

    public void setWhom(Integer whom) {
        this.whom = whom;
    }
}
