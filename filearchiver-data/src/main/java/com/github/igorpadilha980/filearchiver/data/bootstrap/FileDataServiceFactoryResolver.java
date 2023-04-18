package com.github.igorpadilha980.filearchiver.data.bootstrap;

import java.util.ServiceLoader;

class FileDataServiceFactoryResolver {

    private final ServiceLoader<FileDataServiceFactory> serviceLoader;

    FileDataServiceFactoryResolver() {
        serviceLoader = ServiceLoader.load(FileDataServiceFactory.class);
    }

    FileDataServiceFactory resolveImplementation() {
        return serviceLoader.findFirst().orElseThrow(() -> {
            return new FileServiceFactoryNotFoundException("file data service factory not provided");
        });
    }

}
