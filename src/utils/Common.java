package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/*
 * Created by demidovskiy-r on 01.06.2015.
 */
public class Common {
    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
            return str;
        }
    }

    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
            return str;
        }
    }
}