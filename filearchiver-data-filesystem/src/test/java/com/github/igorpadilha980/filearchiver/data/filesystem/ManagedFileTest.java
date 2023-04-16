package com.github.igorpadilha980.filearchiver.data.filesystem;

import com.github.igorpadilha980.filearchiver.data.FileDataSource;
import com.github.igorpadilha980.filearchiver.data.filesystem.io.FileDataReader;
import com.github.igorpadilha980.filearchiver.data.filesystem.io.FileDataWriter;
import com.github.igorpadilha980.filearchiver.data.filesystem.test.FileTestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ManagedFileTest {

    private ManagedFile managedFile;
    private Path tempFile;
    private UUID fileId;

    private FileDataWriter fileWriter;
    private FileDataReader fileReader;

    @BeforeEach
    public void prepareForTest() throws IOException {
        tempFile = FileTestUtil.createTempFile();
        fileId = UUID.randomUUID();

        fileWriter = mock(FileDataWriter.class);
        fileReader = mock(FileDataReader.class);

        managedFile = new ManagedFile(fileId, tempFile, fileWriter, fileReader);
    }

    @AfterEach
    public void cleanAfterTest() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void should_retrieve_same_id() {
        assertEquals(fileId, managedFile.fileId());
    }

    @Test
    public void should_write_to_managed_file() throws IOException {
        FileDataSource testData = FileTestUtil.randomDataSource();
        managedFile.write(testData);

        verify(fileWriter, times(1)).write(any(), any());
    }

    @Test
    public void throws_exception_if_null_parameter_on_constructor() {
        assertThrows(NullPointerException.class, () -> {
           new ManagedFile(null, null, null, null);
        });
    }

    @Test
    public void should_return_true_if_file_is_same() {
        ManagedFile m0 = new ManagedFile(fileId, tempFile, fileWriter, fileReader);

        assertTrue(managedFile.isSame(m0));
    }

    @Test
    public void should_return_false_if_file_is_different() {
        ManagedFile m1 = new ManagedFile(UUID.randomUUID(), Path.of("."), fileWriter, fileReader);

        assertFalse(managedFile.isSame(m1));
    }

    @Test
    public void should_delete_file() throws IOException {
        managedFile.delete();

        boolean fileDeleted = Files.notExists(tempFile);
        assertTrue(fileDeleted);
    }
    
    @Test
    public void should_read_managed_file_content() throws IOException {
        FileDataSource testSource = FileTestUtil.randomDataSource();

        managedFile.read();

        verify(fileReader, times(1)).read(any());
    }
}
