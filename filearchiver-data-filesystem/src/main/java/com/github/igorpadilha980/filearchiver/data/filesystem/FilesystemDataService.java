package com.github.igorpadilha980.filearchiver.data.filesystem;

import com.github.igorpadilha980.filearchiver.data.FileDataService;
import com.github.igorpadilha980.filearchiver.data.FileDataSource;
import com.github.igorpadilha980.filearchiver.data.FileServiceException;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class FilesystemDataService implements FileDataService {

    private final ManagedDirectory managedDirectory;

    FilesystemDataService(ManagedDirectory managedDirectory) {
        this.managedDirectory = managedDirectory;
    }

    @Override
    public UUID storeData(FileDataSource fileSource) throws FileServiceException {
        ManagedFile file;

        try {
            file = managedDirectory.newFile();
            file.write(fileSource);

        } catch(IOException e) {
            throw new FileServiceException("could not store data into filesystem file", e);
        }

        return file.fileId();
    }

    @Override
    public FileDataSource dataFrom(UUID sourceId) throws FileServiceException {
        return null;
    }

    @Override
    public void deleteSource(UUID sourceId) throws FileServiceException {
        Optional<ManagedFile> searchResult = managedDirectory.findFile(sourceId);
        ManagedFile sourceFile = searchResult.orElseThrow(() -> new FileServiceException("could not delete source, file not found"));

        try {
            sourceFile.delete();
        } catch(IOException e) {
            throw new FileServiceException("could not delete source", e);
        }
    }
}
