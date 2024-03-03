package com.tutego.date4u.interfaces.shell;

import com.tutego.date4u.core.FileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.unit.DataSize;

@ShellComponent
public class FsCommands {

    private final FileSystem fs;
    private Environment env;

    @Value("${date4u.filesystem.minimum-free-disk-space:1000000}")
    private long minimumFreeDiskSpace;

    @ShellMethod("Prints the minimum free disk space")
    public long minimumFreeDiskSpace() {
        return minimumFreeDiskSpace;
    }

    @ShellMethod("Prints the user home directory")
    public String userHome() {
        return env.getProperty("user.home");
    }

    @Autowired
    public FsCommands(FileSystem fs) {
        this.fs = fs;
    }

    @ShellMethod("Prints required free disk space")
    public String freeDiskSpace() {
        return DataSize.ofBytes(fs.getFreeDiskSpace()).toGigabytes() + " GB";
    }

    @ShellMethod("Converts a string to lower case")
    public String toLowerCase(String s) {
        return s.toLowerCase();
    }

}
