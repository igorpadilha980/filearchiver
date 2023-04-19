package com.github.igorpadilha980.filearchiver.data.filesystem;

import com.github.igorpadilha980.filearchiver.data.FileDataService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Properties;

import static com.github.igorpadilha980.filearchiver.data.filesystem.FilesystemDataServiceConfiguration.STORAGE_DIRECTORY;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockConstruction;

@ExtendWith(MockitoExtension.class)
public class FilesystemDataServiceFactoryTest {

    private MockedConstruction<ManagedDirectory> mockedConstruction;
    private FilesystemDataServiceFactory factory;

    @BeforeEach
    public void prepareForTests() {
        mockedConstruction = mockConstruction(ManagedDirectory.class);

        factory = new FilesystemDataServiceFactory();
    }

    @AfterEach
    public void cleanTests() {
        mockedConstruction.close();
    }

    @Test
    public void should_create_new_instance_of_filesystem_data_service() {
        Properties props = new Properties();
        props.put(STORAGE_DIRECTORY.property(), "some/test/location");

        FileDataService service = factory.newFileDataService(props);

        assertInstanceOf(FilesystemDataService.class, service);
    }

    @Test
    public void throws_exception_if_invalid_configuration() {
        assertThrows(RuntimeException.class, () -> {
            factory.newFileDataService(new Properties());
        });
    }

}
