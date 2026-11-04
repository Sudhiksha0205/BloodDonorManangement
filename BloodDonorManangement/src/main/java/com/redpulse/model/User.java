package com.redpulse.model;

public class User {

    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String bloodGroup;
    private String password;
    private String dob;
    private String gender;
    private String state;
    private String city;
    private String landmark;
    private String medicalCondition;
    private String lastDonationDate;

    public User() {
    }

    public User(String fullName, String email, String phone,
                String bloodGroup, String password, String dob,
                String gender, String state, String city,
                String landmark, String medicalCondition,
                String lastDonationDate) {

        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.bloodGroup = bloodGroup;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
        this.state = state;
        this.city = city;
        this.landmark = landmark;
        this.medicalCondition = medicalCondition;
        this.lastDonationDate = lastDonationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getMedicalCondition() {
        return medicalCondition;
    }

    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = medicalCondition;
    }

    public String getLastDonationDate() {
        return lastDonationDate;
    }

    public void setLastDonationDate(String lastDonationDate) {
        this.lastDonationDate = lastDonationDate;
    }
}