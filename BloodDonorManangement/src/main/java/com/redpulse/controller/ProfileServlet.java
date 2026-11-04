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

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // ==================================================
    // GET PROFILE
    // ==================================================

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // User is not logged in
        if (session == null ||
            session.getAttribute("user") == null) {

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
                + "\"bloodGroup\":\"" + escape(user.getBloodGroup()) + "\","
                + "\"dob\":\"" + escape(user.getDob()) + "\","
                + "\"gender\":\"" + escape(user.getGender()) + "\","
                + "\"state\":\"" + escape(user.getState()) + "\","
                + "\"city\":\"" + escape(user.getCity()) + "\","
                + "\"landmark\":\"" + escape(user.getLandmark()) + "\","
                + "\"medicalCondition\":\""
                + escape(user.getMedicalCondition()) + "\","
                + "\"lastDonationDate\":\""
                + escape(user.getLastDonationDate()) + "\""
                + "}";

        response.getWriter().write(json);
    }


    // ==================================================
    // UPDATE PROFILE
    // ==================================================

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        // User is not logged in
        if (session == null ||
            session.getAttribute("user") == null) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.getWriter().write(
                    "Session expired. Please login again."
            );

            return;
        }

        User sessionUser =
                (User) session.getAttribute("user");

        int userId = sessionUser.getId();


        // Get updated values

        String fullName =
                request.getParameter("fullName");

        String email =
                request.getParameter("email");

        String phone =
                request.getParameter("phone");

        String dob =
                request.getParameter("dob");

        String gender =
                request.getParameter("gender");

        String bloodGroup =
                request.getParameter("bloodGroup");

        String lastDonationDate =
                request.getParameter("lastDonationDate");

        String state =
                request.getParameter("state");

        String city =
                request.getParameter("city");

        String landmark =
                request.getParameter("landmark");

        String medicalCondition =
                request.getParameter("medicalCondition");


        // Update the User object

        sessionUser.setFullName(fullName);
        sessionUser.setEmail(email);
        sessionUser.setPhone(phone);
        sessionUser.setDob(dob);
        sessionUser.setGender(gender);
        sessionUser.setBloodGroup(bloodGroup);
        sessionUser.setLastDonationDate(lastDonationDate);
        sessionUser.setState(state);
        sessionUser.setCity(city);
        sessionUser.setLandmark(landmark);
        sessionUser.setMedicalCondition(medicalCondition);


        // Update database

        UserDAO userDAO = new UserDAO();

        boolean updated =
                userDAO.updateUser(sessionUser);


        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");


        if (updated) {

            // Update session with latest user information
            session.setAttribute("user", sessionUser);

            response.getWriter().write("SUCCESS");

        } else {

            response.getWriter().write(
                    "Could not update profile in database."
            );
        }
    }


    // ==================================================
    // JSON ESCAPE
    // ==================================================

    private String escape(String value) {

        if (value == null) {
            return "";
        }

        return value.replace("\\", "\\\\")
                    .replace("\"", "\\\"");
    }
}