package com.github.igorpadilha980.filearchiver.data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A gateway for file data access
 * 
 * @author Igor Padilha
 */
@FunctionalInterface
public interface FileDataSource {
	
	static FileDataSource fromFile(Path filePath) {
		return () -> Files.newInputStream(filePath);
	}
	
	/**
	 * Returns a new {@link InputStream} for file data
	 * streaming
	 * 
	 * @throws IOException on fail to open resource
	 * 
	 * @return file data
	 */
	InputStream inputStream() throws IOException; 

}
