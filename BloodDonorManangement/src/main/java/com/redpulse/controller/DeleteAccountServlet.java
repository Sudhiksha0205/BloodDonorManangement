
package com.redpulse.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.redpulse.model.User;
import com.redpulse.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/DeleteAccountServlet")
public class DeleteAccountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.html");
            return;
        }

        User user = (User) session.getAttribute("user");
        int userId = user.getId();

        String confirmation = request.getParameter("confirmDelete");

        if (!"YES".equals(confirmation)) {
            response.sendRedirect("settings.html?account=cancelled");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                // Delete donation records belonging to this user.
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM donations WHERE user_id = ?")) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                }

                // Delete blood requests created by this user.
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM blood_requests WHERE requester_id = ?")) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                }

                // user_settings is deleted automatically by ON DELETE CASCADE.
                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM users WHERE id = ?")) {
                    ps.setInt(1, userId);

                    int rows = ps.executeUpdate();

                    if (rows != 1) {
                        throw new Exception("Account could not be deleted.");
                    }
                }

                con.commit();

            } catch (Exception e) {
                con.rollback();
                throw e;
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("settings.html?account=error");
            return;
        }

        // Invalidate the session only after the database transaction succeeds.
        session.invalidate();
        response.sendRedirect("login.html?account=deleted");
    }
}
