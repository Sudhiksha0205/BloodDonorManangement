package com.redpulse.dao;

import com.redpulse.model.BloodRequest;
import com.redpulse.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BloodRequestDAO {

    public boolean createRequest(BloodRequest request) {

        String sql =
                "INSERT INTO blood_requests "
                + "(user_id, patient_name, blood_group, units, "
                + "hospital_name, hospital_address, city, "
                + "required_date, priority, contact_number) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(
                    1,
                    request.getUserId()
            );

            statement.setString(
                    2,
                    request.getPatientName()
            );

            statement.setString(
                    3,
                    request.getBloodGroup()
            );

            statement.setInt(
                    4,
                    request.getUnits()
            );

            statement.setString(
                    5,
                    request.getHospitalName()
            );

            statement.setString(
                    6,
                    request.getHospitalAddress()
            );

            statement.setString(
                    7,
                    request.getCity()
            );

            statement.setDate(
                    8,
                    Date.valueOf(
                            request.getRequiredDate()
                    )
            );

            statement.setString(
                    9,
                    request.getPriority()
            );

            statement.setString(
                    10,
                    request.getContactNumber()
            );

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }


    public List<BloodRequest> getRecentRequests() {

        List<BloodRequest> requests =
                new ArrayList<>();

        String sql =
                "SELECT * FROM blood_requests "
                + "ORDER BY created_at DESC";

        try (Connection connection =
                     DBConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql);

             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                BloodRequest request =
                        new BloodRequest();

                request.setId(
                        resultSet.getInt("id")
                );

                request.setUserId(
                        resultSet.getInt("user_id")
                );

                request.setPatientName(
                        resultSet.getString("patient_name")
                );

                request.setBloodGroup(
                        resultSet.getString("blood_group")
                );

                request.setUnits(
                        resultSet.getInt("units")
                );

                request.setHospitalName(
                        resultSet.getString("hospital_name")
                );

                request.setHospitalAddress(
                        resultSet.getString(
                                "hospital_address"
                        )
                );

                request.setCity(
                        resultSet.getString("city")
                );

                Date date =
                        resultSet.getDate("required_date");

                if (date != null) {
                    request.setRequiredDate(
                            date.toString()
                    );
                }

                request.setPriority(
                        resultSet.getString("priority")
                );

                request.setContactNumber(
                        resultSet.getString(
                                "contact_number"
                        )
                );

                requests.add(request);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return requests;
    }
}