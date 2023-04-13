package com.github.igorpadilha980.filearchiver.data.filesystem;

import com.github.igorpadilha980.filearchiver.data.filesystem.io.FileDataWriter;
import com.github.igorpadilha980.filearchiver.data.filesystem.test.FileTestUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ManagedFileTest {

    private ManagedFile managedFile;
    private Path tempFile;
    private UUID fileId;

    @BeforeEach
    public void prepareForTest() throws IOException {
        tempFile = FileTestUtil.createTempFile();
        fileId = UUID.randomUUID();

        managedFile = new ManagedFile(fileId, tempFile, new FileDataWriter());
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
        String dataToWrite = "some data to test";

        managedFile.write(FileTestUtil.sourceFromString(dataToWrite));

        String writtenData = Files.readString(tempFile);
        assertEquals(writtenData, dataToWrite);
    }

    @Test
    public void throws_exception_if_null_parameter_on_constructor() {
        assertThrows(NullPointerException.class, () -> {
           new ManagedFile(null, null, null);
        });
    }

    @Test
    public void should_return_true_if_file_is_same() {
        ManagedFile m0 = new ManagedFile(fileId, tempFile, new FileDataWriter());


        assertTrue(managedFile.isSame(m0));
    }

    @Test
    public void should_return_false_if_file_is_different() {
        FileDataWriter writerMock = Mockito.mock(FileDataWriter.class);

        ManagedFile m1 = new ManagedFile(UUID.randomUUID(), Path.of("."), writerMock);

        assertFalse(managedFile.isSame(m1));
    }
}
