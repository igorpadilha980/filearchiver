package com.github.igorpadilha980.filearchiver.rest.metadata;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public record FileMetadata(UUID id, UUID sourceId, String fileName, String fileMime, String description,
                           ZonedDateTime storageDate) {

    public FileMetadata {
        Objects.requireNonNull(fileName);
        Objects.requireNonNull(fileMime);
        Objects.requireNonNull(description);
    }

    static FileMetadata fromModel(FileMetadataModel model) {
        return new FileMetadata(model.getId(), model.getSourceId(), model.getFileName(), model.getFileMime(), model.getDescription(), model.getStorageDate());
    }

    FileMetadataModel toModel() {
        return new FileMetadataModel(id, sourceId, fileName, fileMime, description, storageDate);
    }

}
