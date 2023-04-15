package com.github.igorpadilha980.filearchiver.data.filesystem;

import com.github.igorpadilha980.filearchiver.data.FileDataSource;
import com.github.igorpadilha980.filearchiver.data.FileServiceException;
import com.github.igorpadilha980.filearchiver.data.filesystem.test.FileTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilesystemDataServiceTest {

    @Mock
    private ManagedDirectory directory;

    @InjectMocks
    private FilesystemDataService service;

    @Test
    public void should_create_and_store_data_into_file() throws FileServiceException, IOException {
        UUID id = UUID.randomUUID();
        ManagedFile fileMock = Mockito.mock(ManagedFile.class);

        when(fileMock.fileId()).thenReturn(id);
        when(directory.newFile()).thenReturn(fileMock);

        UUID savedFileId = service.storeData(FileTestUtil.randomDataSource());

        verify(fileMock, times(1)).write(any());
        assertEquals(id, savedFileId);
    }

    @Test
    public void throws_exception_if_fail_to_write_data_to_file() throws IOException {
        ManagedFile fileMock = Mockito.mock(ManagedFile.class);
        doThrow(IOException.class).when(fileMock).write(any());

        when(directory.newFile()).thenReturn(fileMock);

        assertThrows(FileServiceException.class, () -> {
           service.storeData(FileTestUtil.randomDataSource());
        });
    }

    @Test
    public void throws_exception_if_fail_to_create_file() throws IOException {
        when(directory.newFile()).thenThrow(IOException.class);

        assertThrows(FileServiceException.class, () -> {
           service.storeData(FileTestUtil.randomDataSource());
        });
    }
}
