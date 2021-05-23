/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.User;
import java.io.Serializable;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import model.LoginModel;
import util.DateUtils;
import util.MessagesUtils;
import util.SessionUtils;

/**
 *
 * @author ADMIN
 */
@ManagedBean
@ViewScoped
public class LoginController implements Serializable{

    private User user;
    private LoginModel loginModel;
    private User changeNewPassword;
    /**
     * Creates a new instance of LoginController
     */
    public LoginController() {
        try {
            user = new User();
            loginModel = new LoginModel();
            changeNewPassword=new User();
            changeNewPassword=loginModel.getUserCurrent();
        } catch (Exception ex) {
           ex.printStackTrace();
        }
    }
    public String converFormatDate(Date date){
        try {
            return DateUtils.convertDate(date,"dd/MM/yyyy");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
    public void handStatus(){
        this.changeNewPassword=new User();
    }
    public void handSave(){
        try {
            loginModel.updatePassword(changeNewPassword);
            MessagesUtils.info("","update mật khẩu thành công!!!");
            //return "TrangChu.xhtml";
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return "";
    }
    public long ipToLong(String ipAddress) {
    String[] ipAddressInArray = ipAddress.split("\\.");
    long result = 0;
    for (int i = 0; i < ipAddressInArray.length; i++) {

        int power = 3 - i;
        int ip = Integer.parseInt(ipAddressInArray[i]);
        result += ip * Math.pow(256, power);

    }
    return result;
  }
    public String handLogin() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(user.getPassword().getBytes());
            byte[] digest = md.digest();
            String password = DatatypeConverter
                    .printHexBinary(digest).toLowerCase();
            User checkLogin = loginModel.checkLogin(user.getUserName(), password);
            String[] grantedIp = checkLogin.getGrantedIp().split(";");
            boolean checkIp = false;
            InetAddress myIp = InetAddress.getLocalHost();
            String ipCurr = (String) (myIp.getHostAddress());
            for (String w : grantedIp) {
                if(w.indexOf("-")!=-1){
                    String[] cutIp=w.split("-");
                    long a=ipToLong(cutIp[0]);
                    long b=ipToLong(cutIp[1]);
                    long x=ipToLong(ipCurr);
                    if(x>=a && x<=b) checkIp=true;
                    else checkIp=false;
                }else{
                    if (w.equals(ipCurr) == true) {
                    checkIp = true;
                }
                }
            }
              if(!checkIp){
                MessagesUtils.error("","Máy tính của bạn không được quyền truy nhập");
            } 
            if (checkLogin != null && checkIp ) {
                HttpSession session = SessionUtils.getSession();
                session.setAttribute("USER_NAME", user.getUserName());
                Date lastDate = checkLogin.getLastTimeCHGPWD();
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                cal.add(Calendar.MONTH, -3); 
                if(getDate(cal).compareTo(lastDate)>0) {
                    MessagesUtils.warn("", "Hơn 3 tháng bạn đã chưa thay đổi mật khẩu");
                    return "/Pages/SuaPassword.xhtml";
                } else {
                    return "TrangChu.xhtml";
                }
            } else {
                MessagesUtils.error("", "Tên đăng nhập hoặc mật khẩu không đúng!");
            }
        } catch (Exception e) {
            MessagesUtils.error("", "Tên đăng nhập hoặc mật khẩu không đúng!");
            e.printStackTrace();
        }
        return "";
    }

    public static Date getDate(Calendar cal) {
        try {
            String dateString = cal.get(Calendar.DATE) + "/"
                    + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
            return date1;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String handLogout() {
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        return "/Login?faces-redirect=true";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LoginModel getLoginModel() {
        return loginModel;
    }

    public void setLoginModel(LoginModel loginModel) {
        this.loginModel = loginModel;
    }

    public User getChangeNewPassword() {
        return changeNewPassword;
    }

    public void setChangeNewPassword(User changeNewPassword) {
        this.changeNewPassword = changeNewPassword;
    }

}
