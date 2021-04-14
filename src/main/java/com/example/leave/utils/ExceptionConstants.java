package com.example.leave.utils;

public class ExceptionConstants {
    // Errors
    public static final String ERRORS = "Errors";
    public static final String SUCCESS = "Success";

    // Employee Exception Constant
    public static final String EMPLOYEE_RECORD_NOT_FOUND = "Employee doesn't exists";
    public static final String EMPLOYEE_USERNAME_NOT_VALID = "Employee username not valid";
    public static final String OLD_PASSWORD_DOESNT_MATCH = "Old Password Doesn't Match";
    public static final String EMPLOYEE_SUPERVISOR_MISMATCH = "Employee supervisor mismatch";

    // Leave Exception Constant
    public static final String EMPLOYEE_LEAVE_RECORD_NOT_FOUND = "Employee leave record not found";
    public static final String EMPLOYEE_LEAVE_ACTION_ALREADY_TAKEN = "Action already taken on this leave request";


    // LeavePolicy Exception Constant
    public static final String LEAVE_TYPE_RECORD_NOT_FOUND = "Leave type not found";
    public static final String LEAVE_TYPE_NAME_INVALID = "Leave type name invalid";
    public static final String EFFECTIVE_TIME_INVALID = "Policy's effective time invalid";

    // Duration leave Exception Constant
    public static final String LEAVE_DURATION_INVALID = "Leave duration invalid";
    public static final String LEAVE_DURATION_TIME_OUT = "LEAVE DURATION TIMEOUT";
    public static final String DATE_LEAVE_EXIST = "Date leave is exist";
    public static final String DATE_LEAVE_ILLEGAL = "Date leave invalid";
    public static final String NOT_WORKING_TIME = "Not working time";

    // Leave Application Exception Constant

    // Utils error
    public static final String DATE_CANT_BE_PARSED = "Date can't be parsed";

}
