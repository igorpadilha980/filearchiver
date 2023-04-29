package com.github.igorpadilha980.filearchiver.rest.archive;

import com.github.igorpadilha980.filearchiver.data.FileDataService;
import com.github.igorpadilha980.filearchiver.data.FileDataSource;
import com.github.igorpadilha980.filearchiver.data.FileServiceException;
import com.github.igorpadilha980.filearchiver.rest.metadata.FileMetadata;
import com.github.igorpadilha980.filearchiver.rest.metadata.FileMetadataNotFoundException;
import com.github.igorpadilha980.filearchiver.rest.metadata.FileMetadataService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArchiveServiceTest {

    @Mock
    private FileMetadataService metadataService;

    @Mock
    private FileDataService dataService;

    @InjectMocks
    private ArchiveService archiveService;

    private FileMetadata testMetadata(UUID id, UUID sourceId) {
        return new FileMetadata(id, sourceId, "name", "mime", "description", ZonedDateTime.now());
    }

    private FileMetadata filledMetatadata() {
        return testMetadata(UUID.randomUUID(), UUID.randomUUID());
    }

    @Test
    public void should_archive_file() throws FileServiceException {
        ArgumentCaptor<FileMetadata> captor = ArgumentCaptor.forClass(FileMetadata.class);
        UUID sourceId = UUID.randomUUID();

        when(dataService.storeData(any())).thenReturn(sourceId);
        when(metadataService.store(captor.capture())).thenAnswer((i) -> i.getArgument(0));

        FileInfo fileToSave = new FileInfo(
                null,
                "file name",
                "file/mime",
                "file description",
                null
        );
        archiveService.archive(fileToSave, InputStream.nullInputStream());

        FileMetadata savedMetadata = captor.getValue();

        assertEquals(savedMetadata.sourceId(), sourceId);
        assertEquals(savedMetadata.fileName(), fileToSave.name());
        assertEquals(savedMetadata.fileMime(), fileToSave.mime());
        assertEquals(savedMetadata.description(), fileToSave.description());
    }

    @Test
    public void should_retrive_file_info() {
        UUID id = UUID.randomUUID();

        when(metadataService.get(any())).thenReturn(testMetadata(id, UUID.randomUUID()));

        archiveService.info(id);

        verify(metadataService).get(id);
    }
    
    @Test
    public void should_read_data_from_archive() throws FileServiceException {
        UUID id = UUID.randomUUID();
        UUID sourceId = UUID.randomUUID();

        when(metadataService.get(id)).thenReturn(testMetadata(id, sourceId));
        when(dataService.dataFrom(sourceId)).thenReturn(() -> InputStream.nullInputStream());

        archiveService.data(id);

        verify(dataService).dataFrom(sourceId);
    }

    @Test
    public void should_delete_archive() throws FileServiceException {
        UUID id = UUID.randomUUID();
        UUID sourceId = UUID.randomUUID();

        when(metadataService.get(id)).thenReturn(testMetadata(id, sourceId));

        archiveService.delete(id);

        verify(metadataService).delete(id);
        verify(dataService).deleteSource(sourceId);
    }

    @Test
    public void throws_exception_if_fail_to_save_data() throws FileServiceException {
        doThrow(FileServiceException.class).when(dataService).storeData(any());
        FileInfo fileInfo = FileInfo.fromMetadata(filledMetatadata());

        assertThrows(ArchiveServiceIncompleteOperationException.class, () -> {
           archiveService.archive(fileInfo, InputStream.nullInputStream());
        });
    }

    @Test
    public void throws_exception_if_try_read_non_existent_archive() {
        doThrow(FileMetadataNotFoundException.class).when(metadataService).get(any());

        assertThrows(ArchiveNotFoundException.class, () -> archiveService.info(UUID.randomUUID()));
        assertThrows(ArchiveNotFoundException.class, () -> archiveService.data(UUID.randomUUID()));
        assertThrows(ArchiveNotFoundException.class, () -> archiveService.delete(UUID.randomUUID()));
    }

    @Test
    public void throws_exception_if_fail_to_read_archive_data() throws FileServiceException {
        when(metadataService.get(any())).thenReturn(filledMetatadata());

        FileDataSource faultyDataSource = () -> {
            throw new IOException();
        };
        when(dataService.dataFrom(any())).thenReturn(faultyDataSource);

        assertThrows(ArchiveServiceIncompleteOperationException.class, () -> {
            archiveService.data(UUID.randomUUID());
        });

        doThrow(FileServiceException.class).when(dataService).dataFrom(any());

        assertThrows(ArchiveServiceIncompleteOperationException.class, () -> {
           archiveService.data(UUID.randomUUID());
        });
    }

    @Test
    public void throws_exception_if_fail_to_delete_archive_data() throws FileServiceException {
        when(metadataService.get(any())).thenReturn(filledMetatadata());
        doThrow(FileServiceException.class).when(dataService).deleteSource(any());

        assertThrows(ArchiveServiceIncompleteOperationException.class, () -> {
           archiveService.delete(UUID.randomUUID());
        });
    }
}
