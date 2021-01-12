package ua.training.top.util.exception;

public class ErrorInfo {
    private final String url;
    private final ErrorType errorType;
    private final String[] details;

//    public ErrorInfo(CharSequence url, HttpStatus status, String[] details) {
//        this.url = url.toString();
//        this.status = status;
//        this.details = details;
//    }

    public ErrorInfo(StringBuffer url, ErrorType errorType, String[] details) {
        this.url = url.toString();
        this.errorType = errorType;
        this.details = details;

    }
}
/*public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final String typeMessage;
    private final String[] details;

    public ErrorInfo(CharSequence url, ErrorType type, String typeMessage, String... details) {
        this.url = url.toString();
        this.type = type;
        this.typeMessage = typeMessage;
        this.details = details;
    }*/
