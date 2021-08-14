package com.khedr.ecommerce.pojo.user;

public class UserDataForLoginRequest {

    String password;
    String name;
    String phone;

    String email;
    int id;
    int points;
    double credit;
    String image;
    String token;


    //for login request
    public UserDataForLoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }


    //for login response
    public UserDataForLoginRequest(int id, String name, String email, String phone, String image
            , int points, int credit, String token) {

        this.name = name;
        this.phone = phone;
        this.email = email;
        this.id = id;
        this.points = points;
        this.credit = credit;
        this.image = image;
        this.token = token;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
