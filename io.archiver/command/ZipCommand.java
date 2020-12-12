packege io.archiver.command;

import  io.archiver.ConsoleHelper;
import  io.archiver.ZipFileManager;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class ZipCommand implements Command{
    @Override
    public void execute() throws Exception {

    }
    
    public ZipFileManager getZipFileManager() throws Exception{
    String pathString = ConsoleHelper.readString();

    return new ZipFileManager(Paths.get(pathString));
    }
}
