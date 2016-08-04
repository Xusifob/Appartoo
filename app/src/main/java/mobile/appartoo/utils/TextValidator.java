package mobile.appartoo.utils;

import android.text.TextWatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by alexandre on 16-08-04.
 */
public class TextValidator {

    public static boolean isEmail(String email) {
        //Regex defining an email
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

        //Return the match result of the regex
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean haveText(String stringToVerify){
        return !stringToVerify.replaceAll("\\s", "").equals("");
    }

    public static boolean haveText(String[] stringsToVerify){
        for(int i = 0 ; i < stringsToVerify.length ; i++) {
            if(stringsToVerify[i].replaceAll("\\s", "").equals("")) {
                return false;
            }
        }
        return true;
    }
}
