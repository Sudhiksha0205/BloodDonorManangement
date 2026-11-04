package com.redpulse.model;

public class UserSettings {

    private int userId;
    
    private String language = "en";
    private String timezone = "Asia/Kolkata";

    private boolean emailNotifications;
    private boolean smsNotifications;

    private boolean bloodRequestAlerts;
    private boolean emergencyAlerts;

    private boolean donorAvailable;

    private boolean profileVisibility;
    private boolean showPhone;
    private boolean showEmail;

    private boolean darkMode;
    private boolean largeText;
    private boolean reducedMotion;

    private boolean autoLogout;
    private boolean rememberLogin;

    public UserSettings() {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public boolean isSmsNotifications() {
        return smsNotifications;
    }

    public void setSmsNotifications(boolean smsNotifications) {
        this.smsNotifications = smsNotifications;
    }

    public boolean isBloodRequestAlerts() {
        return bloodRequestAlerts;
    }

    public void setBloodRequestAlerts(boolean bloodRequestAlerts) {
        this.bloodRequestAlerts = bloodRequestAlerts;
    }

    public boolean isEmergencyAlerts() {
        return emergencyAlerts;
    }

    public void setEmergencyAlerts(boolean emergencyAlerts) {
        this.emergencyAlerts = emergencyAlerts;
    }

    public boolean isDonorAvailable() {
        return donorAvailable;
    }

    public void setDonorAvailable(boolean donorAvailable) {
        this.donorAvailable = donorAvailable;
    }

    public boolean isProfileVisibility() {
        return profileVisibility;
    }

    public void setProfileVisibility(boolean profileVisibility) {
        this.profileVisibility = profileVisibility;
    }

    public boolean isShowPhone() {
        return showPhone;
    }

    public void setShowPhone(boolean showPhone) {
        this.showPhone = showPhone;
    }

    public boolean isShowEmail() {
        return showEmail;
    }

    public void setShowEmail(boolean showEmail) {
        this.showEmail = showEmail;
    }

    public boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
    }

    public boolean isLargeText() {
        return largeText;
    }

    public void setLargeText(boolean largeText) {
        this.largeText = largeText;
    }

    public boolean isReducedMotion() {
        return reducedMotion;
    }

    public void setReducedMotion(boolean reducedMotion) {
        this.reducedMotion = reducedMotion;
    }

    public boolean isAutoLogout() {
        return autoLogout;
    }

    public void setAutoLogout(boolean autoLogout) {
        this.autoLogout = autoLogout;
    }

    public boolean isRememberLogin() {
        return rememberLogin;
    }

    public void setRememberLogin(boolean rememberLogin) {
        this.rememberLogin = rememberLogin;
    }
    
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}