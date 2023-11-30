package manager;

public class Managers {

	public static TaskManager getDefault() {
		return new InMemoryTaskManager();
	}

	public static HistoryManager getDefaultHistory() {
		return new InMemoryHistoryManager();
	}

	public static PrioritizedManager getPrioritizedManager() {
		return new PrioritizedManager();
	}
}
