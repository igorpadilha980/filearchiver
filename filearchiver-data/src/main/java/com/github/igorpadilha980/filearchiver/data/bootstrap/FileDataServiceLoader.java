package com.github.igorpadilha980.filearchiver.data.bootstrap;

import com.github.igorpadilha980.filearchiver.data.FileDataService;

import java.util.Objects;
import java.util.Properties;

/**
 * Provides a implementation independent way for instantiating a
 * {@link FileDataService}
 * <p>
 * A consumer can instantiate it by calling the default constructor, and
 * load a available data service with {@link #load()} method.
 * <p>
 * To customize the generated instance is possible to pass configuration parameters
 * via {@link #configure(Properties)}
 *
 * @see #FileDataServiceLoader()
 *
 * @author Igor Padilha
 *
 */
public class FileDataServiceLoader {

    private final FileDataServiceFactoryResolver factoryResolver;
    private Properties serviceConfiguration;

    /**
     * Create a default {@code FileDataServiceLoader} with
     * empty configurations
     */
    public FileDataServiceLoader() {
        this(new FileDataServiceFactoryResolver());
    }

    /**
     * Created primarily for tests
     *
     * @param factoryResolver injected dependency used by instance
     */
    protected FileDataServiceLoader(FileDataServiceFactoryResolver factoryResolver) {
        this.factoryResolver = factoryResolver;
        this.serviceConfiguration = new Properties();
    }

    /**
     * Properties used for configuration and generation of {@link FileDataService}
     *
     * @param serviceConfiguration service configuration properties
     */
    public void configure(Properties serviceConfiguration) {
        this.serviceConfiguration = serviceConfiguration;
    }

    /**
     * Loads and instantiate an implementation of {@link FileDataService} using
     * the current configuration of this loader
     *
     * @see #configure(Properties)
     *
     * @return new service instance
     */
    public FileDataService load() {
        FileDataServiceFactory factory = factoryResolver.resolveImplementation();

        try {
            FileDataService loadedService = factory.newFileDataService(serviceConfiguration);

            return Objects.requireNonNull(loadedService, "fail to build service, loaded service must not be null");

        } catch (Exception e) {
            throw new FileDataServiceLoadException("could not load file data service", e);
        }
    }

}
