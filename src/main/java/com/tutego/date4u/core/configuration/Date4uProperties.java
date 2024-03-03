package com.tutego.date4u.core.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("date4u")
public class Date4uProperties {

    public  static class FileSystem {
        private long minimumFreeDiskSpace = 1_000_000;

        public long getMinimumFreeDiskSpace() {
            return minimumFreeDiskSpace;
        }

        public void setMinimumFreeDiskSpace(long minimumFreeDiskSpace) {
            this.minimumFreeDiskSpace = minimumFreeDiskSpace;
        }
    }

    private FileSystem filesystem = new FileSystem();
    public FileSystem getFilesystem() {
        return filesystem;
    }

    public void setFilesystem(FileSystem filesystem) {
        this.filesystem = filesystem;
    }
}
