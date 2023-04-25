module filearchiver.rest {
    requires spring.boot;
    requires spring.boot.autoconfigure;

    requires spring.web;
    requires spring.beans;

    requires filearchiver.data;
    requires filearchiver.data.filesystem;
}