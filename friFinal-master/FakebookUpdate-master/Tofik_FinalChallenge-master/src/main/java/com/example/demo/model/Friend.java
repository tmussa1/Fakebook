package com.example.demo.model;

import com.example.demo.model.auth.AppUser;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Friend{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String fullName;
    private String picture;

    @OneToMany(mappedBy = "whoPosted")
    private Set<UserPost> post;

    @ManyToOne
    Profile profile;

    public Friend() {
        this.post = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<UserPost> getPost() {
        return post;
    }

    public void setPost(Set<UserPost> post) {
        this.post = post;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
