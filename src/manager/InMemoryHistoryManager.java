package manager;

import model.Task;
import model.list.CustomLinkedList;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

	private final CustomLinkedList<Task> historyList;

	public InMemoryHistoryManager() {
		historyList = new CustomLinkedList<>();
	}

	@Override
	public void add(Task task) {
		historyList.add(task);
	}

	@Override
	public Task delete(Task task) {
		return historyList.delete(task);
	}

	@Override
	public List<Task> getHistory() {
		return historyList.getAsList();
	}
}
