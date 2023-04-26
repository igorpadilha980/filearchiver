package com.github.igorpadilha980.filearchiver.rest.metadata;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface FileMetadataRepository extends CrudRepository<FileMetadataModel, UUID> {

}
