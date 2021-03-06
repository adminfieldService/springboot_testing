/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author newbiecihuy
 */
public class Util {

    public static String setSHA256(final String input) throws UnsupportedEncodingException {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input.getBytes("UTF-8"));
            final byte[] messageDigest = md.digest();
            final BigInteger bigInt = new BigInteger(1, messageDigest);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String setMD5(final String input) throws UnsupportedEncodingException {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes("UTF-8"));
            final byte[] messageDigest = md.digest();
            final BigInteger bigInt = new BigInteger(1, messageDigest);
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String upperCaseFirst(final String input) {
        final char[] array = input.toCharArray();
        array[0] = Character.toUpperCase(array[0]);
        return new String(array);
    }

    public static String cekEmail(final String input) throws UnsupportedEncodingException {
        final String domain = "csasolution.com";
        final String[] tokens = input.split("@");
        String kata1 = null;
        String kata2 = null;
        final String isiEmail = null;
        for (int j = 0; j < tokens.length; ++j) {
            if (tokens[j].equals(domain)) {
                kata1 = tokens[j];
            } else {
                kata2 = tokens[j];
            }
        }
        return isiEmail;
    }

    public static String getTokenString() {
        final String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
        final StringBuilder salt = new StringBuilder(4);
        final Random rnd = new Random();
        while (salt.length() < 3) {
            final int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        final String saltStr = salt.toString();
        return saltStr;
    }

    public static String getPasswordString() {
        final String SALTCHARS = "abcdefghijklmnopqrstuvwxyz1234567890";
        final StringBuilder salt = new StringBuilder(5);
        final Random rnd = new Random();
        while (salt.length() < 6) {
            final int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        final String saltStr = salt.toString();
        return saltStr;
    }

    public static String setMonth(String input) {
        final String s = input;
        switch (s) {
            case "1":
            case "01": {
                input = "I";
                break;
            }
            case "2":
            case "02": {
                input = "II";
                break;
            }
            case "3":
            case "03": {
                input = "III";
                break;
            }
            case "4":
            case "04": {
                input = "IV";
                break;
            }
            case "5":
            case "05": {
                input = "V";
                break;
            }
            case "6":
            case "06": {
                input = "VI";
                break;
            }
            case "7":
            case "07": {
                input = "VII";
                break;
            }
            case "8":
            case "08": {
                input = "VIII";
                break;
            }
            case "9":
            case "09": {
                input = "IX";
                break;
            }
            case "10": {
                input = "X";
                break;
            }
            case "11": {
                input = "XI";
                break;
            }
            case "12": {
                input = "XII";
                break;
            }
            default: {
                input = "";
                break;
            }
        }
        return input;
    }

    public static String setNumber(final String param) {
        String isiParam = null;
        if (param.length() == 1) {
            isiParam = "000" + param;
        }
        if (param.length() == 2) {
            isiParam = "00" + param;
        }
        if (param.length() == 3) {
            isiParam = "0" + param;
        }
        if (param.length() == 4) {
            isiParam = param;
        }
        return isiParam;
    }

    /*
     *source https://howtodoinjava.com/regex/java-regex-validate-email-address/
     *
     */
    public static boolean validation(String param) {
        final String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(param);
        return matcher.matches();
//    	String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(param);
//        if (matcher.equals(true)) {
//            return true;
//        } else {
//            return false;
//        }
    }
}
