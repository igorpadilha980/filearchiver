package com.github.igorpadilha980.filearchiver.data.filesystem;

import com.github.igorpadilha980.filearchiver.data.FileDataSource;
import com.github.igorpadilha980.filearchiver.data.filesystem.io.FileDataWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

class ManagedFile {

    private final UUID id;
    private final Path filePath;

    private final FileDataWriter fileWriter;

    public ManagedFile(UUID id, Path filePath, FileDataWriter fileWriter) {
        this.id = id;
        this.filePath = filePath;
        this.fileWriter = fileWriter;
    }

    void write(FileDataSource data) throws IOException {
        fileWriter.write(data, filePath);
    }

    public UUID fileId() {
        return id;
    }

}
