packege io.archiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Archiver {
      public static Operation askOperation() throws IOException {
        ConsoleHelper.writeMessage("Выберите операцию:\n" +
                "0 - упаковать файлы в архив\n" +
                "1 - добавить файл в архив\n" +
                "2 - удалить файл из архива\n" +
                "3 - распаковать архив\n" +
                "4 - просмотреть содержимое архива\n" +
                "5 - выход\n");
        int number = ConsoleHelper.readInt();

        switch (number){
            case 0: return Operation.CREATE;
            case 1: return Operation.ADD;
            case 2: return Operation.REMOVE;
            case 3: return Operation.EXTRACT;
            case 4: return Operation.CONTENT;
            case 5: return Operation.EXIT;
        }
        return null;
    }

    //using UTF-8 coding
    public static void main(String[] args) throws Exception {
        Operation operation = null;
        do {
            try {
                operation = askOperation();
                CommandExecutor.execute(operation);
            } catch (WrongZipFileException e) {
                ConsoleHelper.writeMessage("Вы не выбрали файл архива или выбрали неверный файл.");
            } catch (Exception e) {
                ConsoleHelper.writeMessage("Произошла ошибка. Проверьте введенные данные.");
            }
        } while (operation != Operation.EXIT);
        
//        //request 2 paths: 1. an archived file path; 2. a path to where we are going to archive
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        String fileName = reader.readLine();
//        String archiveSource = reader.readLine();
//
//        Path filePath = Paths.get(fileName);
//        Path archivePath = Paths.get(archiveSource);
//
//        ZipFileManager zipFileManager = new ZipFileManager(filePath);
//        zipFileManager.createZip(archivePath);
//
//        ExitCommand exitCommand = new ExitCommand();
//        exitCommand.execute();
    }
}
