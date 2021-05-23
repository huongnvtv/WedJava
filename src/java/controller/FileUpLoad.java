/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.primefaces.model.file.UploadedFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import model.UserModel;
import util.DateUtils;
import util.MessagesUtils;
import util.StringUtils;

/**
 *
 * @author ADMIN
 */
@ManagedBean
@SessionScoped
public class FileUpLoad implements Serializable {

    private UploadedFile file;
    private List<User> lstUser;
    private UserModel userModel; 
    /**
     * Creates a new instance of FileUpLoad
     */
    public FileUpLoad() {
        lstUser = new ArrayList<>();
        userModel=new UserModel();
    }
    public boolean checkValidIp(User user) {
        String ip = "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.(?!$)|$)){4}$";
        boolean checkIp = true;
        String[] grantedIp = user.getGrantedIp().split(";");
        for (String w : grantedIp) {
            if (w.indexOf("-") != -1) {
                String[] cutIp = w.split("-");
                if (!StringUtils.checkRegex(ip, cutIp[0])) {
                    checkIp = false;
                } else if (!StringUtils.checkRegex(ip, cutIp[1])) {
                    checkIp = false;
                }
            } else {
                if (!StringUtils.checkRegex(ip, w)) {
                    checkIp = false;
                }
            }
        }
        return checkIp;
    }
    public boolean valid(User user) {
        if (!user.getPassword().equals(user.getRePassword())) {
            MessagesUtils.error("", "Bạn nhập mật lại mật khẩu không đúng!!!");
            return false;
        } 
        if (!StringUtils.checkRegex("^[\\w]+$", user.getUserName())) {
            MessagesUtils.error("", "Tên đăng nhập không được bắt đầu bằng số từ 5 đến 29 kí tự");
            return false;
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (!StringUtils.checkRegex("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", user.getEmail())) {
                MessagesUtils.error("", "Email không hợp lệ");
                return false;
            }
        }
        if (user.getTelNumber() != null && !user.getTelNumber().isEmpty()) {
            if (!StringUtils.checkRegex("^[\\d]{10}$", user.getTelNumber())) {
                MessagesUtils.error("", "Số điện thoại không hợp lệ");
                return false;
            }
        }
        if (!checkValidIp(user)) {
            MessagesUtils.error("", "Nhập ip không đúng định dạng");
            return false;
        }
        return true;
    }

    public void upload() {
        if (file.getSize() > 0) {
            MessagesUtils.info("","welcome "+file.getFileName());
            lstUser=readFileExcelReturnList(file.getFileName());
            for(User user:lstUser){
                user.setRePassword(user.getPassword());
                if(valid(user)){
                    try {
                        userModel.insert(user);
                        MessagesUtils.info("","Chúc mừng bạn thêm thành công :" + user.getUserName());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        MessagesUtils.error("",user.getUserName()+" chưa được thêm!!!");
                    }
                }
            }
        } else {
            MessagesUtils.error("","Upload không thành công!!!");
        }
    }

    public static List<User> readFileExcelReturnList(String fileName) {
        List<User> lstUser = new ArrayList<>();
        try {
            File file = new File("C:\\Users\\ADMIN\\Documents\\Zalo Received Files\\"+fileName);
            FileInputStream excelFile = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
             Iterator<Row> iterator = datatypeSheet.iterator();
            Row currentRow = iterator.next();
            while (iterator.hasNext()) {
                User user = new User();
                currentRow = iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                try {
                    Cell currentCell = cellIterator.next();
                    String strUserId = currentCell.getStringCellValue();
                    long userId = Long.parseLong(strUserId);
                    user.setUserId(userId);
                    currentCell = cellIterator.next();
                    user.setUserName(currentCell.getStringCellValue());
                    currentCell = cellIterator.next();
                    user.setFullName(currentCell.getStringCellValue());
                    currentCell = cellIterator.next();
                    user.setPassword(currentCell.getStringCellValue());
                    currentCell = cellIterator.next();
                    user.setStatus(currentCell.getStringCellValue());
                    currentCell = cellIterator.next();
                    String date = (currentCell.getStringCellValue());
                    try {
                        Date dateUser = DateUtils.convertStringToDate(date, "dd/MM/yyyy");
                        user.setLastTimeCHGPWD(dateUser);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    currentCell = cellIterator.next();
                    user.setGrantedIp(currentCell.getStringCellValue());
                    currentCell = cellIterator.next();
                    user.setEmail(currentCell.getStringCellValue());
                    currentCell = cellIterator.next();
                    user.setTelNumber(currentCell.getStringCellValue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                lstUser.add(user);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lstUser;
    }

    public List<User> getLstUser() {
        return lstUser;
    }

    public void setLstUser(List<User> lstUser) {
        this.lstUser = lstUser;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
