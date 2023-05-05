package com.github.igorpadilha980.filearchiver.rest.controller;

import com.github.igorpadilha980.filearchiver.rest.archive.ArchiveNotFoundException;
import com.github.igorpadilha980.filearchiver.rest.archive.ArchiveService;
import com.github.igorpadilha980.filearchiver.rest.archive.ArchiveServiceIncompleteOperationException;
import com.github.igorpadilha980.filearchiver.rest.archive.FileInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/archive")
public class ArchiveController {

    @Autowired
    private ArchiveService archiveService;

    private UUID parseId(String idString) {
        try {
            return UUID.fromString(idString);
        } catch (IllegalArgumentException e) {
            throw new InvalidIdFormatException("invalid archive id format '%s'".formatted(idString));
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileInfo> uploadArchive(@ModelAttribute ArchiveUploadRequest request) throws IOException {
        FileInfo fileInfo = new FileInfo(null, request.fileName, request.description, request.file.getContentType(), null);
        InputStream dataStream = request.file.getInputStream();

        FileInfo storedData = archiveService.archive(fileInfo, dataStream);

        return ResponseEntity.ok(storedData);
    }

    @GetMapping("/{archive}")
    public ResponseEntity<FileInfo> archiveInfo(@PathVariable("archive") String idString) {
        UUID archiveId = parseId(idString);
        FileInfo fileInfo = archiveService.info(archiveId);

        return ResponseEntity.ok(fileInfo);
    }

    @GetMapping(
            path = "/{archive}/data",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Resource> streamData(@PathVariable("archive") String idString) {
        UUID archiveId = parseId(idString);
        InputStream dataStream = archiveService.data(archiveId);

        InputStreamResource resource = new InputStreamResource(dataStream);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{archive}")
    public ResponseEntity deleteArchive(@PathVariable("archive") String idString) {
        UUID archiveId = parseId(idString);
        archiveService.delete(archiveId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private ResponseEntity fromError(HttpStatus status, Exception exception) {
        ErrorResponse error = new ErrorResponse();

        error.status = status.value();
        error.message = exception.getMessage();

        return ResponseEntity
                .status(status)
                .body(error);
    }

    @ExceptionHandler(ArchiveNotFoundException.class)
    public ResponseEntity archiveNotFound(ArchiveNotFoundException exception) {
        return fromError(HttpStatus.NOT_FOUND, exception);
    }

    @ExceptionHandler(ArchiveServiceIncompleteOperationException.class)
    public ResponseEntity failToCompleteOperation(ArchiveServiceIncompleteOperationException exception) {
        return fromError(HttpStatus.INTERNAL_SERVER_ERROR, exception);
    }

    @ExceptionHandler({ InvalidIdFormatException.class })
    public ResponseEntity invalidArchiveId(InvalidIdFormatException exception) {
        return fromError(HttpStatus.BAD_REQUEST, exception);
    }

    @Data
    static class ArchiveUploadRequest {
        @NotBlank
        private String fileName;

        @NotBlank
        private String description;

        @NotNull
        private MultipartFile file;
    }

    @Data
    static class ErrorResponse {
        private int status;
        private String message;
    }

    public static class InvalidIdFormatException extends RuntimeException {
        private InvalidIdFormatException(String message) {
            super(message);
        }
    }
}
