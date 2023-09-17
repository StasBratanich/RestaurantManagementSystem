package com.hit.model;

public class UserObject {

    private String username;
    private String password;
    private String question;
    private String answer;

    public UserObject(String username, String password, String answer, String question) {
        this.username = username;
        this.password = password;
        this.question = question;
        this.answer = answer;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {return password;}
    public String getQuestion() {return question;}
    public String getAnswer() {return answer;}
    public void setPassword(String password) {this.password = password;}

}