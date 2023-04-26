package com.github.igorpadilha980.filearchiver.rest;

import com.github.igorpadilha980.filearchiver.data.FileDataService;
import com.github.igorpadilha980.filearchiver.data.bootstrap.FileDataServiceLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Properties;

@Configuration
public class FileDataServiceConfig {

    @Autowired
    private Environment env;

    private Properties copyFromEnv(List<String> properties) {
        Properties copy = new Properties();

        for (String prop : properties)
            copy.put(prop, env.getProperty(prop));

        System.out.println(copy);

        return copy;
    }

    private Properties serviceConfiguration() {
        List<String> props = List.of(
                "filearchiver.data.filesystem.storage",
                "filearchiver.data.filesystem.generate-storage"
        );

        return copyFromEnv(props);
    }

    @Bean
    public FileDataService fileDataService() {
        FileDataServiceLoader loader = new FileDataServiceLoader();

        loader.configure(serviceConfiguration());

        return loader.load();
    }

}
