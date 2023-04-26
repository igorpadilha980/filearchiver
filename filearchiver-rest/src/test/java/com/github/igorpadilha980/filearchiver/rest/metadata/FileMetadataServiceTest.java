package com.github.igorpadilha980.filearchiver.rest.metadata;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileMetadataServiceTest {

    @Captor
    private ArgumentCaptor<FileMetadataModel> captor;

    @Mock
    private FileMetadataRepository repository;

    @InjectMocks
    private FileMetadataService service;

    @Test
    public void should_store_file_metadata_with_time() {
        FileMetadata data = new FileMetadata(null, UUID.randomUUID(), "file", "plain/text", "some file", null);
        when(repository.save(any())).thenReturn(data.toModel());

        service.store(data);

        verify(repository).save(captor.capture());
        FileMetadataModel saved = captor.getValue();

        assertEquals(data.sourceId(), saved.getSourceId());
        assertEquals(data.fileName(), saved.getFileName());
        assertEquals(data.fileMime(), saved.getFileMime());
        assertEquals(data.description(), saved.getDescription());
    }

    @Test
    public void should_return_stored_metadata() {
        UUID id = UUID.randomUUID();
        FileMetadataModel data = new FileMetadataModel(UUID.randomUUID(), UUID.randomUUID(), "fileName", "test/mime", "description", ZonedDateTime.now());
        when(repository.findById(id)).thenReturn(Optional.of(data));

        FileMetadata result = service.get(id);

        assertEquals(result, FileMetadata.fromModel(data));
    }

    @Test
    public void should_delete_metadata() {
        UUID id = UUID.randomUUID();
        FileMetadataModel data = mock(FileMetadataModel.class);

        when(repository.findById(id)).thenReturn(Optional.of(data));

        service.delete(id);

        verify(repository).delete(data);
    }

    @Test
    public void throws_exception_if_get_non_existing_metadata() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(FileMetadataNotFoundException.class, () -> {
            service.get(UUID.randomUUID());
        });
    }

    @Test
    public void throws_exception_if_delete_non_existing_metadata() {
        when(repository.findById(any())).thenReturn(Optional.empty());

        assertThrows(FileMetadataNotFoundException.class, () -> {
            service.delete(UUID.randomUUID());
        });
    }
}
