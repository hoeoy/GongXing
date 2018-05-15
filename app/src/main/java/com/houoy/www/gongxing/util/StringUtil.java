package com.houoy.www.gongxing.util;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static boolean isCellphone(String str) {
        Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$");
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static String byteToM(Long byteNum) {
        if (byteNum == null) {
            return "0";
        }

        DecimalFormat df = new DecimalFormat("0.00");
        String s = df.format((double) byteNum / 1048576);
        return s;
    }

    //是否
    public static Boolean isEmpty(String s) {
        if (s == null || s.trim().equals("")) {
            return true;
        }
        return false;
    }

    //字符串转为16进制
    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    //转换十六进制编码为字符串
    public static String toStringHex(String s) {
        if ("0x".equals(s.substring(0, 2))) {
            s = s.substring(2);
        }
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            s = new String(baKeyword, "utf-8");//UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    public static String getEPCValue(String readvalue) {
        readvalue = toStringHex(readvalue);
        int ix = -1;

        for (int i = readvalue.length() - 1; i > 0; i--) {
            if (readvalue.charAt(i) != 'f') {
                ix = i;
                break;
            }
        }
        String ss = readvalue.substring(0, ix + 1);
        return ss;
    }
}
