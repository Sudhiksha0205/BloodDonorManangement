package com.redpulse.controller;

import com.redpulse.dao.UserDAO;
import com.redpulse.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UserDAO userDAO = new UserDAO();

        User user = userDAO.loginUser(email, password);

        if (user != null) {

            // Create session for the logged-in user
            HttpSession session = request.getSession();

            session.setAttribute("user", user);
            session.setAttribute("userId", user.getId());

            response.sendRedirect("dashboard.html");

        } else {

            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().println(
                "<script>" +
                "alert('Invalid email or password.');" +
                "window.location.href='login.html';" +
                "</script>"
            );
        }
    }
}