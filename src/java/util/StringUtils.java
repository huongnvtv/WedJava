/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nguyễn Tiến Dũng
 */
public class StringUtils implements Serializable {

    public static boolean checkRegex(String regex, String value) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }
    
    public static String nvl(String value, String strDefault){
        if(value == null){
            return strDefault;
        }else{
            return value;
        }
    }
}
