package com.redpulse.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.redpulse.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null ||
            session.getAttribute("user") == null) {

            response.sendRedirect("login.html");
            return;
        }

        int userId =
                ((com.redpulse.model.User)
                        session.getAttribute("user"))
                        .getId();

        String currentPassword =
                request.getParameter("currentPassword");

        String newPassword =
                request.getParameter("newPassword");

        String confirmPassword =
                request.getParameter("confirmPassword");


        if (currentPassword == null ||
            newPassword == null ||
            confirmPassword == null ||
            currentPassword.isEmpty() ||
            newPassword.isEmpty() ||
            confirmPassword.isEmpty()) {

            response.sendRedirect(
                "settings.html?password=empty"
            );

            return;
        }


        if (!newPassword.equals(confirmPassword)) {

            response.sendRedirect(
                "settings.html?password=mismatch"
            );

            return;
        }


        try (Connection con =
                DBConnection.getConnection()) {


            // Check current password
            String checkSql =
                    "SELECT password FROM users WHERE id = ?";

            try (PreparedStatement ps =
                    con.prepareStatement(checkSql)) {

                ps.setInt(1, userId);

                ResultSet rs =
                        ps.executeQuery();

                if (!rs.next()) {

                    response.sendRedirect(
                        "settings.html?password=failed"
                    );

                    return;
                }

                String oldPassword =
                        rs.getString("password");

                if (!oldPassword.equals(currentPassword)) {

                    response.sendRedirect(
                        "settings.html?password=wrong"
                    );

                    return;
                }
            }


            // Update password
            String updateSql =
                    "UPDATE users SET password = ? WHERE id = ?";

            try (PreparedStatement ps =
                    con.prepareStatement(updateSql)) {

                ps.setString(1, newPassword);
                ps.setInt(2, userId);

                int rows =
                        ps.executeUpdate();

                if (rows > 0) {

                    response.sendRedirect(
                        "settings.html?password=success"
                    );

                } else {

                    response.sendRedirect(
                        "settings.html?password=failed"
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                "settings.html?password=error"
            );
        }
    }
}
