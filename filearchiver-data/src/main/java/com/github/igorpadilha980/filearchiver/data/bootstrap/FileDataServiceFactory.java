package com.github.igorpadilha980.filearchiver.data.bootstrap;

import com.github.igorpadilha980.filearchiver.data.FileDataService;

import java.util.Properties;

/**
 * Provide a interface to bootstrap and integrate implementations
 * of this module with others applications
 * <p>
 * {@code FileDataServiceFactory} implementations must have a no-args
 * constructor and provide it as a service
 *
 * @author Igor Padilha
 */
public interface FileDataServiceFactory {

    public FileDataService newFileDataService(Properties serviceConfig);

}
