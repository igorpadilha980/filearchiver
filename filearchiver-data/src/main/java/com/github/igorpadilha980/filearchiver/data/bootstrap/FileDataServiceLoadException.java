package com.github.igorpadilha980.filearchiver.data.bootstrap;

/**
 * Thrown by {@link FileDataServiceLoader} when fail to load and create
 * and instance of {@link com.github.igorpadilha980.filearchiver.data.FileDataService}
 *
 * @author Igor Padilha
 *
 */
public class FileDataServiceLoadException extends RuntimeException {

    FileDataServiceLoadException(String message, Throwable cause) {
        super(message, cause);
    }

}
