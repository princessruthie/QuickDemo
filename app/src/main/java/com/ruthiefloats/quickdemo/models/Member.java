package com.ruthiefloats.quickdemo.models;

/**
 * A model for parsing GitHub's orgs/{org}/members response
 */

public class Member {
    private String login;
    private String avatar_url;

    public String getLogin() {
        return login;
    }

    public String getAvatar_url() {
        return avatar_url;
    }
}
