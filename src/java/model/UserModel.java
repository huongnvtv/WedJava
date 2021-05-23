/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.User;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import util.ConnectionUtil;
import util.DateUtils;

/**
 *
 * @author ADMIN
 */
public class UserModel extends ConnectionUtil implements Serializable {

    public List<User> getListUser() throws Exception {
        List<User> lstUser = new ArrayList<>();
        String sql = "select * from ADMIN_USER";
        try {
            open();
            mStmt = mConnection.prepareStatement(sql);
            mRs = mStmt.executeQuery();
            while (mRs.next()) {
                User user = new User();
                user.setUserId(mRs.getLong("USER_ID"));
                user.setUserName(mRs.getString("USER_NAME"));
                user.setLastTimeCHGPWD(mRs.getDate("LAST_TIME_CHG_PWD"));
                user.setFullName(mRs.getString("FULL_NAME"));
                user.setStatus(mRs.getString("status"));
                user.setTelNumber(mRs.getString("TEL_NUMBER"));
                user.setEmail(mRs.getString("email"));
                user.setGrantedIp(mRs.getString("granted_ip"));
                user.setRePassword(mRs.getString("password"));
                lstUser.add(user);
            }
        } finally {
            close();
        }
        return lstUser;
    }

    public void insert(User user) throws Exception {
        String sql = "insert into ADMIN_USER(USER_ID,USER_NAME,FULL_NAME,PASSWORD,LAST_TIME_CHG_PWD,GRANTED_IP,EMAIL,TEL_NUMBER,STATUS) "
                + "values(USER_ID.NEXTVAL,?,?,?,SYSDATE,?,?,?,1)";
        try {
            open();
            mStmt = mConnection.prepareStatement(sql);
            mStmt.setString(1, user.getUserName());
            mStmt.setString(2, user.getFullName());
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(user.getPassword().getBytes());
            byte[] digest = md.digest();
            String password = DatatypeConverter
                    .printHexBinary(digest).toLowerCase();
            mStmt.setString(3, password);
            mStmt.setString(4, user.getGrantedIp());
            mStmt.setString(5, user.getEmail());
            mStmt.setString(6, user.getTelNumber());
            mStmt.executeUpdate();
        } finally {
            close();
        }
    }

    public void insertList(User user) throws Exception {
        String sql = "insert into ADMIN_USER(USER_ID,USER_NAME,FULL_NAME,PASSWORD,LAST_TIME_CHG_PWD,GRANTED_IP,EMAIL,TEL_NUMBER,STATUS) "
                + "values(?,?,?,?,SYSDATE,?,?,?,1)";
        try {
            open();
            mStmt = mConnection.prepareStatement(sql);
            mStmt.setLong(1, user.getUserId());
            mStmt.setString(2, user.getUserName());
            mStmt.setString(3, user.getFullName());
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(user.getPassword().getBytes());
            byte[] digest = md.digest();
            String password = DatatypeConverter
                    .printHexBinary(digest).toLowerCase();
            mStmt.setString(4, password);
            mStmt.setString(5, user.getGrantedIp());
            mStmt.setString(6, user.getEmail());
            mStmt.setString(7, user.getTelNumber());
            mStmt.executeUpdate();
        } finally {
            close();
        }
    }

    public void delete(User user) throws Exception {
        String sql = "delete from ADMIN_USER where USER_ID=?";
        try {
            open();
            mStmt = mConnection.prepareStatement(sql);
            mStmt.setLong(1, user.getUserId());
            mStmt.executeUpdate();
        } finally {
            close();
        }
    }
    public void update(User user, String newPassword) throws Exception {
        try {
            if (newPassword != "") {
                String sql = "update ADMIN_USER set USER_NAME=?, full_name=? ,password=? ,LAST_TIME_CHG_PWD=SYSDATE,"
                        + "GRANTED_IP=? ,EMAIL=? ,\n"
                        + "TEL_NUMBER=? where USER_ID=?";
                open();
                mStmt = mConnection.prepareStatement(sql);
                mStmt.setString(1, user.getUserName());
                mStmt.setString(2, user.getFullName());
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(user.getPassword().getBytes());
                byte[] digest = md.digest();
                String password = DatatypeConverter
                        .printHexBinary(digest).toLowerCase();
                mStmt.setString(3, password);
                mStmt.setString(4, user.getGrantedIp());
                mStmt.setString(5, user.getEmail());
                mStmt.setString(6, user.getTelNumber());
                mStmt.setLong(7, user.getUserId());
                mStmt.executeUpdate();
            } else {
                String sql = "update ADMIN_USER set USER_NAME=?, full_name=? ,LAST_TIME_CHG_PWD=SYSDATE,"
                        + "GRANTED_IP=? ,EMAIL=? ,\n"
                        + "TEL_NUMBER=? where USER_ID=?";
                open();
                mStmt = mConnection.prepareStatement(sql);
                mStmt.setString(1, user.getUserName());
                mStmt.setString(2, user.getFullName());
                mStmt.setString(3, user.getGrantedIp());
                mStmt.setString(4, user.getEmail());
                mStmt.setString(5, user.getTelNumber());
                mStmt.setLong(6, user.getUserId());
                mStmt.executeUpdate();
            }
        } finally {
            close();
        }
    }
}
