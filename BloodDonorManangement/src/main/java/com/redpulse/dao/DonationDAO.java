package com.redpulse.dao;

import com.redpulse.model.Donation;
import com.redpulse.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.sql.SQLException;

public class DonationDAO {

    public boolean addDonation(Donation donation) {

        String sql =
                "INSERT INTO donations " +
                "(user_id, hospital_name, hospital_address, " +
                "donation_date, donation_time, blood_group, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            if (connection == null) {
                System.out.println("Database connection is NULL.");
                return false;
            }

            statement.setInt(
                    1,
                    donation.getUserId()
            );

            statement.setString(
                    2,
                    donation.getHospitalName()
            );

            statement.setString(
                    3,
                    donation.getHospitalAddress()
            );

            statement.setDate(
                    4,
                    Date.valueOf(donation.getDonationDate())
            );

            statement.setTime(
                    5,
                    Time.valueOf(
                            donation.getDonationTime() + ":00"
                    )
            );

            statement.setString(
                    6,
                    donation.getBloodGroup()
            );

            statement.setString(
                    7,
                    "Pending"
            );

            int rows = statement.executeUpdate();

            System.out.println(
                    "Donation insert rows: " + rows
            );

            return rows > 0;

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Invalid date or time format."
            );

            e.printStackTrace();

            return false;

        } catch (SQLException e) {

            System.out.println(
                    "Error while inserting donation."
            );

            e.printStackTrace();

            return false;
        }
    }
}