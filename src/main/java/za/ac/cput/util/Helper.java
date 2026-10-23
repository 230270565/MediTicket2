/*
 Helper.java

 Shared validation helpers used by the factories

 Authors:
  Joshua Reid Adams (230317693)
  Abdullahi Raage Farah (230971091)
  Aidan Barends (230255639)
  Jaden Clayton Abrahams (222206721)
  Joshua Peter Bonzet (221312536)
  Matthew Barron (230398863)
  Raul Jaaim Everts (230270565)

 Date: 20th June 2026
*/

package za.ac.cput.util;

import org.apache.commons.validator.routines.EmailValidator;

public class Helper {

    // Prevent instantiation - utility class
    private Helper() {}

    // Check if a String is null or empty
    public static boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    // Validate email using commons-validator
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) return false;
        return EmailValidator.getInstance().isValid(email);
    }

    // Validate mobile number - must be digits only and between 10-15 chars
    public static boolean isValidMobileNumber(String mobileNumber) {
        if (isNullOrEmpty(mobileNumber)) return false;
        return mobileNumber.matches("\\d{10,15}");
    }

    // Validate positive double (price, payment, subTotal)
    public static boolean isValidDouble(double value) {
        return value > 0;
    }

    // Validate positive int (stockQuantity, quantity)
    public static boolean isValidInt(int value) {
        return value > 0;
    }

    // Validate object is not null (for Name, Product etc)
    public static boolean isValidObject(Object object) {
        return object != null;
    }
}
