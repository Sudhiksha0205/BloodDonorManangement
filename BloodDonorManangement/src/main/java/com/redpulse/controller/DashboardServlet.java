package com.redpulse.controller;

import com.redpulse.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // User is not logged in
        if (session == null || session.getAttribute("user") == null) {

            response.sendRedirect("login.html");
            return;
        }

        User user = (User) session.getAttribute("user");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String json =
                "{"
                + "\"id\":" + user.getId() + ","
                + "\"fullName\":\"" + escape(user.getFullName()) + "\","
                + "\"email\":\"" + escape(user.getEmail()) + "\","
                + "\"phone\":\"" + escape(user.getPhone()) + "\","
                + "\"bloodGroup\":\"" + escape(user.getBloodGroup()) + "\""
                + "}";

        response.getWriter().write(json);
    }

    private String escape(String value) {

        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"");
    }
}