package com.redpulse.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.redpulse.util.DBConnection;

@WebServlet("/BloodRequestServlet")
@MultipartConfig
public class BloodRequestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    // ==========================
    // GET - LOAD REQUESTS
    // ==========================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null ||
            session.getAttribute("userId") == null) {

            response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
            );

            return;
        }


        String sql =
            "SELECT id, patient_name, blood_group, " +
            "hospital_name, hospital_address, " +
            "required_date, units_needed, " +
            "contact_phone, message, status " +
            "FROM blood_requests " +
            "ORDER BY id DESC";


        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            PrintWriter out = response.getWriter()
        ) {

            StringBuilder json =
                new StringBuilder("[");

            boolean first = true;


            while (rs.next()) {

                if (!first) {
                    json.append(",");
                }

                first = false;


                json.append("{");


                // ID
                json.append("\"id\":")
                    .append(rs.getInt("id"))
                    .append(",");


                // PATIENT NAME
                json.append("\"patientName\":\"")
                    .append(
                        escapeJson(
                            rs.getString("patient_name")
                        )
                    )
                    .append("\",");


                // BLOOD GROUP
                json.append("\"bloodGroup\":\"")
                    .append(
                        escapeJson(
                            rs.getString("blood_group")
                        )
                    )
                    .append("\",");


                // HOSPITAL NAME
                json.append("\"hospitalName\":\"")
                    .append(
                        escapeJson(
                            rs.getString("hospital_name")
                        )
                    )
                    .append("\",");


                // HOSPITAL ADDRESS
                json.append("\"hospitalAddress\":\"")
                    .append(
                        escapeJson(
                            rs.getString("hospital_address")
                        )
                    )
                    .append("\",");


                // REQUIRED DATE
                json.append("\"requiredDate\":\"")
                    .append(
                        escapeJson(
                            rs.getString("required_date")
                        )
                    )
                    .append("\",");


                // UNITS
                json.append("\"units\":")
                    .append(
                        rs.getInt("units_needed")
                    )
                    .append(",");


                // CONTACT NUMBER
                json.append("\"contactNumber\":\"")
                    .append(
                        escapeJson(
                            rs.getString("contact_phone")
                        )
                    )
                    .append("\",");


                // STATUS
                // IMPORTANT: closing quote is included here
                json.append("\"status\":\"")
                    .append(
                        escapeJson(
                            rs.getString("status")
                        )
                    )
                    .append("\",");


                // CITY
                // City is stored inside message because
                // blood_requests table has no city column.
                String message =
                    rs.getString("message");

                String city = "";

                if (message != null &&
                    message.startsWith("City: ")) {

                    int separator =
                        message.indexOf(" | Priority:");

                    if (separator > 6) {

                        city =
                            message.substring(
                                6,
                                separator
                            ).trim();
                    }
                }


                json.append("\"city\":\"")
                    .append(
                        escapeJson(city)
                    )
                    .append("\",");


                // PRIORITY
                String priority = "Normal";

                if (message != null &&
                    message.contains("Priority:")) {

                    int priorityIndex =
                        message.indexOf("Priority:");

                    if (priorityIndex >= 0) {

                        String extracted =
                            message.substring(
                                priorityIndex +
                                "Priority:".length()
                            ).trim();

                        if (!extracted.isEmpty()) {
                            priority = extracted;
                        }
                    }
                }


                json.append("\"priority\":\"")
                    .append(
                        escapeJson(priority)
                    )
                    .append("\"");


                json.append("}");
            }


            json.append("]");


            out.print(
                json.toString()
            );


        } catch (SQLException e) {

            e.printStackTrace();

            response.setStatus(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            response.getWriter().print(
                "{\"error\":\"Unable to load blood requests\"}"
            );
        }
    }



    // ==========================
    // POST - CREATE REQUEST
    // ==========================

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");


        HttpSession session =
            request.getSession(false);


        // ==========================
        // CHECK LOGIN
        // ==========================

        if (session == null ||
            session.getAttribute("userId") == null) {

            response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
            );

            response.getWriter().print(
                "Please login before creating a blood request."
            );

            return;
        }


        // ==========================
        // GET USER ID
        // ==========================

        int requesterId;

        try {

            requesterId =
                Integer.parseInt(
                    session
                        .getAttribute("userId")
                        .toString()
                );

        } catch (Exception e) {

            response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
            );

            response.getWriter().print(
                "Invalid user session."
            );

            return;
        }


        // ==========================
        // GET FORM VALUES
        // ==========================

        String patientName =
            request.getParameter("patientName");

        String bloodGroup =
            request.getParameter("bloodGroup");

        String unitsValue =
            request.getParameter("units");

        String hospitalName =
            request.getParameter("hospitalName");

        String hospitalAddress =
            request.getParameter("hospitalAddress");

        String city =
            request.getParameter("city");

        String requiredDate =
            request.getParameter("requiredDate");

        String priority =
            request.getParameter("priority");

        String contactNumber =
            request.getParameter("contactNumber");


        // ==========================
        // VALIDATION
        // ==========================

        if (patientName == null ||
            patientName.trim().isEmpty()) {

            response.setStatus(
                HttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().print(
                "Please enter patient name."
            );

            return;
        }


        if (bloodGroup == null ||
            bloodGroup.trim().isEmpty()) {

            response.setStatus(
                HttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().print(
                "Please select blood group."
            );

            return;
        }


        if (unitsValue == null ||
            unitsValue.trim().isEmpty()) {

            response.setStatus(
                HttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().print(
                "Please enter units required."
            );

            return;
        }


        if (hospitalName == null ||
            hospitalName.trim().isEmpty()) {

            response.setStatus(
                HttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().print(
                "Please enter hospital name."
            );

            return;
        }


        // ==========================
        // CONVERT UNITS
        // ==========================

        int units;

        try {

            units =
                Integer.parseInt(
                    unitsValue.trim()
                );

        } catch (NumberFormatException e) {

            response.setStatus(
                HttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().print(
                "Units must be a valid number."
            );

            return;
        }


        if (units < 1 || units > 10) {

            response.setStatus(
                HttpServletResponse.SC_BAD_REQUEST
            );

            response.getWriter().print(
                "Units must be between 1 and 10."
            );

            return;
        }


        // ==========================
        // MESSAGE
        // ==========================

        String finalCity =
            city == null
                ? ""
                : city.trim();

        String finalPriority =
            priority == null ||
            priority.trim().isEmpty()
                ? "Normal"
                : priority.trim();


        String message =
            "City: " +
            finalCity +
            " | Priority: " +
            finalPriority;


        // ==========================
        // INSERT DATABASE
        // ==========================

        String sql =
            "INSERT INTO blood_requests " +
            "(requester_id, patient_name, blood_group, " +
            "hospital_name, hospital_address, required_date, " +
            "units_needed, contact_phone, message, status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'Pending')";


        try (
            Connection con =
                DBConnection.getConnection();

            PreparedStatement ps =
                con.prepareStatement(sql)
        ) {

            ps.setInt(
                1,
                requesterId
            );


            ps.setString(
                2,
                patientName.trim()
            );


            ps.setString(
                3,
                bloodGroup.trim()
            );


            ps.setString(
                4,
                hospitalName.trim()
            );


            if (hospitalAddress == null ||
                hospitalAddress.trim().isEmpty()) {

                ps.setString(
                    5,
                    null
                );

            } else {

                ps.setString(
                    5,
                    hospitalAddress.trim()
                );
            }


            // ==========================
            // REQUIRED DATE
            // ==========================

            if (requiredDate == null ||
                requiredDate.trim().isEmpty()) {

                ps.setDate(
                    6,
                    null
                );

            } else {

                try {

                    ps.setDate(
                        6,
                        java.sql.Date.valueOf(
                            requiredDate.trim()
                        )
                    );

                } catch (IllegalArgumentException e) {

                    response.setStatus(
                        HttpServletResponse.SC_BAD_REQUEST
                    );

                    response.getWriter().print(
                        "Invalid required date."
                    );

                    return;
                }
            }


            // UNITS
            ps.setInt(
                7,
                units
            );


            // CONTACT
            if (contactNumber == null ||
                contactNumber.trim().isEmpty()) {

                ps.setString(
                    8,
                    null
                );

            } else {

                ps.setString(
                    8,
                    contactNumber.trim()
                );
            }


            // MESSAGE
            ps.setString(
                9,
                message
            );


            // ==========================
            // EXECUTE INSERT
            // ==========================

            int rows =
                ps.executeUpdate();


            if (rows > 0) {

                response.getWriter().print(
                    "SUCCESS"
                );

            } else {

                response.setStatus(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                );

                response.getWriter().print(
                    "Blood request could not be created."
                );
            }


        } catch (SQLException e) {

            e.printStackTrace();

            response.setStatus(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            response.getWriter().print(
                "Database error: " +
                e.getMessage()
            );
        }
    }



    // ==========================
    // JSON ESCAPE
    // ==========================

    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r");
    }

}