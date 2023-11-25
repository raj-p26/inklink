package com.example.inklink.models;

public class User {
    private int id;
    private String firstName, lastName, username, email, password, about, accountStatus, registrationDate;

    public User() {}

    public User(String firstName, String lastName, String email, String password, String about) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = firstName;
        this.email = email;
        this.password = password;
        this.about = about;
    }

    public User(String firstName, String lastName, String username, String email, String password, String about) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.about = about;
    }

    public User(int id, String firstName, String lastName, String username, String email, String password, String about) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.about = about;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getAbout() { return about; }

    public void setAbout(String about) { this.about = about; }

    public String getAccountStatus() { return accountStatus; }

    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus; }

    public String getRegistrationDate() { return registrationDate; }

    public void setRegistrationDate(String registrationDate) { this.registrationDate = registrationDate; }
}
