package com.github.igorpadilha980.filearchiver.rest.archive;

import com.github.igorpadilha980.filearchiver.rest.metadata.FileMetadata;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileInfoTest {

    private void checkEquals(FileInfo info, FileMetadata metadata) {
        assertEquals(info.archiveId(), metadata.id());
        assertEquals(info.name(), metadata.fileName());
        assertEquals(info.mime(), metadata.fileMime());
        assertEquals(info.description(), metadata.description());
        assertEquals(info.storageDate(), metadata.storageDate());
    }

    @Test
    public void should_convert_to_metadata() {
        FileInfo info = new FileInfo(
                UUID.randomUUID(),
                "test file name",
                "test/mime",
                "test file description",
                ZonedDateTime.now()
        );

        FileMetadata metadata = info.toMetadata(UUID.randomUUID());
        checkEquals(info, metadata);
    }

    @Test
    public void should_create_from_metadata() {
        FileMetadata metadata = new FileMetadata(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "test file name",
                "test/mime",
                "test file description",
                ZonedDateTime.now()
        );

        FileInfo info = FileInfo.fromMetadata(metadata);
        checkEquals(info, metadata);
    }
}
