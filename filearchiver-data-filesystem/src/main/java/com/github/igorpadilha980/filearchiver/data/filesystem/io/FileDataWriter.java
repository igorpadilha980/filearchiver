package com.github.igorpadilha980.filearchiver.data.filesystem.io;

import com.github.igorpadilha980.filearchiver.data.FileDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class FileDataWriter {

    public void write(FileDataSource dataSource, Path filePath) throws IOException {
        Objects.requireNonNull(dataSource);
        Objects.requireNonNull(filePath);

        InputStream stream = dataSource.inputStream();

        if(stream == null)
            throw new IllegalArgumentException("data source stream must not be null");

        try(stream) {
            Files.copy(stream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

}
