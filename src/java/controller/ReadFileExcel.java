/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import entity.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.DateUtils;

/*
 * @author ADMIN
 */
public class ReadFileExcel {

    public static void main(String[] args) {
        List<User> lstUser = new ArrayList<>();
        try {
            File file = new File("C:\\Users\\ADMIN\\Documents\\Zalo Received Files\\quan_li_admin.xlsx");
            FileInputStream excelFile = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            Row currentRow = iterator.next();
           
            while (iterator.hasNext()) {
                User user = new User();
                currentRow=iterator.next();
                Iterator<Cell> cellIterator = currentRow.iterator();
                try {
                    Cell currentCell = cellIterator.next();
                    String strUserId = currentCell.getStringCellValue();
                    long userId = Long.parseLong(strUserId);
                    user.setUserId(userId);
                    currentCell=cellIterator.next();
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
    }
}
