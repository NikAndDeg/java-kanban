package manager;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager<Task> {
	private final static int DEFAULT_HISTORY_SIZE = 10;

	private final List<Task> history;

	public InMemoryHistoryManager() {
		history = new ArrayList<>();
	}

	@Override
	public void add(Task task) {
		if (history.size() >= DEFAULT_HISTORY_SIZE) {
			history.remove(0);
		}
		history.add(task);
	}

	@Override
	public List<Task> getHistory() {
		return history;
	}
}
