package com.redpulse.model;

public class BloodRequest {

    private int id;
    private int userId;

    private String patientName;
    private String bloodGroup;
    private int units;

    private String hospitalName;
    private String hospitalAddress;
    private String city;

    private String requiredDate;
    private String priority;
    private String contactNumber;

    public BloodRequest() {
    }

    public BloodRequest(
            int userId,
            String patientName,
            String bloodGroup,
            int units,
            String hospitalName,
            String hospitalAddress,
            String city,
            String requiredDate,
            String priority,
            String contactNumber) {

        this.userId = userId;
        this.patientName = patientName;
        this.bloodGroup = bloodGroup;
        this.units = units;
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.city = city;
        this.requiredDate = requiredDate;
        this.priority = priority;
        this.contactNumber = contactNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}