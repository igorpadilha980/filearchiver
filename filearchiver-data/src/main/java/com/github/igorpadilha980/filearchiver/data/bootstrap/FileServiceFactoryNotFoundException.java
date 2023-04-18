package com.github.igorpadilha980.filearchiver.data.bootstrap;

/**
 * Thrown when {@link FileDataServiceFactoryResolver} could not find any
 * implementation of {@link FileDataServiceFactory} provided on classpath or
 * modulepath
 *
 * @author Igor Padilha
 */
public class FileServiceFactoryNotFoundException extends RuntimeException {

    public FileServiceFactoryNotFoundException(String message) {
        super(message);
    }

}
