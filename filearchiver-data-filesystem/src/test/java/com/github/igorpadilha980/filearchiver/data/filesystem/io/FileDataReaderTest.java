package com.github.igorpadilha980.filearchiver.data.filesystem.io;

import com.github.igorpadilha980.filearchiver.data.FileDataSource;
import com.github.igorpadilha980.filearchiver.data.filesystem.test.FileTestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileDataReaderTest {

    private Path testFile;
    private FileDataReader reader;

    @BeforeEach
    public void prepareForTest() throws IOException {
        testFile = FileTestUtil.createTempFile();
        reader = new FileDataReader();
    }

    @AfterEach
    public void cleanTest() throws IOException {
        Files.deleteIfExists(testFile);
    }

    @Test
    public void should_read_data_from_file() throws IOException {
        String testData = "some test data";
        Files.writeString(testFile, testData);

        FileDataSource readSource = reader.read(testFile);

        String readData = null;

        try(InputStream stream = readSource.inputStream()) {
            byte[] bytes = stream.readAllBytes();

            readData = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(bytes)).toString();
        }

        assertEquals(testData, readData);
    }

    @Test
    public void throws_exception_if_read_null_path() {
        assertThrows(NullPointerException.class, () -> {
           reader.read(null);
        });
    }

    @Test
    public void throws_exception_if_read_non_existing_path() {
        assertThrows(IllegalArgumentException.class, () -> {
            Path invalidPath = Path.of(".","some-non-existing-file-path");
            reader.read(invalidPath);
        });
    }

    @Test
    public void thows_exception_if_try_to_read_path_pointing_to_non_file() {
        assertThrows(IllegalArgumentException.class, () -> {
            Path directory = Path.of(".").toAbsolutePath();
            reader.read(directory);
        });
    }
}
