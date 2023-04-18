package com.github.igorpadilha980.filearchiver.data;

/**
 * Thrown when an {@link FileDataService} operation failed
 *
 * @author Igor Padilha
 */
public class FileServiceException extends Exception {

    private static final long serialVersionUID = -6425041008717264025L;

    public FileServiceException(String message) {
        super(message);
    }

    public FileServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
