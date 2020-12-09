packege io.Archiver

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Archiver {
    public static void main(String[] args) throws Exception {
        //request 2 paths: 1. an archived file path; 2. a path to where we are going to archive
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileName = reader.readLine();
        String archiveSource = reader.readLine();

        Path filePath = Paths.get(fileName);
        Path archivePath = Paths.get(archiveSource);

        ZipFileManager zipFileManager = new ZipFileManager(filePath);
        zipFileManager.createZip(archivePath);
    }
}
