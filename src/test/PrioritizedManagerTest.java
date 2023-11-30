package test;

import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import static model.Status.*;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PrioritizedManagerTest {
	TaskManager manager;

	@BeforeEach
	public void createNewManager() {
		manager = new InMemoryTaskManager();
	}

	@Test
	public void prioritized_test_with_no_tasks() {
		assertTrue(manager.getPrioritizedTasks().isEmpty());
	}

	@Test
	public void prioritized_test_with_one_task() {
		manager.addTask(createTask("task", "2010-10-10T10:10:10"));

		List<Task> expected = new LinkedList<>();
		expected.add(createTask("task", "2010-10-10T10:10:10"));

		assertArrayEquals(expected.toArray(), manager.getPrioritizedTasks().toArray());
	}

	@Test
	public void prioritized_test_with_tasks() {
		manager.addTask(createTask("first task", "2020-10-10T10:10:10"));
		manager.addTask(createTask("second task", "2010-10-10T10:10:10"));
		manager.addTask(createTask("third task", "1990-10-10T10:10:10"));
		manager.addTask(createTask("fourth task", "2005-10-10T10:10:10"));

		List<Task> expected = new LinkedList<>();
		expected.add(createTask("third task", "1990-10-10T10:10:10"));
		expected.add(createTask("fourth task", "2005-10-10T10:10:10"));
		expected.add(createTask("second task", "2010-10-10T10:10:10"));
		expected.add(createTask("first task", "2020-10-10T10:10:10"));

		assertArrayEquals(expected.toArray(), manager.getPrioritizedTasks().toArray());
	}

	@Test
	public void prioritized_test_with_tasks_when_one_task_is_deleted() {
		manager.addTask(createTask("first task", "2020-10-10T10:10:10"));
		manager.addTask(createTask("second task", "2010-10-10T10:10:10"));
		manager.addTask(createTask("third task", "1990-10-10T10:10:10"));
		manager.addTask(createTask("fourth task", "2005-10-10T10:10:10"));

		manager.deleteTask(2);

		List<Task> expected = new LinkedList<>();
		expected.add(createTask("third task", "1990-10-10T10:10:10"));
		expected.add(createTask("fourth task", "2005-10-10T10:10:10"));
		expected.add(createTask("first task", "2020-10-10T10:10:10"));

		assertArrayEquals(expected.toArray(), manager.getPrioritizedTasks().toArray());
	}

	@Test
	public void prioritized_test_with_tasks_when_all_tasks_are_deleted() {
		manager.addTask(createTask("first task", "2020-10-10T10:10:10"));
		manager.addTask(createTask("second task", "2010-10-10T10:10:10"));
		manager.addTask(createTask("third task", "1990-10-10T10:10:10"));
		manager.addTask(createTask("fourth task", "2005-10-10T10:10:10"));

		manager.deleteTasks();

		assertTrue(manager.getPrioritizedTasks().isEmpty());
	}

	@Test
	public void prioritized_test_with_task_and_subtask() {
		manager.addEpic(new Epic("epic", ""));
		manager.addSubtask(createSubtask("subtask","2020-10-10T10:10:10"));
		manager.addTask(createTask("first task", "1990-10-10T10:10:10"));


		List<Task> expected = new LinkedList<>();
		expected.add(createTask("first task", "1990-10-10T10:10:10"));
		expected.add(createSubtask("subtask","2020-10-10T10:10:10"));

		assertArrayEquals(expected.toArray(), manager.getPrioritizedTasks().toArray());
	}

	@Test
	public void prioritized_test_with_tasks_and_subtasks() {
		manager.addEpic(new Epic("epic", ""));
		manager.addSubtask(createSubtask("first subtask","2020-10-10T10:10:10"));
		manager.addTask(createTask("first task", "1990-10-10T10:10:10"));
		manager.addSubtask(createSubtask("second subtask","2222-10-10T10:10:10"));
		manager.addTask(createTask("second task", "2000-10-10T10:10:10"));


		List<Task> expected = new LinkedList<>();
		expected.add(createTask("first task", "1990-10-10T10:10:10"));
		expected.add(createTask("second task", "2000-10-10T10:10:10"));
		expected.add(createSubtask("first subtask","2020-10-10T10:10:10"));
		expected.add(createSubtask("second subtask","2222-10-10T10:10:10"));

		assertArrayEquals(expected.toArray(), manager.getPrioritizedTasks().toArray());
	}

	@Test
	public void prioritized_test_with_tasks_and_subtasks_when_all_tasks_are_deleted() {
		manager.addEpic(new Epic("epic", ""));
		manager.addSubtask(createSubtask("first subtask","2020-10-10T10:10:10"));
		manager.addTask(createTask("first task", "1990-10-10T10:10:10"));
		manager.addSubtask(createSubtask("second subtask","2222-10-10T10:10:10"));
		manager.addTask(createTask("second task", "2000-10-10T10:10:10"));

		manager.deleteTasks();


		List<Task> expected = new LinkedList<>();
		expected.add(createSubtask("first subtask","2020-10-10T10:10:10"));
		expected.add(createSubtask("second subtask","2222-10-10T10:10:10"));

		assertArrayEquals(expected.toArray(), manager.getPrioritizedTasks().toArray());
	}

	@Test
	public void prioritized_test_with_tasks_and_subtasks_when_one_subtask_is_deleted() {
		manager.addEpic(new Epic("epic", ""));
		manager.addSubtask(createSubtask("first subtask","2020-10-10T10:10:10"));
		manager.addTask(createTask("first task", "1990-10-10T10:10:10"));
		manager.addSubtask(createSubtask("second subtask","2222-10-10T10:10:10"));
		manager.addTask(createTask("second task", "2000-10-10T10:10:10"));

		manager.deleteSubtask(4);

		List<Task> expected = new LinkedList<>();
		expected.add(createTask("first task", "1990-10-10T10:10:10"));
		expected.add(createTask("second task", "2000-10-10T10:10:10"));
		expected.add(createSubtask("first subtask","2020-10-10T10:10:10"));

		assertArrayEquals(expected.toArray(), manager.getPrioritizedTasks().toArray());
	}

	@Test
	public void prioritized_test_with_tasks_and_subtasks_when_epic_is_deleted() {
		manager.addEpic(new Epic("epic", ""));
		manager.addSubtask(createSubtask("first subtask","2020-10-10T10:10:10"));
		manager.addTask(createTask("first task", "1990-10-10T10:10:10"));
		manager.addSubtask(createSubtask("second subtask","2222-10-10T10:10:10"));
		manager.addTask(createTask("second task", "2000-10-10T10:10:10"));

		manager.deleteEpic(1);

		List<Task> expected = new LinkedList<>();
		expected.add(createTask("first task", "1990-10-10T10:10:10"));
		expected.add(createTask("second task", "2000-10-10T10:10:10"));

		assertArrayEquals(expected.toArray(), manager.getPrioritizedTasks().toArray());
	}

	private Task createTask(String name, String startTime) {
		return new Task(name,
				"",
				NEW,
				Duration.ofMinutes(20),
				LocalDateTime.parse(startTime)
		);
	}

	private Subtask createSubtask(String name, String startTime) {
		return new Subtask(1,
				name,
				"",
				NEW,
				Duration.ofMinutes(20),
				LocalDateTime.parse(startTime)
		);
	}
}