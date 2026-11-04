package com.redpulse.controller;

import com.redpulse.dao.DonationDAO;
import com.redpulse.model.Donation;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/DonationServlet")
public class DonationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        System.out.println("=================================");
        System.out.println("DonationServlet called");
        System.out.println("=================================");


        /* =========================================
           CHECK LOGIN
           ========================================= */

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("user") == null) {

            System.out.println("User is NOT logged in.");

            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().println(
                "<script>" +
                "alert('Please login before confirming a donation.');" +
                "window.location.href='login.html';" +
                "</script>"
            );

            return;
        }


        /* =========================================
           GET USER ID
           ========================================= */

        Integer userId =
            (Integer) session.getAttribute("userId");

        if (userId == null) {

            System.out.println(
                "User ID not found in session."
            );

            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().println(
                "<script>" +
                "alert('User session expired. Please login again.');" +
                "window.location.href='login.html';" +
                "</script>"
            );

            return;
        }


        /* =========================================
           GET FORM DATA
           ========================================= */

        String hospitalName =
            request.getParameter("hospitalName");

        String donationDate =
            request.getParameter("donationDate");

        String hospitalAddress =
            request.getParameter("hospitalAddress");

        String donationTime =
            request.getParameter("donationTime");

        String bloodGroup =
            request.getParameter("bloodGroup");


        System.out.println("User ID: " + userId);
        System.out.println("Hospital: " + hospitalName);
        System.out.println("Date: " + donationDate);
        System.out.println("Address: " + hospitalAddress);
        System.out.println("Time: " + donationTime);
        System.out.println("Blood Group: " + bloodGroup);


        /* =========================================
           VALIDATION
           ========================================= */

        if (hospitalName == null ||
            hospitalName.trim().isEmpty() ||

            donationDate == null ||
            donationDate.trim().isEmpty() ||

            hospitalAddress == null ||
            hospitalAddress.trim().isEmpty() ||

            donationTime == null ||
            donationTime.trim().isEmpty() ||

            bloodGroup == null ||
            bloodGroup.trim().isEmpty()) {

            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().println(
                "<script>" +
                "alert('Please fill in all donation details.');" +
                "window.history.back();" +
                "</script>"
            );

            return;
        }


        /* =========================================
           CREATE DONATION OBJECT
           ========================================= */

        Donation donation = new Donation();

        donation.setUserId(userId);

        donation.setHospitalName(
            hospitalName.trim()
        );

        donation.setDonationDate(
            donationDate
        );

        donation.setHospitalAddress(
            hospitalAddress.trim()
        );

        donation.setDonationTime(
            donationTime
        );

        donation.setBloodGroup(
            bloodGroup
        );


        /* =========================================
           SAVE TO DATABASE
           ========================================= */

        DonationDAO donationDAO =
            new DonationDAO();

        boolean success =
            donationDAO.addDonation(donation);


        /* =========================================
           SUCCESS
           ========================================= */

        if (success) {

            System.out.println(
                "DONATION SAVED SUCCESSFULLY."
            );

            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().println(
                "<script>" +
                "alert('Donation confirmed successfully! ❤️ Thank you for saving a life.');" +
                "window.location.href='donate.html';" +
                "</script>"
            );

        }


        /* =========================================
           FAILURE
           ========================================= */

        else {

            System.out.println(
                "DONATION COULD NOT BE SAVED."
            );

            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().println(
                "<script>" +
                "alert('Donation could not be saved. Please check your database connection.');" +
                "window.history.back();" +
                "</script>"
            );
        }

    }
}