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
    
       public List<FileProperties> getFilesList() throws Exception {
            //check if zipFile isn't a normal file
            if(!Files.isRegularFile(zipFile)) throw new WrongZipFileException();

            //create ArrayList of FileProperties
            List<FileProperties> listOfFiles = new ArrayList<>();


            try(ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))){
                //receive a zipEntry
                ZipEntry zipEntry = zipInputStream.getNextEntry();

                while(zipEntry!=null){
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    copyData(zipInputStream, baos);

                    FileProperties file = new FileProperties(zipEntry.getName(), zipEntry.getSize(), zipEntry.getCompressedSize(), zipEntry.getMethod());
                    listOfFiles.add(file);
                    zipEntry = zipInputStream.getNextEntry();
                }
                
            }
            return listOfFiles;
    }
    
       public void extractAll(Path outputFolder) throws Exception{
        //check if zipFile exists
       if(!Files.isRegularFile(zipFile)) throw new WrongZipFileException();

        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
            //create a directory if it doesn't exist
            if (Files.notExists(outputFolder))
                Files.createDirectories(outputFolder);

            ZipEntry zipEntry = zipInputStream.getNextEntry();

            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                Path fileFullName = outputFolder.resolve(fileName);
                
                Path parent = fileFullName.getParent();
                if (Files.notExists(parent))
                    Files.createDirectories(parent);

                try (OutputStream outputStream = Files.newOutputStream(fileFullName)) {
                    copyData(zipInputStream, outputStream);
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        }
    }

    private void copyData(InputStream in, OutputStream out) throws Exception {
        byte[] buffer = new byte[8 * 1024];
        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }
    
    public void removeFile(Path path) throws Exception {
        removeFiles(Collections.singletonList(path));
    }

    public void removeFiles(List<Path> pathList) throws Exception{
        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }

        //create temporary file
        Path tempZipFile = Files.createTempFile(null, null);

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempZipFile))) {
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {

                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while (zipEntry != null) {

                    Path archivedFile = Paths.get(zipEntry.getName());

                    if (!pathList.contains(archivedFile)) {
                        String fileName = zipEntry.getName();
                        zipOutputStream.putNextEntry(new ZipEntry(fileName));

                        copyData(zipInputStream, zipOutputStream);

                        zipOutputStream.closeEntry();
                        zipInputStream.closeEntry();
                    } else {
                        ConsoleHelper.writeMessage(String.format("Файл '%s' удален из архива.", archivedFile.toString()));
                    }
                    zipEntry = zipInputStream.getNextEntry();
                }
            }
        }
        //swap tempFile and origFile
        Files.move(tempZipFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }
    
    
      public void addFiles(List<Path> absolutePathList) throws Exception{
        if (!Files.isRegularFile(zipFile)) {
            throw new WrongZipFileException();
        }

        Path tempZipFile = Files.createTempFile(null, null);

        List<Path> archiveFiles = new ArrayList<>();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(tempZipFile))) {
            try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {

                ZipEntry zipEntry = zipInputStream.getNextEntry();
                while (zipEntry != null) {
                    String fileName = zipEntry.getName();
                    archiveFiles.add(Paths.get(fileName));

                    zipOutputStream.putNextEntry(new ZipEntry(fileName));
                    copyData(zipInputStream, zipOutputStream);

                    zipInputStream.closeEntry();
                    zipOutputStream.closeEntry();

                    zipEntry = zipInputStream.getNextEntry();
                }
            }

            //archive new files
            for (Path file : absolutePathList) {
                if (Files.isRegularFile(file)) {
                    if (archiveFiles.contains(file.getFileName()))
                        ConsoleHelper.writeMessage(String.format("Файл '%s' уже существует в архиве.", file.toString()));
                    else {
                        addNewZipEntry(zipOutputStream, file.getParent(), file.getFileName());
                        ConsoleHelper.writeMessage(String.format("Файл '%s' добавлен в архиве.", file.toString()));
                    }
                } else
                    throw new PathIsNotFoundException();
            }
        }
        Files.move(tempZipFile, zipFile, StandardCopyOption.REPLACE_EXISTING);
    }
    
     public void addFile(Path absolutePath) throws Exception {
        addFiles(Collections.singletonList(absolutePath));
    }
}
