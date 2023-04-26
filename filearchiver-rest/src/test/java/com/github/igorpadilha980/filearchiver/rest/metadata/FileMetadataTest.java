package com.github.igorpadilha980.filearchiver.rest.metadata;

import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileMetadataTest {

    private void checkEquals(FileMetadata metadata, FileMetadataModel model) {
        assertEquals(metadata.id(), model.getId());
        assertEquals(metadata.sourceId(), model.getSourceId());
        assertEquals(metadata.fileName(), model.getFileName());
        assertEquals(metadata.fileMime(), model.getFileMime());
        assertEquals(metadata.description(), model.getDescription());
        assertEquals(metadata.storageDate(), model.getStorageDate());
    }

    @Test
    public void throws_exception_if_invalid_null_parameters_on_constructor() {
        assertThrows(NullPointerException.class, () -> {
            new FileMetadata(null, null, null, null, null, null);
        });
    }

    @Test
    public void should_convert_to_model() {
        FileMetadata metadata = new FileMetadata(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "test-file-name",
                "test/mime",
                "test file description",
                ZonedDateTime.now()
        );

        FileMetadataModel model = metadata.toModel();

        checkEquals(metadata, model);
    }

    @Test
    public void should_convert_from_model() {
        FileMetadataModel model = new FileMetadataModel(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "test file name",
                "test/mime",
                "some test file description",
                ZonedDateTime.now()
        );

        FileMetadata metadata = FileMetadata.fromModel(model);

        checkEquals(metadata, model);
    }
}
