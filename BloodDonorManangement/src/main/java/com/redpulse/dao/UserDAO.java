package com.redpulse.dao;

import com.redpulse.model.User;
import com.redpulse.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // =========================
    // REGISTER USER
    // =========================
    public boolean registerUser(User user) {

        String sql = "INSERT INTO users "
                + "(fullname, email, phone, bloodgroup, password, dob, "
                + "gender, state, city, landmark, medical_condition, "
                + "last_donation_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, user.getFullName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getBloodGroup());
            statement.setString(5, user.getPassword());

            // DOB
            if (user.getDob() != null && !user.getDob().isEmpty()) {
                statement.setDate(
                        6,
                        Date.valueOf(user.getDob())
                );
            } else {
                statement.setNull(6, java.sql.Types.DATE);
            }

            statement.setString(7, user.getGender());
            statement.setString(8, user.getState());
            statement.setString(9, user.getCity());
            statement.setString(10, user.getLandmark());
            statement.setString(11, user.getMedicalCondition());

            // Last donation date
            if (user.getLastDonationDate() != null
                    && !user.getLastDonationDate().isEmpty()) {

                statement.setDate(
                        12,
                        Date.valueOf(user.getLastDonationDate())
                );

            } else {
                statement.setNull(12, java.sql.Types.DATE);
            }

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // LOGIN USER
    // =========================
    public User loginUser(String email, String password) {

        String sql = "SELECT * FROM users "
                + "WHERE email = ? AND password = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                User user = new User();

                user.setId(
                        resultSet.getInt("id")
                );

                user.setFullName(
                        resultSet.getString("fullname")
                );

                user.setEmail(
                        resultSet.getString("email")
                );

                user.setPhone(
                        resultSet.getString("phone")
                );

                user.setBloodGroup(
                        resultSet.getString("bloodgroup")
                );

                user.setPassword(
                        resultSet.getString("password")
                );

                Date dob = resultSet.getDate("dob");

                if (dob != null) {
                    user.setDob(dob.toString());
                }

                user.setGender(
                        resultSet.getString("gender")
                );

                user.setState(
                        resultSet.getString("state")
                );

                user.setCity(
                        resultSet.getString("city")
                );

                user.setLandmark(
                        resultSet.getString("landmark")
                );

                user.setMedicalCondition(
                        resultSet.getString("medical_condition")
                );

                Date lastDonation =
                        resultSet.getDate("last_donation_date");

                if (lastDonation != null) {
                    user.setLastDonationDate(
                            lastDonation.toString()
                    );
                }

                return user;
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }
 // ==================================================
 // UPDATE USER PROFILE
 // ==================================================

 public boolean updateUser(User user) {

     String sql =
             "UPDATE users SET "
             + "fullname = ?, "
             + "email = ?, "
             + "phone = ?, "
             + "bloodgroup = ?, "
             + "dob = ?, "
             + "gender = ?, "
             + "state = ?, "
             + "city = ?, "
             + "landmark = ?, "
             + "medical_condition = ?, "
             + "last_donation_date = ? "
             + "WHERE id = ?";

     try (Connection connection = DBConnection.getConnection();
          PreparedStatement statement =
                  connection.prepareStatement(sql)) {

         statement.setString(
                 1,
                 user.getFullName()
         );

         statement.setString(
                 2,
                 user.getEmail()
         );

         statement.setString(
                 3,
                 user.getPhone()
         );

         statement.setString(
                 4,
                 user.getBloodGroup()
         );


         // DOB

         if (user.getDob() != null &&
             !user.getDob().isEmpty()) {

             statement.setDate(
                     5,
                     Date.valueOf(user.getDob())
             );

         } else {

             statement.setNull(
                     5,
                     java.sql.Types.DATE
             );
         }


         statement.setString(
                 6,
                 user.getGender()
         );

         statement.setString(
                 7,
                 user.getState()
         );

         statement.setString(
                 8,
                 user.getCity()
         );

         statement.setString(
                 9,
                 user.getLandmark()
         );

         statement.setString(
                 10,
                 user.getMedicalCondition()
         );


         // Last donation date

         if (user.getLastDonationDate() != null &&
             !user.getLastDonationDate().isEmpty()) {

             statement.setDate(
                     11,
                     Date.valueOf(
                             user.getLastDonationDate()
                     )
             );

         } else {

             statement.setNull(
                     11,
                     java.sql.Types.DATE
             );
         }


         statement.setInt(
                 12,
                 user.getId()
         );


         int rows =
                 statement.executeUpdate();

         return rows > 0;

     } catch (SQLException e) {

         e.printStackTrace();

         return false;
     }
 }
}