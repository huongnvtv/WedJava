/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.User;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import model.UserModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import util.ActionUtil;
import util.DateUtils;
import util.MessagesUtils;
import util.StringUtils;

/**
 *
 * @author ADMIN
 */
@ManagedBean
@SessionScoped
public class UserController extends ActionUtil implements Serializable {

    private User user;
    private List<User> lstUser;
    private UserModel userModel;
    private List<User> lstUserExcel;
    private UploadedFile file;
    private StreamedContent fileDowload;
    private StreamedContent fileLog;
    private boolean disableLog;
    private static final long serialVersionUID = 1420672609912364060L;

    /**
     * Creates a new instance of UserModel
     */
    public UserController() {
        try {
            disableLog = false;
            user = new User();
            userModel = new UserModel();
            lstUser = userModel.getListUser();
            lstUserExcel = new ArrayList<>();
            fileDowload = DefaultStreamedContent.builder()
                    .name("file_excel_mau.xlsx")
                    .contentType("xls/xlsx")
                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resouce/excelMau/file_excel_mau.xlsx"))
                    .build();
            fileLog = DefaultStreamedContent.builder()
                    .name("writeLogExcel.txt")
                    .contentType("txt")
                    .stream(() -> FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resouce/excelMau/writeLogExcel.txt"))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        this.user = new User();
    }

    public void upload() {
        int countTC = 0, countTB = 0;
        try {

            if (file.getSize() > 0) {
                MessagesUtils.info("", "welcome " + file.getFileName());
                lstUserExcel = readFileExcelReturnList(file.getFileName());
                for (User user : lstUserExcel) {
                    user.setRePassword(user.getPassword());
                    if (validFile(user) && checkUserTrungFile(user)) {
                        try {
                            lstUser.add(user);
                            userModel.insertList(user);
                            countTC++;
                            MessagesUtils.info("", "Chúc mừng bạn thêm thành công :" + user.getUserName());
                        } catch (Exception ex) {
                            countTB++;
                            ex.printStackTrace();
                        }
                    } else {
                        countTB++;
                        MessagesUtils.error("", user.getUserName() + " chưa được thêm!!!");
                    }
                }
                MessagesUtils.info("", "Số bản ghi thành công :" + countTC);
                MessagesUtils.error("", "Số bản ghi thất bại :" + countTB);
            } else {
                MessagesUtils.error("", "Upload không thành công!!!");
            }
            handCancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        disableLog = true;
    }

    public static List<User> readFileExcelReturnList(String fileName) {
        List<User> lstUserReadFileExcel = new ArrayList<>();
        try {
           // File file = new File("C:\\Users\\ADMIN\\Documents\\Zalo Received Files\\" + fileName);
            File files=new File(fileName);
            String path=files.getAbsolutePath();
            File file=new File(path);
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
                lstUserReadFileExcel.add(user);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstUserReadFileExcel;
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
                long a = ipToLong(cutIp[0]);
                long b = ipToLong(cutIp[1]);
                if (a < b) {
                    checkIp = true;
                } else {
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

    public void handOk() {
        try {
            if (isAdd) {
                if (checkUserTrung(user) && valid(user)) {
                    lstUser.add(user);
                    userModel.insert(user);
                    MessagesUtils.info("", "Chúc mừng bạn thêm thành công!!!");
                    handCancel();
                } else {
                    MessagesUtils.error("", "User bạn thêm đã có!!!");
                }
            } else if (isEdit) {
                if (validEdit(user, user.getPassword())) {
                    userModel.update(user, user.getPassword());
                    MessagesUtils.info("", "Chúc mừng bạn sửa thành công!!!");
                    handCancel();
                }
            } else if (isCopy) {
                if (valid(user)) {
                    userModel.insert(user);
                    lstUser.add(user);
                    MessagesUtils.info("", "Chúc mừng bạn copy thành công!!!");
                    handCancel();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            MessagesUtils.error("", e.toString());
        }
    }

    public void changStatusAdd() {
        super.changeStateAdd();
        this.user = new User();
    }

    public void changStatusEdit(User user) {
        super.changeStateEdit();
        this.user = user;
    }

    public void changStatusCopy(User user) {
        super.changeStateCopy();
        this.user = new User(user);
    }

    public void changStatusView(User user) {
        super.changeStateView();
        this.user = user;
    }

    public void handDelete(User user) {
        try {
            lstUser.remove(user);
            userModel.delete(user);
            MessagesUtils.info("", "Chúc mừng bạn xóa thành công!!!");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessagesUtils.error("", ex.toString());
        }
    }

    public boolean checkUserTrung(User userCheck) throws IOException {
        for (User user : lstUser) {
            if (userCheck.getUserId() == user.getUserId()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkUserTrungFile(User userCheck) throws IOException {
        File file=new File("writeLogExcel.txt");
        String path=file.getAbsolutePath();
        FileWriter writer = new FileWriter(path, true);
        //FileWriter writer = new FileWriter("D:\\wedsite\\LOGIN\\web\\resouce\\excelMau\\writeLog.txt", true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        for (User user : lstUser) {
            if (userCheck.getUserId() == user.getUserId()) {
                bufferedWriter.write(user.getUserId() + " :da co");
                bufferedWriter.newLine();
                bufferedWriter.close();
                return false;
            }
        }
        return true;
    }

    public boolean valid(User user) {
        if (!checkPassword(user)) {
            return false;
        }
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

    public boolean validFile(User user) throws IOException {
        File file=new File("writeLogExcel.txt");
        String path=file.getAbsolutePath();
        FileWriter writer = new FileWriter(path, true);
        //FileWriter writer = new FileWriter("D:\\wedsite\\LOGIN\\web\\resouce\\excelMau\\writeLog.txt", true);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        if (!checkPasswordFile(user)) {
            return false;
        }
        if (!user.getPassword().equals(user.getRePassword())) {
            MessagesUtils.error("", "Bạn nhập mật lại mật khẩu không đúng!!!");
            return false;
        }
        if (!StringUtils.checkRegex("^[\\w]+$", user.getUserName())) {
            bufferedWriter.write(user.getUserName() + " :khong dung dinh dang ;");
            bufferedWriter.newLine();
            bufferedWriter.close();
            MessagesUtils.error("", "Tên đăng nhập không được bắt đầu bằng số từ 5 đến 29 kí tự");
            return false;
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            if (!StringUtils.checkRegex("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", user.getEmail())) {
                MessagesUtils.error("", "Email không hợp lệ");
                bufferedWriter.write(user.getEmail() + " :khong dung dinh dang ;");
                bufferedWriter.newLine();
                bufferedWriter.close();
                return false;
            }
        }
        if (user.getTelNumber() != null && !user.getTelNumber().isEmpty()) {
            if (!StringUtils.checkRegex("^[\\d]{10}$", user.getTelNumber())) {
                MessagesUtils.error("", "Số điện thoại không hợp lệ");
                bufferedWriter.write(user.getTelNumber() + " :khong dung dinh dang ;");
                bufferedWriter.newLine();
                bufferedWriter.close();
                return false;
            }
        }
        if (!checkValidIp(user)) {
            MessagesUtils.error("", "Nhập ip không đúng định dạng");
            bufferedWriter.write(user.getUserName() + " :ip khong dung dinh dang");
            bufferedWriter.newLine();
            bufferedWriter.close();
            return false;
        }
        bufferedWriter.close();
        return true;
    }

    public boolean validEdit(User user, String newPassword){
        if (newPassword != "") {
            if (!checkPassword(user)) {
                return false;
            }
            if (!user.getPassword().equals(user.getRePassword())) {
                MessagesUtils.error("", "Bạn nhập mật lại mật khẩu không đúng!!!");
                return false;
            }
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

    public boolean checkPassword(User user) {
        if (!StringUtils.checkRegex("^[\\w]+$", user.getPassword())) {
            MessagesUtils.error("", "Mật khẩu không đúng định dạng");
            return false;
        } else {
            return true;
        }
    }

    public boolean checkPasswordFile(User user) throws IOException {
        if (!StringUtils.checkRegex("^[\\w]+$", user.getPassword())) {
            MessagesUtils.error("", "Mật khẩu không đúng định dạng");
            File file=new File("writeLogExcel.txt");
            String path=file.getAbsolutePath();
            FileWriter writer = new FileWriter(path, true);
            //FileWriter writer = new FileWriter("D:\\wedsite\\LOGIN\\web\\resouce\\excelMau\\writeLog.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            bufferedWriter.write(user.getUserId() + " password khong dung dinh dang");
            bufferedWriter.newLine();
            bufferedWriter.close();
            return false;
        } else {
            return true;
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public List<User> getLstUserExcel() {
        return lstUserExcel;
    }

    public void setLstUserExcel(List<User> lstUserExcel) {
        this.lstUserExcel = lstUserExcel;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public StreamedContent getFileDowload() {
        return fileDowload;
    }

    public void setFileDowload(StreamedContent fileDowload) {
        this.fileDowload = fileDowload;
    }

    public StreamedContent getFileLog() {
        return fileLog;
    }

    public void setFileLog(StreamedContent fileLog) {
        this.fileLog = fileLog;
    }

    public boolean isDisableLog() {
        return disableLog;
    }

    public void setDisableLog(boolean disableLog) {
        this.disableLog = disableLog;
    }

}
