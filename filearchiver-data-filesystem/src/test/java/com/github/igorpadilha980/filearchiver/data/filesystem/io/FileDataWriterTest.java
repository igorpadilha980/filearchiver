package com.github.igorpadilha980.filearchiver.data.filesystem.io;

import com.github.igorpadilha980.filearchiver.data.FileDataSource;
import com.github.igorpadilha980.filearchiver.data.filesystem.test.FileTestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileDataWriterTest {

    private Path testFile;
    private FileDataWriter fileWriter;

    @BeforeEach
    public void prepareForTest() throws IOException {
        testFile = FileTestUtil.createTempFile();
        fileWriter = new FileDataWriter();
    }

    @AfterEach
    public void cleanTest() throws IOException {
        Files.deleteIfExists(testFile);
    }

    private static FileDataSource sourceFromString(String text) {
        return () -> new ByteArrayInputStream(text.getBytes());
    }

    @Test
    public void should_save_data_into_file() throws IOException {
        String dataSaved = "some text for test";
        FileDataSource source = FileTestUtil.sourceFromString(dataSaved);

        fileWriter.write(source, testFile);

        String writtenData = Files.readString(testFile);
        assertEquals(dataSaved, writtenData);
    }

    @Test
    public void fail_to_write_if_null_parameters() {
        assertThrows(NullPointerException.class, () -> {
           fileWriter.write(null, null);
        });
    }

    @Test
    public void fail_to_write_if_invalid_data_source_stream() {
        assertThrows(IllegalArgumentException.class, () -> {
           fileWriter.write(() -> null, testFile);
        });
    }
}
