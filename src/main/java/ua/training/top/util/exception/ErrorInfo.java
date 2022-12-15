package ua.training.top.util.exception;

public class ErrorInfo {
    private String url;
    private ErrorType errorType;
    private final String[] details;

    public ErrorInfo(StringBuffer url, ErrorType errorType, String... details) {
        this(details);
        this.url = url.toString();
        this.errorType = errorType;
    }
    // use this simpler case
    public ErrorInfo(String... details) {
        this.details = details;
    }
}
