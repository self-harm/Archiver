package io.archive.command;

import io.archive.ConsoleHelper;
import io.archive.ZipFileManager;
import io.archive.exception.PathIsNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ZipCreateCommand extends ZipCommand{
    @Override
    public void execute() throws Exception {
        try {
            ConsoleHelper.writeMessage("Создание архива");

            ZipFileManager zipFileManager = getZipFileManager();

            ConsoleHelper.writeMessage("Введите полное имя файла и директирию для архивации:");

            //full path of fileName
            Path path = Paths.get(ConsoleHelper.readString());
            zipFileManager.createZip(path);

            ConsoleHelper.writeMessage("Архив создан.");
        }
        catch (PathIsNotFoundException e){
            ConsoleHelper.writeMessage("Вы неверно указали имя файла или директории.");
            e.getStackTrace();
        }
    }
}
