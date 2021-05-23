/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;


/**
 *
 * @author ADMIN
 */
public class User implements Serializable{
    private static final long serialVersionUID = 1420672609912364060L;
    private long userId;
    private String userName;
    private String fullName;
    private String status;
    private String password;
    private Date lastTimeCHGPWD;
    private String grantedIp;
    private String email;
    private String telNumber;
    private String rePassword;
    private String newPassword;
    
    public User(){
        
    }
    public User(User obj) {
        this.userId = obj.userId;
        this.userName = obj.userName;
        this.fullName = obj.fullName;
        this.status = obj.status;
        this.password = obj.password;
        this.lastTimeCHGPWD = obj.lastTimeCHGPWD;
        this.grantedIp = obj.grantedIp;
        this.email = obj.email;
        this.telNumber = obj.telNumber;
        this.rePassword = obj.rePassword;
    }
    
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastTimeCHGPWD() {
        return lastTimeCHGPWD;
    }

    public void setLastTimeCHGPWD(Date lastTimeCHGPWD) {
        this.lastTimeCHGPWD = lastTimeCHGPWD;
    }

    public String getGrantedIp() {
        return grantedIp;
    }

    public void setGrantedIp(String grantedIp) {
        this.grantedIp = grantedIp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
}
