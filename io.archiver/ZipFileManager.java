packege io.archiver;

import io.archive.exception.PathIsNotFoundException;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipFileManager {
    private Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    public void createZip(Path source) throws Exception {
        //check if zipDirectory exists; if no then create new one
        Path zipDirectory = zipFile.getParent();
        //check
        if (Files.notExists(zipDirectory)) {
            //create new one
            Files.createDirectories(zipDirectory);
        }

        //create the stream (zip will archive zipFile)
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile));
        ) {

            if (Files.isDirectory(source)) {
                //if we are going to archive the directory, we have to get names of all files in it
                FileManager fileManager = new FileManager(source);
                List<Path> fileNames = fileManager.getFileList();

                for (Path fileName : fileNames)
                    addNewZipEntry(zipOutputStream, source, fileName);

            }
            //if the source is a regular file
            else if (Files.isRegularFile(source)) {
                addNewZipEntry(zipOutputStream, source.getParent(), source.getFileName());
            }
        //if the source isn't either a file or a directory, then throw an exception
        else{
                ConsoleHelper.writeMessage("Путь не найден!");
                throw new PathIsNotFoundException();
            }
        }
    }

    private void addNewZipEntry(ZipOutputStream zipOutputStream, Path filePath, Path fileName) throws Exception{
        Path fullPath = Paths.get(filePath.toString() + "/" + fileName.toString());

        try(InputStream inputStream = Files.newInputStream(fullPath)) {
            ZipEntry entry = new ZipEntry(fileName.toString());
            zipOutputStream.putNextEntry(entry);

            //write from inputStream to zipOutputStream
//            while (inputStream.available()>0){
//                zipOutputStream.write(inputStream.read());
//            }
            copyData(inputStream, zipOutputStream);

            zipOutputStream.closeEntry();
        }
    }

    private void copyData(InputStream in, OutputStream out) throws Exception {
        byte[] buffer = new byte[8 * 1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }
}
