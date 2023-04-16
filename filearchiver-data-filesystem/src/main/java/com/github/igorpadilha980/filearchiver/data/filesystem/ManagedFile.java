package com.github.igorpadilha980.filearchiver.data.filesystem;

import com.github.igorpadilha980.filearchiver.data.FileDataSource;
import com.github.igorpadilha980.filearchiver.data.filesystem.io.FileDataReader;
import com.github.igorpadilha980.filearchiver.data.filesystem.io.FileDataWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

class ManagedFile {

    private final UUID id;
    private final Path filePath;

    private final FileDataWriter fileWriter;
    private final FileDataReader fileReader;

    public ManagedFile(UUID id, Path filePath, FileDataWriter fileWriter, FileDataReader fileReader) {
        this.id = Objects.requireNonNull(id);
        this.filePath = Objects.requireNonNull(filePath);
        this.fileWriter = Objects.requireNonNull(fileWriter);
        this.fileReader = Objects.requireNonNull(fileReader);
    }

    void write(FileDataSource data) throws IOException {
        fileWriter.write(data, filePath);
    }

    public UUID fileId() {
        return id;
    }

    public FileDataSource read() throws IOException {
        return fileReader.read(filePath);
    }

    public void delete() throws IOException {
        Files.delete(filePath);
    }

    boolean isSame(ManagedFile other) {
        return id.equals(other.id) && filePath.equals(other.filePath);
    }

}
