import java.nio.file.Path;

public class ZipFileManager {
    private Path zipFile;

    public ZipFileManager(Path zipFile) {
        this.zipFile = zipFile;
    }

    public void createZip(Path source) throws Exception {
            //create the stream (zip will archive zipFile)
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFile));
            InputStream inputStream = Files.newInputStream(source)){

            ZipEntry entry = new ZipEntry(source.getFileName().toString());
            zipOutputStream.putNextEntry(entry);
            while (inputStream.available() > 0){
                //write data from inputStream to zipOutStream
                zipOutputStream.write(inputStream.read());
            }
        }
        catch (Exception e){
            e.getStackTrace();
            System.out.println("Произошла ошибка при архивации файла");
        }
    }
}
