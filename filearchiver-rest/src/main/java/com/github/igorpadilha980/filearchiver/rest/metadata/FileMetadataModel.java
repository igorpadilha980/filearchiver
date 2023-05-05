package com.github.igorpadilha980.filearchiver.rest.metadata;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
class FileMetadataModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = true)
    private UUID sourceId;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileMime;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private ZonedDateTime storageDate;

}
