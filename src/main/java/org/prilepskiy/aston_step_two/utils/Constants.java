package org.prilepskiy.aston_step_two.utils;

import java.util.regex.Pattern;

public class Constants {
    public final static int CREATE_USER = 1;
    public final static int GET_USER_BY_ID = 2;
    public final static int GET_ALL_USERS = 3;
    public final static int  UPDATE_USER = 4;
    public final static int  DELETE_USER = 5;
    public final static int  EXIT = 0;
    public final static Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    public final static int MIN_NAME_LEN = 2;
    public final static int MAX_NAME_LEN = 100;
    public final static Pattern NAME_PATTERN  = Pattern.compile("^[A-Za-zА-Яа-яЁё\\s'-]+$");
    public final static int MIN_AGE_LEN = 0;
    public final static int MAX_AGE_LEN = 100;
    public final static int MIN_EMAIL_LEN = 6;
    public final static int MAX_EMAIL_LEN = 255;
    public final static int FIRST_INDEX = 0;
    public final static int SHIFT_INDEX = 1;

}
