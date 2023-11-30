package test;

import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import model.Task;
import static model.Status.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;
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
		historyManager.add(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));

		Task taskFromHistory = historyManager.getHistory().get(0);

		Task task = new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now());

		assertEquals(task, taskFromHistory);
	}

	@Test
	public void history_manager_should_remove_duplicate_and_save_task_as_last() {
		historyManager.add(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		historyManager.add(new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now()));
		historyManager.add(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));

		List<Task> tasks = historyManager.getHistory();

		Task firstTask = new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now());
		Task secondTask = new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now());

		assertEquals(tasks.get(0), secondTask);
		assertEquals(tasks.get(1), firstTask);
		assertEquals(2, tasks.size());
	}

	@Test
	public void history_manager_should_delete_first_task() {
		historyManager.add(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		historyManager.add(new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now()));
		historyManager.add(new Task("task 3", "", NEW, Duration.ZERO, LocalDateTime.now()));

		Task firstTask = new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now());
		Task secondTask = new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now());
		Task thirdTask = new Task("task 3", "", NEW, Duration.ZERO, LocalDateTime.now());
		Task deletedTask = historyManager.delete(firstTask);

		List<Task> tasks = historyManager.getHistory();

		assertEquals(firstTask, deletedTask);
		assertEquals(tasks.get(0), secondTask);
		assertEquals(tasks.get(1), thirdTask);
		assertEquals(2, tasks.size());
	}

	@Test
	public void history_manager_should_delete_middle_task() {
		historyManager.add(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		historyManager.add(new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now()));
		historyManager.add(new Task("task 3", "", NEW, Duration.ZERO, LocalDateTime.now()));

		Task firstTask = new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now());
		Task secondTask = new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now());
		Task thirdTask = new Task("task 3", "", NEW, Duration.ZERO, LocalDateTime.now());
		Task deletedTask = historyManager.delete(secondTask);

		List<Task> tasks = historyManager.getHistory();

		assertEquals(secondTask, deletedTask);
		assertEquals(tasks.get(0), firstTask);
		assertEquals(tasks.get(1), thirdTask);
		assertEquals(2, tasks.size());
	}

	@Test
	public void history_manager_should_delete_last_task() {
		historyManager.add(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		historyManager.add(new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now()));
		historyManager.add(new Task("task 3", "", NEW, Duration.ZERO, LocalDateTime.now()));

		Task firstTask = new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now());
		Task secondTask = new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now());
		Task thirdTask = new Task("task 3", "", NEW, Duration.ZERO, LocalDateTime.now());
		Task deletedTask = historyManager.delete(thirdTask);

		List<Task> tasks = historyManager.getHistory();

		assertEquals(thirdTask, deletedTask);
		assertEquals(tasks.get(0), firstTask);
		assertEquals(tasks.get(1), secondTask);
		assertEquals(2, tasks.size());
	}
}