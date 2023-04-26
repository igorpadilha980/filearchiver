package com.github.igorpadilha980.filearchiver.rest.metadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileMetadataService {

    private final FileMetadataRepository repository;

    @Autowired
    FileMetadataService(FileMetadataRepository repository) {
        this.repository = repository;
    }

    public FileMetadata store(FileMetadata metadata) {
        FileMetadataModel model = metadata.toModel();
        model.setStorageDate(ZonedDateTime.now(ZoneOffset.UTC));

        FileMetadataModel saved = repository.save(model);

        return FileMetadata.fromModel(saved);
    }

    public FileMetadata get(UUID metadataId) {
        Optional<FileMetadataModel> result = repository.findById(metadataId);

        FileMetadataModel model = result.orElseThrow(
                () -> new FileMetadataNotFoundException("unable to fetch data from inexistent metadata (id: %s)".formatted(metadataId.toString()))
        );

        return FileMetadata.fromModel(model);
    }

    public void delete(UUID metadataId) {
        FileMetadataModel model = repository.findById(metadataId).orElseThrow(
                () -> new FileMetadataNotFoundException("unable to delete inexistent metadata (id: %s)".formatted(metadataId.toString()))
        );

        repository.delete(model);
    }
}
