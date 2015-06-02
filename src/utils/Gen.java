package utils;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public class Gen {

    public static String getString(int length) {
        if(length < 0) {
            throw new IllegalArgumentException("Length must be positive value");
        } else {
            String chars = "aeiouybcdfghjklmnpqrstvwxz";
            StringBuilder str = new StringBuilder(length);

            for(int i = 0; i < length; ++i) {
                str.append(chars.charAt(getInt(chars.length())));
            }

            return str.toString();
        }
    }

    public static int getInt(int max) {
        return (int)(Math.random() * (double)max);
    }
}