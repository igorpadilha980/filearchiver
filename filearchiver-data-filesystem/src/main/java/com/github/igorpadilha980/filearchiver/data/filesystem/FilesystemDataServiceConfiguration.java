package com.github.igorpadilha980.filearchiver.data.filesystem;

public enum FilesystemDataServiceConfiguration {

    STORAGE_DIRECTORY("filearchiver.data.filesystem.storage"),
    STORAGE_GENERATE("filearchiver.data.filesystem.generate-storage");

    private final String propertyName;

    FilesystemDataServiceConfiguration(String propertyName) {
        this.propertyName = propertyName;
    }

    public String property() {
        return propertyName;
    }

}
