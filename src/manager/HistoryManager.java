package manager;

import model.Task;

import java.util.List;

public interface HistoryManager {
	void add(Task task);
	Task delete(Task task);
	List<Task> getHistory();
}
