package servers;

import manager.*;
import manager.FileBackedTasksManager;

public class HttpTaskServer {
    FileBackedTasksManager manager = Managers.getDefaultFileBackedTasksManager();
}
