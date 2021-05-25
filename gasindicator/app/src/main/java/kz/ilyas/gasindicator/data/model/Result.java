package kz.ilyas.gasindicator.data.model;

public class Result {
    private String status;
    private Exception exception;

    public Result() {
    }

    public Result(String status, Exception exception) {
        this.status = status;
        this.exception = exception;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
