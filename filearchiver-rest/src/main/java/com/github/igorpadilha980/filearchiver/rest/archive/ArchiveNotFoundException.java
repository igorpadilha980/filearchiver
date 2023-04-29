package com.github.igorpadilha980.filearchiver.rest.archive;

public class ArchiveNotFoundException extends RuntimeException {

    ArchiveNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
