package com.github.igorpadilha980.filearchiver.data;

import java.util.UUID;

/**
 * Access a service to manage file data
 *
 * @author Igor Padilha
 */
public interface FileDataService {

    /**
     * Reads all data from {@code fileSource} and stores
     * it on the service. Should return a unique id referencing
     * the stored file
     *
     * @param fileSource provide data for storage file
     * @return stored file identifier
     * @throws FileServiceException on fail to store file
     */
    UUID storeData(FileDataSource fileSource) throws FileServiceException;

    /**
     * Returns a {@link FileDataSource} instance for
     * retriveing data from the file with the same {@code sourceId}
     *
     * @param sourceId resource file id
     * @return a resource poiting to the requested file
     * @throws FileServiceException on fail to retrieve file data
     * @see #storeData(FileDataSource)
     */
    FileDataSource dataFrom(UUID sourceId) throws FileServiceException;

    /**
     * Remove file refered by {@code sourceId} from storage
     *
     * @param sourceId file id
     * @throws FileServiceException on fail to delete file data
     * @see #storeData(FileDataSource)
     */
    void deleteSource(UUID sourceId) throws FileServiceException;

}
