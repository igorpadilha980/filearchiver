import com.github.igorpadilha980.filearchiver.data.bootstrap.FileDataServiceFactory;
import com.github.igorpadilha980.filearchiver.data.filesystem.FilesystemDataServiceFactory;

module filearchiver.data.filesystem {
    requires filearchiver.data;

    exports com.github.igorpadilha980.filearchiver.data.filesystem;

    provides FileDataServiceFactory with FilesystemDataServiceFactory;
}