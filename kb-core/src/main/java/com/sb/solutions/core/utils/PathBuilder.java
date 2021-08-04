package com.sb.solutions.core.utils;

/**
 * this utility method is use for chaining folder path for json file or document file
 *
 * @author Sunil Babu Shrestha on 9/6/2019
 */
public class PathBuilder {

    private String basePath;
    private String branchName;
    private String customerName;
    private String customerCitizenship;
    private String loanType;
    // action can be either "new" or "close" or "renew"
    private String action;
    private boolean jsonPath;
    private String memoType;
    private String customerLoanId;


    public PathBuilder(String basePath) {
        this.basePath = basePath;
    }

    private static String getDigitsFromString(String citizenNumber) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < citizenNumber.length(); i++) {
            char c = citizenNumber.charAt(i);
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public PathBuilder withBranch(String branchName) {
        this.branchName = branchName;
        return this;
    }

    public PathBuilder withCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public PathBuilder withCitizenship(String customerCitizenship) {
        this.customerCitizenship = customerCitizenship;
        return this;
    }

    public PathBuilder withLoanType(String loanType) {
        this.loanType = loanType;
        return this;
    }

    public PathBuilder withAction(String action) {
        this.action = action;
        return this;
    }

    public PathBuilder isJsonPath(boolean jsonPath) {
        this.jsonPath = jsonPath;
        return this;
    }

    public PathBuilder withMemoType(String memoType) {
        this.memoType = memoType;
        return this;
    }

    public PathBuilder withCustomerLoanId(String customerLoanId) {
        this.customerLoanId = customerLoanId;
        return this;
    }

    public String build() {
        StringBuilder sb = new StringBuilder(this.basePath);
        return sb.append(branchName).append("/")
            .append(customerName.trim().replace(" ", "_")).append("_")
            .append(getDigitsFromString(customerCitizenship)).append("/")
            .append(loanType.trim().replace(" ", "_")).append("/")
            .append(action).append("/")
            .append(jsonPath ? "json/" : "doc/").toString();
    }

    public String buildAccountOpening() {
        StringBuilder sb = new StringBuilder(this.basePath);
        return sb.append(branchName)
            .append("/")
            .append(customerName.trim().replace(" ", "_"))
            .append("_")
            .append(getDigitsFromString(customerCitizenship)).append("/")
            .append("AccountOpening")
            .append("/")
            .append(jsonPath ? "json/" : "doc/")
            .toString();
    }

    public String buildCreditMemo() {
        StringBuilder sb = new StringBuilder(this.basePath);
        return sb
            .append(branchName)
            .append("/")
            .append(customerName.trim().replace(" ", "_"))
            .append("_")
            .append(getDigitsFromString(customerCitizenship))
            .append("/")
            .append("CreditMemo")
            .append("/")
            .append(customerLoanId)
            .append("/")
            .append(memoType.trim().replace(" ", "_"))
            .append("/")
            .append(jsonPath ? "json/" : "doc/")
            .toString();
    }
}
