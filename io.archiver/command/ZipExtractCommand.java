packege io.archiver.command;

public class ZipExtractCommand extends ZipCommand{
  @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Распаковка архива");

            ZipFileManager zipFileManager = getZipFileManager();

            ConsoleHelper.writeMessage("Введите полное имя файла или директории для распаковки:");
            Path sourcePath = Paths.get(ConsoleHelper.readString());
            zipFileManager.extractAll(sourcePath);

            ConsoleHelper.writeMessage("Архив распакован.");

        } catch (PathIsNotFoundException e) {
            ConsoleHelper.writeMessage("Вы неверно указали имя файла или директории.");
        }
    }
}
