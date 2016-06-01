package de.hska.lkit.demo.web;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by patrickkoenig on 01.06.16.
 */
public class Post implements Serializable {

    private int id;
    private String content;
    private String userName;
    private Date date;

    public Post() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        return id + 37 + content.hashCode() + userName.hashCode() + date.hashCode();
    }

    @Override
    public boolean equals(Object otherPost) {
        if (!(otherPost instanceof Post)) {
            return false;
        }
        Post other = (Post) otherPost;

        return (other.getId() == this.id && other.getContent().equals(this.content)
                && other.getDate() == this.date && other.getUserName().equals(this.userName));


    }

}


