packege io.archiver.command;

public class ZipRemoveCommand extends ZipCommand{
    @Override
    public void execute() throws Exception {
        ZipFileManager zipFileManager = getZipFileManager();
        ConsoleHelper.writeMessage("Введите путь архива и файл для удаления:");

        zipFileManager.removeFile(Paths.get(ConsoleHelper.readString()));
    }
}
