package com.sb.solutions.core.constant;

import com.google.common.collect.ImmutableMap;

import java.util.Map;


/**
 * @author Sunil Babu Shrestha on 7/18/2019
 */
public final class EmailConstant {

    public static final Map<Template, String> MAIL = ImmutableMap.<Template, String>builder()
            .put(Template.RESET_PASSWORD, "/mail/password-reset.html")
            .put(Template.RESET_PASSWORD_SUCCESS, "/mail/password-reset-success.html")
            .put(Template.ELIGIBILITY_ELIGIBLE, "/mail/eligible.html")
            .put(Template.ACCOUNT_OPENING_THANK_YOU, "/mail/account-opening-thank-you.html")
            .put(Template.ONE_TIME_PASSWORD, "/mail/customer-otp.html")
            .put(Template.TEST, "/mail/email-test.html")
            .put(Template.ACCOUNT_OPENING_ACCEPT, "/mail/account-opening-registration-accept")
            .put(Template.ELIGIBILITY_APPROVE, "/mail/eligible-approve")
            .put(Template.LOAN_ACTION, "/mail/loan-action")
            .build();

    private EmailConstant() {
    }


    public enum Template {
        RESET_PASSWORD("Reset Password !!"),
        RESET_PASSWORD_SUCCESS("Password Reset Successful !!"),
        ELIGIBILITY_ELIGIBLE("You are Eligible !!"),
        ELIGIBILITY_APPROVE("Thank You !! Loan request has been approved."),
        ACCOUNT_OPENING_THANK_YOU("Thank You !! Account Opening request is received."),
        ONE_TIME_PASSWORD("One Time Password"),
        TEST("Email Configuration Test"),
        ACCOUNT_OPENING_ACCEPT("Thank You !! Account Opening request has been accepted."),
        LOAN_ACTION("Customer Loan Action Notification !!");

        private final String subject;

        Template(String subject) {
            this.subject = subject;
        }

        public String get() {
            return this.subject;
        }
    }
}
