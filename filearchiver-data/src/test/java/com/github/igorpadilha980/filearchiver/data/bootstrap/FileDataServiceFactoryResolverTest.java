package com.github.igorpadilha980.filearchiver.data.bootstrap;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileDataServiceFactoryResolverTest {

    @Mock
    private MockedStatic<ServiceLoader> loaderMockedStatic;

    @Mock
    private ServiceLoader<FileDataServiceFactory> serviceLoaderMock;

    private FileDataServiceFactoryResolver resolver;

    @BeforeEach
    public void prepareForTest() {
        loaderMockedStatic.when(() -> ServiceLoader.load(any())).thenReturn(serviceLoaderMock);

        resolver = new FileDataServiceFactoryResolver();
    }

    @Test
    public void should_resolve_factory_implementation() {
        when(serviceLoaderMock.findFirst()).thenReturn(Optional.of(mock()));

        FileDataServiceFactory resolvedImpl =  resolver.resolveImplementation();

        assertNotNull(resolvedImpl);
    }

    @Test
    public void throws_exception_if_no_implementation_found() {
        when(serviceLoaderMock.findFirst()).thenReturn(Optional.empty());

        assertThrows(FileServiceFactoryNotFoundException.class, () -> {
            resolver.resolveImplementation();
        });
    }

}
