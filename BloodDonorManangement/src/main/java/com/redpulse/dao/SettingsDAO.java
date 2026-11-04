
package com.redpulse.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.redpulse.model.UserSettings;
import com.redpulse.util.DBConnection;

public class SettingsDAO {

    public UserSettings getSettings(int userId) {
        String sql = "SELECT * FROM user_settings WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                UserSettings s = new UserSettings();
                s.setUserId(rs.getInt("user_id"));
                s.setEmailNotifications(rs.getBoolean("email_notifications"));
                s.setSmsNotifications(rs.getBoolean("sms_notifications"));
                s.setBloodRequestAlerts(rs.getBoolean("blood_request_alerts"));
                s.setEmergencyAlerts(rs.getBoolean("emergency_alerts"));
                s.setDonorAvailable(rs.getBoolean("donor_available"));
                s.setProfileVisibility(rs.getBoolean("profile_visibility"));
                s.setShowPhone(rs.getBoolean("show_phone"));
                s.setShowEmail(rs.getBoolean("show_email"));
                s.setDarkMode(rs.getBoolean("dark_mode"));
                s.setLargeText(rs.getBoolean("large_text"));
                s.setReducedMotion(rs.getBoolean("reduced_motion"));
                s.setAutoLogout(rs.getBoolean("auto_logout"));
                s.setRememberLogin(rs.getBoolean("remember_login"));

                String language = rs.getString("language");
                s.setLanguage(language == null ? "en" : language);

                String timezone = rs.getString("timezone");
                s.setTimezone(timezone == null
                        ? "Asia/Kolkata" : timezone);

                return s;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean createDefaultSettings(int userId) {
        String sql = """
            INSERT INTO user_settings (
                user_id,
                email_notifications,
                sms_notifications,
                blood_request_alerts,
                emergency_alerts,
                donor_available,
                profile_visibility,
                show_phone,
                show_email,
                dark_mode,
                large_text,
                reduced_motion,
                auto_logout,
                remember_login,
                language,
                timezone
            )
            VALUES (?, TRUE, FALSE, TRUE, TRUE, TRUE, TRUE, FALSE, FALSE,
                    FALSE, FALSE, FALSE, FALSE, TRUE, 'en', 'Asia/Kolkata')
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveSettings(UserSettings s) {
        String sql = """
            UPDATE user_settings SET
                email_notifications = ?,
                sms_notifications = ?,
                blood_request_alerts = ?,
                emergency_alerts = ?,
                donor_available = ?,
                profile_visibility = ?,
                show_phone = ?,
                show_email = ?,
                dark_mode = ?,
                large_text = ?,
                reduced_motion = ?,
                auto_logout = ?,
                remember_login = ?,
                language = ?,
                timezone = ?
            WHERE user_id = ?
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setBoolean(1, s.isEmailNotifications());
            ps.setBoolean(2, s.isSmsNotifications());
            ps.setBoolean(3, s.isBloodRequestAlerts());
            ps.setBoolean(4, s.isEmergencyAlerts());
            ps.setBoolean(5, s.isDonorAvailable());
            ps.setBoolean(6, s.isProfileVisibility());
            ps.setBoolean(7, s.isShowPhone());
            ps.setBoolean(8, s.isShowEmail());
            ps.setBoolean(9, s.isDarkMode());
            ps.setBoolean(10, s.isLargeText());
            ps.setBoolean(11, s.isReducedMotion());
            ps.setBoolean(12, s.isAutoLogout());
            ps.setBoolean(13, s.isRememberLogin());
            ps.setString(14, s.getLanguage());
            ps.setString(15, s.getTimezone());
            ps.setInt(16, s.getUserId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
