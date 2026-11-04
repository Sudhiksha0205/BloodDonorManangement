package com.redpulse.controller;

import com.redpulse.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/FindDonorServlet")
public class FindDonorServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        // Only logged-in users can search
        if (session == null ||
            session.getAttribute("user") == null) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.setContentType("application/json");

            response.getWriter().write(
                    "{\"error\":\"Please login first.\"}"
            );

            return;
        }

        String bloodGroup =
                request.getParameter("bloodGroup");

        String city =
                request.getParameter("city");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        String sql =
                "SELECT id, fullname, email, phone, "
                + "bloodgroup, city, state "
                + "FROM users "
                + "WHERE bloodgroup = ? "
                + "AND city = ?";

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, bloodGroup);
            statement.setString(2, city);

            ResultSet resultSet =
                    statement.executeQuery();

            StringBuilder json =
                    new StringBuilder();

            json.append("[");

            boolean first = true;

            while (resultSet.next()) {

                if (!first) {
                    json.append(",");
                }

                json.append("{");

                json.append("\"id\":")
                    .append(resultSet.getInt("id"))
                    .append(",");

                json.append("\"fullName\":\"")
                    .append(escape(
                            resultSet.getString("fullname")
                    ))
                    .append("\",");

                json.append("\"email\":\"")
                    .append(escape(
                            resultSet.getString("email")
                    ))
                    .append("\",");

                json.append("\"phone\":\"")
                    .append(escape(
                            resultSet.getString("phone")
                    ))
                    .append("\",");

                json.append("\"bloodGroup\":\"")
                    .append(escape(
                            resultSet.getString("bloodgroup")
                    ))
                    .append("\",");

                json.append("\"city\":\"")
                    .append(escape(
                            resultSet.getString("city")
                    ))
                    .append("\",");

                json.append("\"state\":\"")
                    .append(escape(
                            resultSet.getString("state")
                    ))
                    .append("\"");

                json.append("}");

                first = false;
            }

            json.append("]");

            out.write(json.toString());

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            out.write(
                    "{\"error\":\"Unable to search donors.\"}"
            );
        }
    }

    private String escape(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}