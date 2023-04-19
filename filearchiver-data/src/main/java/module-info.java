import com.github.igorpadilha980.filearchiver.data.bootstrap.FileDataServiceFactory;

module filearchiver.data {

    exports com.github.igorpadilha980.filearchiver.data;
    exports com.github.igorpadilha980.filearchiver.data.bootstrap;

    uses FileDataServiceFactory;

}