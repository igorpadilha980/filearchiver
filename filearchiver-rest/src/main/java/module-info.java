module filearchiver.rest {
    requires spring.boot;
    requires spring.boot.autoconfigure;

    requires spring.web;
    requires spring.beans;
    requires spring.context;
    requires spring.data.commons;

    requires jakarta.persistence;

    requires lombok;

    requires filearchiver.data;
    requires filearchiver.data.filesystem;
}