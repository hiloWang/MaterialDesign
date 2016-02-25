package com.hilo.bean;

/**
 * Created by hilo on 16/2/25.
 */
public class User {
    private String userName;
    private String passWord;
    private int age;

    public User(String userName, String passWord, int age) {
        this.userName = userName;
        this.passWord = passWord;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
