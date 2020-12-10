packege io.archiver;

import com.javarush.task.task31.task3110.command.*;

import java.util.HashMap;
import java.util.Map;

public class CommandExecutor {
    private static final Map<Operation, Command> allKnownCommandsMap = new HashMap<>();
    
    private CommandExecutor() {
    }

    static {
            allKnownCommandsMap.put(Operation.CREATE, new ZipCreateCommand());
            allKnownCommandsMap.put(Operation.ADD, new ZipAddCommand());
            allKnownCommandsMap.put(Operation.REMOVE, new ZipRemoveCommand());
            allKnownCommandsMap.put(Operation.EXTRACT, new ZipExtractCommand());
            allKnownCommandsMap.put(Operation.CONTENT, new ZipContentCommand());
            allKnownCommandsMap.put(Operation.EXIT, new ExitCommand());
        }


    //invokes execute method for input operation from HashMap
    public static void execute(Operation operation) throws Exception{
        //allKnownCommandsMap.get(operation).execute();
        for(Map.Entry<Operation, Command> entry: allKnownCommandsMap.entrySet()){
            if(operation.equals(entry.getKey())) entry.getValue().execute();
        }
    }
}
