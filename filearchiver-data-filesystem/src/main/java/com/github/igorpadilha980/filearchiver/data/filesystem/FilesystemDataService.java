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

        } catch (IOException e) {
            throw new FileServiceException("could not store data into filesystem file", e);
        }

        return file.fileId();
    }

    private ManagedFile fileOrThrow(UUID fileId, String exceptionMessage) throws FileServiceException {
        Optional<ManagedFile> searchResult = managedDirectory.findFile(fileId);

        return searchResult.orElseThrow(() -> new FileServiceException(exceptionMessage));
    }

    @Override
    public FileDataSource dataFrom(UUID sourceId) throws FileServiceException {
        ManagedFile sourceFile = fileOrThrow(sourceId, "could not read file, file not found");

        try {
            return sourceFile.read();
        } catch (IOException e) {
            throw new FileServiceException("fail to read file");
        }
    }

    @Override
    public void deleteSource(UUID sourceId) throws FileServiceException {
        ManagedFile sourceFile = fileOrThrow(sourceId, "could not delete source, file not found");

        try {
            sourceFile.delete();
        } catch (IOException e) {
            throw new FileServiceException("could not delete source", e);
        }
    }
}
