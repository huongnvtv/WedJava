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
import util.SessionUtils;

/**
 *
 * @author ADMIN
 */
public class LoginModel extends ConnectionUtil implements Serializable{
    public User checkLogin(String username,String password)throws Exception{
        String sql="select * from ADMIN_USER where USER_NAME=? and PASSWORD=? and status =1";
        try {
            open();
            mStmt=mConnection.prepareStatement(sql);
            mStmt.setString(1,username);
            mStmt.setString(2,password);
            mRs=mStmt.executeQuery();
            while(mRs.next()){
                User user=new User();
                user.setUserName(mRs.getString("USER_NAME"));
                user.setPassword(mRs.getString("PASSWORD"));
                user.setStatus(mRs.getString("status"));
                user.setLastTimeCHGPWD(mRs.getDate("LAST_TIME_CHG_PWD"));
                user.setGrantedIp(mRs.getString("GRANTED_IP"));
                return user;
            }
        }finally{
            close();
        }
        return null;
    }
    public User getUserCurrent() throws Exception{
        String sql="select * from ADMIN_USER where USER_NAME=?";
        
        try {
            open();
            mStmt=mConnection.prepareStatement(sql);
            mStmt.setString(1,SessionUtils.getUserName());
            mRs=mStmt.executeQuery();
            while(mRs.next()){
                User user=new User();
                user.setUserName(mRs.getString("USER_NAME"));
                user.setPassword(mRs.getString("PASSWORD"));
                user.setUserId(mRs.getLong("USER_ID"));
                user.setFullName(mRs.getString("FULL_NAME"));
                user.setGrantedIp(mRs.getString("GRANTED_IP"));
                user.setEmail(mRs.getString("email"));
                user.setLastTimeCHGPWD(mRs.getDate("LAST_TIME_CHG_PWD"));
                user.setStatus(mRs.getString("status"));
                user.setTelNumber(mRs.getString("TEL_NUMBER"));
                return user;
            }
        }finally {
            close();
        }
        return null;
    }
    public void updatePassword(User user) throws Exception{
        String sql="update  ADMIN_USER set PASSWORD=? , LAST_TIME_CHG_PWD=SYSDATE where USER_NAME=?";
        try {
            open();
            mStmt=mConnection.prepareStatement(sql);
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(user.getNewPassword().getBytes());
            byte[] digest = md.digest();
            String password = DatatypeConverter
            .printHexBinary(digest).toLowerCase(); 
            mStmt.setString(1,password);
            mStmt.setString(2,user.getUserName());
            mStmt.executeUpdate();
        }finally{
            close();
        }
    }
}
