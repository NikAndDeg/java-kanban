package test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import model.Task;
import static model.Status.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class HistoryManagerTest {
	HistoryManager historyManager;

	@BeforeEach
	public void createNewHistoryManager() {
		historyManager = new InMemoryHistoryManager();
	}

	@Test
	public void history_manager_should_return_empty_list() {
		assertTrue(historyManager.getHistory().isEmpty());
	}

	@Test
	public void history_manager_should_save_and_return_task() {
		historyManager.add(new Task("task 1", "", NEW));

		Task taskFromHistory = historyManager.getHistory().get(0);

		Task task = new Task("task 1", "", NEW);

		assertEquals(task, taskFromHistory);
	}

	@Test
	public void history_manager_should_remove_duplicate_and_save_task_as_last() {
		historyManager.add(new Task("task 1", "", NEW));
		historyManager.add(new Task("task 2", "", NEW));
		historyManager.add(new Task("task 1", "", NEW));

		List<Task> tasks = historyManager.getHistory();

		Task firstTask = new Task("task 1", "", NEW);
		Task secondTask = new Task("task 2", "", NEW);

		assertEquals(tasks.get(0), secondTask);
		assertEquals(tasks.get(1), firstTask);
		assertEquals(2, tasks.size());
	}

	@Test
	public void history_manager_should_delete_first_task() {
		historyManager.add(new Task("task 1", "", NEW));
		historyManager.add(new Task("task 2", "", NEW));
		historyManager.add(new Task("task 3", "", NEW));

		Task firstTask = new Task("task 1", "", NEW);
		Task secondTask = new Task("task 2", "", NEW);
		Task thirdTask = new Task("task 3", "", NEW);
		Task deletedTask = historyManager.delete(firstTask);

		List<Task> tasks = historyManager.getHistory();

		assertEquals(firstTask, deletedTask);
		assertEquals(tasks.get(0), secondTask);
		assertEquals(tasks.get(1), thirdTask);
		assertEquals(2, tasks.size());
	}

	@Test
	public void history_manager_should_delete_middle_task() {
		historyManager.add(new Task("task 1", "", NEW));
		historyManager.add(new Task("task 2", "", NEW));
		historyManager.add(new Task("task 3", "", NEW));

		Task firstTask = new Task("task 1", "", NEW);
		Task secondTask = new Task("task 2", "", NEW);
		Task thirdTask = new Task("task 3", "", NEW);
		Task deletedTask = historyManager.delete(secondTask);

		List<Task> tasks = historyManager.getHistory();

		assertEquals(secondTask, deletedTask);
		assertEquals(tasks.get(0), firstTask);
		assertEquals(tasks.get(1), thirdTask);
		assertEquals(2, tasks.size());
	}

	@Test
	public void history_manager_should_delete_last_task() {
		historyManager.add(new Task("task 1", "", NEW));
		historyManager.add(new Task("task 2", "", NEW));
		historyManager.add(new Task("task 3", "", NEW));

		Task firstTask = new Task("task 1", "", NEW);
		Task secondTask = new Task("task 2", "", NEW);
		Task thirdTask = new Task("task 3", "", NEW);
		Task deletedTask = historyManager.delete(thirdTask);

		List<Task> tasks = historyManager.getHistory();

		assertEquals(thirdTask, deletedTask);
		assertEquals(tasks.get(0), firstTask);
		assertEquals(tasks.get(1), secondTask);
		assertEquals(2, tasks.size());
	}
}