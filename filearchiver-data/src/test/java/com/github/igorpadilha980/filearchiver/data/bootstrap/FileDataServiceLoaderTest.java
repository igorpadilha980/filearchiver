package com.github.igorpadilha980.filearchiver.data.bootstrap;

import com.github.igorpadilha980.filearchiver.data.FileDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileDataServiceLoaderTest {

    @Mock
    private FileDataServiceFactory factoryMock;

    @Captor
    private ArgumentCaptor<Properties> propertiesCaptor;

    @Mock
    private FileDataServiceFactoryResolver factoryResolverMock;

    @InjectMocks
    private FileDataServiceLoader serviceLoader;

    @BeforeEach
    public void prepareForTest() {
        when(factoryResolverMock.resolveImplementation()).thenReturn(factoryMock);
    }

    @Test
    public void should_load_file_data_service() {
        when(factoryMock.newFileDataService(any())).thenReturn(mock());

        FileDataService loadedService = serviceLoader.load();

        assertNotNull(loadedService);
    }

    @Test
    public void should_pass_configuration_to_factory_when_load_service() {
        when(factoryMock.newFileDataService(any())).thenReturn(mock());

        Properties testConfiguration = new Properties();
        testConfiguration.put("test.property.name", "some test value");

        serviceLoader.configure(testConfiguration);
        serviceLoader.load();

        verify(factoryMock).newFileDataService(propertiesCaptor.capture());
        assertEquals(testConfiguration, propertiesCaptor.getValue());
    }

    @Test
    public void should_use_empty_configuration_for_load_if_none_provided() {
        when(factoryMock.newFileDataService(any())).thenReturn(mock());

        serviceLoader.load();

        verify(factoryMock).newFileDataService(propertiesCaptor.capture());
        assertEquals(propertiesCaptor.getValue(), new Properties());
    }

    @Test
    public void throws_exception_if_fail_to_build_instance_of_service() {
        when(factoryMock.newFileDataService(any())).thenThrow(RuntimeException.class);

        assertThrows(FileDataServiceLoadException.class, () -> {
            serviceLoader.load();
        });
    }

    @Test
    public void throws_exception_if_factory_return_null_service() {
        when(factoryMock.newFileDataService(any())).thenReturn(null);

        assertThrows(FileDataServiceLoadException.class, () -> {
            serviceLoader.load();
        });
    }
}
