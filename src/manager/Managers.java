package manager;

import model.Task;

public class Managers {

	public static InMemoryTaskManager getDefault() {
		return new InMemoryTaskManager();
	}

	public static HistoryManager<Task> getDefaultHistory() {
		return new InMemoryHistoryManager();
	}
}
