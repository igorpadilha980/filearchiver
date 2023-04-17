package com.github.igorpadilha980.filearchiver.data.filesystem;

import com.github.igorpadilha980.filearchiver.data.filesystem.io.FileDataReader;
import com.github.igorpadilha980.filearchiver.data.filesystem.io.FileDataWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

class ManagedDirectory {

    private Path directoryPath;

    ManagedDirectory(Path directoryPath) {
        Objects.requireNonNull(directoryPath);

        if(Files.notExists(directoryPath) || !Files.isDirectory(directoryPath))
            throw new IllegalArgumentException("directoryPath must exist and be a directory");

        this.directoryPath = directoryPath;
    }

    private Path filePathFrom(UUID fileId) {
        String fileName = fileId.toString();

        return directoryPath.resolve(fileName);
    }

    private ManagedFile buildFile(UUID fileId, Path filePath) {
        return new ManagedFile(fileId, filePath, new FileDataWriter(), new FileDataReader());
    }

    public ManagedFile newFile() throws IOException {
        UUID fileId = UUID.randomUUID();
        Path filePath = filePathFrom(fileId);

        Files.createFile(filePath);

        return buildFile(fileId, filePath);
    }

    public Optional<ManagedFile> findFile(UUID fileId) {
        Path filePath = filePathFrom(fileId);

        ManagedFile file = null;

        if(Files.exists(filePath))
            file = buildFile(fileId, filePath);

        return Optional.ofNullable(file);
    }
}
