package com.redpulse.controller;

import com.redpulse.dao.UserDAO;
import com.redpulse.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String bloodGroup = request.getParameter("bloodGroup");
        String password = request.getParameter("password");
        String dob = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String state = request.getParameter("state");
        String city = request.getParameter("city");
        String landmark = request.getParameter("landmark");
        String medicalCondition = request.getParameter("medicalCondition");
        String lastDonationDate = request.getParameter("lastDonationDate");

        User user = new User();

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setBloodGroup(bloodGroup);
        user.setPassword(password);
        user.setDob(dob);
        user.setGender(gender);
        user.setState(state);
        user.setCity(city);
        user.setLandmark(landmark);
        user.setMedicalCondition(medicalCondition);
        user.setLastDonationDate(lastDonationDate);

        UserDAO userDAO = new UserDAO();

        boolean success = userDAO.registerUser(user);

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        if (success) {

            response.getWriter().println(
                "<script>" +
                "alert('Registration successful!');" +
                "window.location.href='login.html';" +
                "</script>"
            );

        } else {

            response.getWriter().println(
                "<script>" +
                "alert('Registration failed. Please check the details or use another email.');" +
                "window.location.href='register.html';" +
                "</script>"
            );
        }
    }
}