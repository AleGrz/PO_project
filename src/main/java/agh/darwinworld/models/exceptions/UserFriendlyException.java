package agh.darwinworld.models.exceptions;

/**
 * Represents an exception with a user-friendly message and header.
 * Should be used only for displaying error messages for the user.
 */
public class UserFriendlyException extends RuntimeException {
    private final String header;

    /**
     * Constructs a new UserFriendlyException with the specified header and message.
     * @param header a brief description of the error.
     * @param message the detailed message explaining the error.
     */
    public UserFriendlyException(String header, String message) {
        super(message);
        this.header = header;
    }

    /**
     * Returns the header of the exception.
     * @return the header of the exception.
     */
    public String getHeader() {
        return header;
    }
}
