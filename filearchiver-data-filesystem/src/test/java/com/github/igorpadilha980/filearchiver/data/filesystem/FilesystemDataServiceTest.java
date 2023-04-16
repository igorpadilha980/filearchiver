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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilesystemDataServiceTest {

    @Mock
    private ManagedDirectory directory;

    @Mock
    private ManagedFile file;

    @InjectMocks
    private FilesystemDataService service;

    @Test
    public void should_create_and_store_data_into_file() throws FileServiceException, IOException {
        UUID id = UUID.randomUUID();

        when(file.fileId()).thenReturn(id);
        when(directory.newFile()).thenReturn(file);

        UUID savedFileId = service.storeData(FileTestUtil.randomDataSource());

        verify(file, times(1)).write(any());
        assertEquals(id, savedFileId);
    }

    @Test
    public void throws_exception_if_fail_to_write_data_to_file() throws IOException {
        doThrow(IOException.class).when(file).write(any());
        when(directory.newFile()).thenReturn(file);

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

    @Test
    public void should_delete_stored_source() throws IOException, FileServiceException {
        UUID mockId = UUID.randomUUID();

        when(directory.findFile(mockId)).thenReturn(Optional.of(file));

        service.deleteSource(mockId);
        verify(file, times(1)).delete();
    }

    @Test
    public void throws_exception_if_try_to_delete_non_existing_source() {
        when(directory.findFile(any())).thenReturn(Optional.empty());

        assertThrows(FileServiceException.class, () -> {
            service.deleteSource(UUID.randomUUID());
        });
    }

    @Test
    public void should_read_data_from_source() throws IOException, FileServiceException {
        UUID fileId = UUID.randomUUID();
        when(directory.findFile(fileId)).thenReturn(Optional.of(file));

        FileDataSource producedSource = service.dataFrom(fileId);
        verify(file, times(1)).read();
    }

    @Test
    public void throws_exception_if_read_non_existing_source() {
        assertThrows(FileServiceException.class, () -> {
            when(directory.findFile(any())).thenReturn(Optional.empty());
            service.dataFrom(UUID.randomUUID());
        });
    }

    @Test
    public void throws_exception_if_fail_to_read_source() throws IOException {
        UUID fileId = UUID.randomUUID();

        when(directory.findFile(fileId)).thenReturn(Optional.of(file));
        when(file.read()).thenThrow(IOException.class);

        assertThrows(FileServiceException.class, () -> {
           service.dataFrom(fileId);
        });
    }
}
