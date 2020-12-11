io.harmed.

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    //main directory
    private Path rootPath;
    //all sub-paths
    private List<Path> fileList;

    public FileManager(Path rootPath) throws IOException {
        this.rootPath=rootPath;
        fileList = new ArrayList<>();
        collectFileList(rootPath);
    }

    public List<Path> getFileList(){
            return fileList;
    }

    private void collectFileList(Path path) throws IOException{
        //if it is the last path
        if(Files.isRegularFile(path)){
            fileList.add(rootPath.relativize(path));
        }
        //Recursion
        else if(Files.isDirectory(path)){
            try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path)){
                for(Path file: directoryStream){
                    collectFileList(file);
                }
            }
        }
    }

    public Path getRootPath() {
        return rootPath;
    }
}
