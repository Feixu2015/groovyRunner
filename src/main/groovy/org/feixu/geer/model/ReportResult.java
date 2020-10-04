package org.feixu.geer.model;

public class ReportResult {
    private String message;
    private Boolean isSuccess;

    public ReportResult() {
        isSuccess = false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }
}
