package com.tutego.date4u.interfaces.shell;

import com.tutego.date4u.core.FileSystem;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.unit.DataSize;

@ShellComponent
public class FsCommands {

    private final FileSystem fs = new FileSystem();

    @ShellMethod("Prints required free disk space")
    public String freeDiskSpace() {
        return DataSize.ofBytes( fs.getFreeDiskSpace() ).toGigabytes() + " GB";
    }

    @ShellMethod("Converts a string to lower case")
    public String toLowerCase( String s ) {
        return s.toLowerCase();
    }

}
