package com.github.igorpadilha980.filearchiver.data.filesystem;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ManagedDirectoryTest {

    private static Path testDirectory;
    private static ManagedDirectory managedDirectory;

    public static Path createTempDirectory() throws IOException {
        String dirName = "test-directory-" + String.valueOf(System.nanoTime());

        return Files.createTempDirectory(dirName);
    }

    public static long countFiles(Path dir) throws IOException {
        try(var fileStream = Files.walk(dir)) {
            return fileStream.skip(1).count();
        }
    }


    @BeforeAll
    public static void setupTests() throws IOException {
        testDirectory = createTempDirectory();
        managedDirectory = new ManagedDirectory(testDirectory);
    }

    @AfterAll
    public static void finishTests() throws IOException {
        System.out.println("finishing all tests");
        Files.delete(testDirectory);
    }

    @AfterEach
    public void cleanTest() throws IOException {
        System.out.println("cleaning test");

        try(var fileStream = Files.walk(testDirectory)) {

            var paths = fileStream.skip(1).toList();

            for (Path p : paths)
                Files.delete(p);
        }
    }

    @Test
    public void should_create_new_file_in_directory() throws IOException {
        assertDoesNotThrow(() -> {
            ManagedFile file = managedDirectory.newFile();
        });

        assertTrue(countFiles(testDirectory) == 1);
    }

    @Test
    public void throws_exception_if_invalid_parameter_constructor() {
        assertThrows(NullPointerException.class, () -> {
           new ManagedDirectory(null);
        });
    }

    @Test
    public void should_return_present_optional_if_find_file() throws IOException {
        ManagedFile createdFile = managedDirectory.newFile();

        Optional<ManagedFile> searchResult =  managedDirectory.findFile(createdFile.fileId());
        assertTrue(searchResult.isPresent());

        ManagedFile foundFile = searchResult.get();
        assertTrue(createdFile.isSame(foundFile));
    }

    @Test
    public void should_return_empty_optional_if_not_find_file() {
        Optional<ManagedFile> searchResult = managedDirectory.findFile(UUID.randomUUID());

        assertTrue(searchResult.isEmpty());
    }
}
