package com.github.igorpadilha980.filearchiver.data.filesystem;

import com.github.igorpadilha980.filearchiver.data.FileDataService;
import com.github.igorpadilha980.filearchiver.data.bootstrap.FileDataServiceFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static com.github.igorpadilha980.filearchiver.data.filesystem.FilesystemDataServiceConfiguration.STORAGE_DIRECTORY;
import static com.github.igorpadilha980.filearchiver.data.filesystem.FilesystemDataServiceConfiguration.STORAGE_GENERATE;

public class FilesystemDataServiceFactory implements FileDataServiceFactory {

    @Override
    public FileDataService newFileDataService(Properties serviceConfig) {
        String storageDirectory = serviceConfig.getProperty(STORAGE_DIRECTORY.property());

        if (storageDirectory == null)
            throw new IllegalArgumentException("null is invalid value for configuration " + STORAGE_DIRECTORY.property());

        Path directoryPath = Path.of(storageDirectory);
        generateStorage(directoryPath, serviceConfig);

        ManagedDirectory managedDirectory = new ManagedDirectory(directoryPath);

        return new FilesystemDataService(managedDirectory);
    }

    private void generateStorage(Path directory, Properties config) {
        String propertyValue = config.getProperty(STORAGE_GENERATE.property());
        boolean generate = Boolean.parseBoolean(propertyValue);

        System.out.println("generate: %s".formatted(generate));

        try {
            if (generate && Files.notExists(directory)) {
                Files.createDirectories(directory);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
