package com.github.igorpadilha980.filearchiver.data.filesystem;

import com.github.igorpadilha980.filearchiver.data.FileDataService;
import com.github.igorpadilha980.filearchiver.data.bootstrap.FileDataServiceFactory;

import java.nio.file.Path;
import java.util.Properties;

import static com.github.igorpadilha980.filearchiver.data.filesystem.FilesystemDataServiceConfiguration.STORAGE_DIRECTORY;

public class FilesystemDataServiceFactory implements FileDataServiceFactory {

    @Override
    public FileDataService newFileDataService(Properties serviceConfig) {
        String storageDirectory = serviceConfig.getProperty(STORAGE_DIRECTORY.property());

        if (storageDirectory == null)
            throw new IllegalArgumentException("null is invalid value for configuration " + STORAGE_DIRECTORY.property());

        ManagedDirectory managedDirectory = new ManagedDirectory(Path.of(storageDirectory));
        return new FilesystemDataService(managedDirectory);
    }

}
