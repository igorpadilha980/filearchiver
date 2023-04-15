package com.github.igorpadilha980.filearchiver.data.filesystem.io;

import com.github.igorpadilha980.filearchiver.data.FileDataSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class FileDataReader {

    public FileDataSource read(Path path) throws IOException {
        Objects.requireNonNull(path);

        if (Files.notExists(path)) {
            throw new IllegalArgumentException("file path to read must exist");
        } else if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("path to be read must indicate a file");
        }

        return () -> Files.newInputStream(path);
    }

}
