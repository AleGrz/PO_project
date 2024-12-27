package agh.darwinworld.model;

public class UserFriendlyException extends Exception {
    private final String header;

    public UserFriendlyException(String header, String message) {
        super(message);
        this.header = header;
    }

    public String getHeader() {
        return header;
    }
}
