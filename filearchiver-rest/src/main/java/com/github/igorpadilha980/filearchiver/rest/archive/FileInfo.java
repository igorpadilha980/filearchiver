package com.github.igorpadilha980.filearchiver.rest.archive;

import com.github.igorpadilha980.filearchiver.rest.metadata.FileMetadata;

import java.time.ZonedDateTime;
import java.util.UUID;

public record FileInfo(UUID archiveId, String name, String mime, String description, ZonedDateTime storageDate) {

    static FileInfo fromMetadata(FileMetadata metadata) {
        return new FileInfo(metadata.id(), metadata.fileName(), metadata.fileMime(), metadata.description(), metadata.storageDate());
    }

    FileMetadata toMetadata(UUID sourceId) {
        return new FileMetadata(archiveId, sourceId, name, mime, description, storageDate);
    }

}
