package com.github.igorpadilha980.filearchiver.rest.archive;

import com.github.igorpadilha980.filearchiver.data.FileDataService;
import com.github.igorpadilha980.filearchiver.data.FileDataSource;
import com.github.igorpadilha980.filearchiver.data.FileServiceException;
import com.github.igorpadilha980.filearchiver.rest.metadata.FileMetadata;
import com.github.igorpadilha980.filearchiver.rest.metadata.FileMetadataNotFoundException;
import com.github.igorpadilha980.filearchiver.rest.metadata.FileMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class ArchiveService {

    private final FileMetadataService metadataService;
    private final FileDataService dataService;

    @Autowired
    public ArchiveService(FileMetadataService metadataService, FileDataService dataService) {
        this.metadataService = metadataService;
        this.dataService = dataService;
    }

    private FileMetadata metadataOrThrows(UUID archiveId) {
        try {
            return metadataService.get(archiveId);

        } catch (FileMetadataNotFoundException e) {
            throw new ArchiveNotFoundException("archive not found", e);
        }
    }

    private UUID storeDataOrThrow(InputStream source) {
        FileDataSource dataSource = () -> source;

        try {
            return dataService.storeData(dataSource);

        } catch (FileServiceException e) {
            throw new ArchiveServiceIncompleteOperationException("could not store archive data", e);
        }
    }

    @Transactional
    public FileInfo archive(FileInfo fileInfo, InputStream source) {
        UUID sourceId = storeDataOrThrow(source);

        FileMetadata metadata = fileInfo.toMetadata(sourceId);
        FileMetadata savedMetadata = metadataService.store(metadata);

        return FileInfo.fromMetadata(savedMetadata);
    }

    public FileInfo info(UUID archiveId) {
        FileMetadata archiveMetadata = metadataOrThrows(archiveId);

        return FileInfo.fromMetadata(archiveMetadata);
    }

    public InputStream data(UUID archiveId) {
        FileMetadata metadata = metadataOrThrows(archiveId);

        try {
            FileDataSource dataSource = dataService.dataFrom(metadata.sourceId());
            return dataSource.inputStream();

        } catch (FileServiceException | IOException e) {
            throw new ArchiveServiceIncompleteOperationException("could not read archive data", e);
        }
    }

    @Transactional
    public void delete(UUID archiveId) {
        FileMetadata metadata = metadataOrThrows(archiveId);

        try {
            metadataService.delete(metadata.id());
            dataService.deleteSource(metadata.sourceId());

        } catch (FileServiceException e) {
            throw new ArchiveServiceIncompleteOperationException("fail to complete archive deletion", e);
        }
    }
}
