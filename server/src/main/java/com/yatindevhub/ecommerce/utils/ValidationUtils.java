package com.yatindevhub.ecommerce.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {

    public  static boolean checkPassword(String password){
        String regExpn = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])\\S{8,20}$";

        Pattern pattern = Pattern.compile(regExpn);
        Matcher matcher = pattern.matcher(password);
        //debugPassword(password);
        return matcher.matches();

    }

    public static void debugPassword(String password) {
        System.out.println("Password: " + password);

        // Rule 1: at least one digit
        System.out.println("Has digit: " + password.matches(".*[0-9].*"));

        // Rule 2: at least one lowercase
        System.out.println("Has lowercase: " + password.matches(".*[a-z].*"));

        // Rule 3: at least one uppercase
        System.out.println("Has uppercase: " + password.matches(".*[A-Z].*"));

        // Rule 4: at least one special char
        System.out.println("Has special: " + password.matches(".*[@#$%^&+=].*"));

        // Rule 5: no whitespace
        System.out.println("No whitespace: " + !password.matches(".*\\s.*"));

        // Rule 6: length between 8–20
        System.out.println("Length valid: " + (password.length() >= 8 && password.length() <= 20));

        // Final check using cleaned regex
        String regExpn = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])\\S{8,20}$";
        boolean matches = password.matches(regExpn);

        System.out.println("Final regex match: " + matches);
    }

}
