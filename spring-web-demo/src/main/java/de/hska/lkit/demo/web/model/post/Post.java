package de.hska.lkit.demo.web.model.post;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by patrickkoenig on 01.06.16.
 */
public class Post implements Serializable {

    private int id;
    private String content;
    private String userName;
    private String profilePicturePath;
    private Date date;

    public Post() {
        content = new String();
        userName = new String();
        date = new Date();
    }


    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId(String id) {
        if (id != null) {
            this.id = Integer.parseInt(id);
        }
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


