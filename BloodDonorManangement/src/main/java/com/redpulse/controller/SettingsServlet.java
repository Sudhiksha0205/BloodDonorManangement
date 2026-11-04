
package com.redpulse.controller;

import java.io.IOException;

import com.redpulse.dao.SettingsDAO;
import com.redpulse.model.User;
import com.redpulse.model.UserSettings;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/SettingsServlet")
public class SettingsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SettingsDAO settingsDAO;

    @Override
    public void init() {
        settingsDAO = new SettingsDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Integer userId = getUserId(request.getSession(false));

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(
                "{\"success\":false,\"message\":\"Not logged in\"}"
            );
            return;
        }

        UserSettings settings = settingsDAO.getSettings(userId);

        if (settings == null) {
            settingsDAO.createDefaultSettings(userId);
            settings = settingsDAO.getSettings(userId);
        }

        if (settings == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(
                "{\"success\":false,\"message\":\"Could not load settings\"}"
            );
            return;
        }

        response.getWriter().write(settingsToJson(settings));
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Integer userId = getUserId(request.getSession(false));

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(
                "{\"success\":false,\"message\":\"Not logged in\"}"
            );
            return;
        }

        UserSettings settings = new UserSettings();
        settings.setUserId(userId);

        settings.setEmailNotifications(getBoolean(request, "emailNotifications"));
        settings.setSmsNotifications(getBoolean(request, "smsNotifications"));
        settings.setBloodRequestAlerts(getBoolean(request, "bloodRequestAlerts"));
        settings.setEmergencyAlerts(getBoolean(request, "emergencyAlerts"));
        settings.setDonorAvailable(getBoolean(request, "donorAvailable"));
        settings.setProfileVisibility(getBoolean(request, "profileVisibility"));
        settings.setShowPhone(getBoolean(request, "showPhone"));
        settings.setShowEmail(getBoolean(request, "showEmail"));
        settings.setDarkMode(getBoolean(request, "darkMode"));
        settings.setLargeText(getBoolean(request, "largeText"));
        settings.setReducedMotion(getBoolean(request, "reducedMotion"));
        settings.setAutoLogout(getBoolean(request, "autoLogout"));
        settings.setRememberLogin(getBoolean(request, "rememberLogin"));

        String language = request.getParameter("language");
        settings.setLanguage("ta".equalsIgnoreCase(language)
                || "Tamil".equalsIgnoreCase(language) ? "ta" : "en");

        String timezone = request.getParameter("timezone");
        settings.setTimezone("UTC".equalsIgnoreCase(timezone)
                ? "UTC" : "Asia/Kolkata");

        if (settingsDAO.getSettings(userId) == null) {
            settingsDAO.createDefaultSettings(userId);
        }

        boolean success = settingsDAO.saveSettings(settings);

        if (success) {
            response.getWriter().write(
                "{\"success\":true,\"message\":\"Settings saved successfully\"}"
            );
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(
                "{\"success\":false,\"message\":\"Unable to save settings\"}"
            );
        }
    }

    private Integer getUserId(HttpSession session) {
        if (session == null) {
            return null;
        }

        Object user = session.getAttribute("user");

        if (!(user instanceof User)) {
            return null;
        }

        int id = ((User) user).getId();
        return id > 0 ? id : null;
    }

    private boolean getBoolean(HttpServletRequest request, String name) {
        return Boolean.parseBoolean(request.getParameter(name));
    }

    private String settingsToJson(UserSettings s) {
        return "{"
            + "\"success\":true,"
            + "\"userId\":" + s.getUserId() + ","
            + "\"emailNotifications\":" + s.isEmailNotifications() + ","
            + "\"smsNotifications\":" + s.isSmsNotifications() + ","
            + "\"bloodRequestAlerts\":" + s.isBloodRequestAlerts() + ","
            + "\"emergencyAlerts\":" + s.isEmergencyAlerts() + ","
            + "\"donorAvailable\":" + s.isDonorAvailable() + ","
            + "\"profileVisibility\":" + s.isProfileVisibility() + ","
            + "\"showPhone\":" + s.isShowPhone() + ","
            + "\"showEmail\":" + s.isShowEmail() + ","
            + "\"darkMode\":" + s.isDarkMode() + ","
            + "\"largeText\":" + s.isLargeText() + ","
            + "\"reducedMotion\":" + s.isReducedMotion() + ","
            + "\"autoLogout\":" + s.isAutoLogout() + ","
            + "\"rememberLogin\":" + s.isRememberLogin() + ","
            + "\"language\":\"" + s.getLanguage() + "\","
            + "\"timezone\":\"" + s.getTimezone() + "\""
            + "}";
    }
}
