package test;

import manager.HttpTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static model.Status.DONE;
import static model.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
	String url = "http://localhost:8070/";
	static KVServer server;

	@BeforeAll
	static void startKVServer() throws IOException {
		server = new KVServer();
		server.start();
	}

	@AfterAll
	static void stopKVServer() {
		server.stop();
	}

	@BeforeEach
	public void createNewManager() throws IOException, InterruptedException {
		manager = new HttpTaskManager(url);
	}

	@Test
	public void manager_should_be_saved_and_loaded_with_tasks() {
		manager.addTask(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addTask(new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now()));

		assertTaskManagerEquals(manager, HttpTaskManager.load(url, manager.getClientAPIToken()));
	}

	@Test
	public void manager_should_be_saved_and_loaded_with_tasks_and_epics() {
		manager.addTask(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addTask(new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addEpic(new Epic("epic 1", ""));
		manager.addEpic(new Epic("epic 2", ""));

		assertTaskManagerEquals(manager, HttpTaskManager.load(url, manager.getClientAPIToken()));
	}

	@Test
	public void manager_should_be_saved_and_loaded_with_tasks_and_epics_and_subtasks() {
		manager.addTask(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addTask(new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addEpic(new Epic("epic 1", ""));
		manager.addEpic(new Epic("epic 2", ""));
		manager.addSubtask(new Subtask(3, "subtask 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addSubtask(new Subtask(3, "subtask 2", "", DONE, Duration.ZERO, LocalDateTime.now()));

		assertTaskManagerEquals(manager, HttpTaskManager.load(url, manager.getClientAPIToken()));
	}

	//Тот самый тест
	@Test
	public void manager_should_be_saved_and_loaded_with_tasks_and_epics_and_subtasks_and_history() {
		manager.addTask(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addTask(new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addEpic(new Epic("epic 1", ""));
		manager.addEpic(new Epic("epic 2", ""));
		manager.addSubtask(new Subtask(3, "subtask 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addSubtask(new Subtask(3, "subtask 2", "", DONE, Duration.ZERO, LocalDateTime.now()));

		assertTaskManagerEquals(manager, HttpTaskManager.load(url, manager.getClientAPIToken()));
	}

	@Test
	public void manager_should_be_saved_and_loaded_with_tasks_epics_subtasks_history_when_all_tasks_are_deleted() {
		manager.addTask(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addTask(new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addEpic(new Epic("epic 1", ""));
		manager.addEpic(new Epic("epic 2", ""));
		manager.addSubtask(new Subtask(3, "subtask 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addSubtask(new Subtask(3, "subtask 2", "", DONE, Duration.ZERO, LocalDateTime.now()));

		manager.getTask(1);
		manager.getSubtask(6);
		manager.getEpic(3);

		manager.deleteTasks();

		assertTaskManagerEquals(manager, HttpTaskManager.load(url, manager.getClientAPIToken()));
	}

	@Test
	public void manager_should_be_saved_and_loaded_with_tasks_epics_subtasks_history_when_all_epics_are_deleted() {
		manager.addTask(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addTask(new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addEpic(new Epic("epic 1", ""));
		manager.addEpic(new Epic("epic 2", ""));
		manager.addSubtask(new Subtask(3, "subtask 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addSubtask(new Subtask(3, "subtask 2", "", DONE, Duration.ZERO, LocalDateTime.now()));

		manager.getTask(1);
		manager.getSubtask(6);
		manager.getEpic(3);

		manager.deleteEpics();

		assertTaskManagerEquals(manager, HttpTaskManager.load(url, manager.getClientAPIToken()));
	}

	@Test
	public void manager_should_be_saved_and_loaded_with_tasks_epics_subtasks_history_when_all_tasks_epics_are_deleted() {
		manager.addTask(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addTask(new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addEpic(new Epic("epic 1", ""));
		manager.addEpic(new Epic("epic 2", ""));
		manager.addSubtask(new Subtask(3, "subtask 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addSubtask(new Subtask(3, "subtask 2", "", DONE, Duration.ZERO, LocalDateTime.now()));

		manager.getTask(1);
		manager.getSubtask(6);
		manager.getEpic(3);

		manager.deleteTasks();
		manager.deleteEpics();

		assertTaskManagerEquals(manager, HttpTaskManager.load(url, manager.getClientAPIToken()));
	}

	private void assertTaskManagerEquals(TaskManager firstManager, TaskManager secondManager) {
		assertArrayEquals(firstManager.getTasks().toArray(), secondManager.getTasks().toArray());
		assertArrayEquals(firstManager.getEpics().toArray(), secondManager.getEpics().toArray());
		assertArrayEquals(firstManager.getAllSubtasks().toArray(), secondManager.getAllSubtasks().toArray());
		assertArrayEquals(firstManager.getHistory().toArray(), secondManager.getHistory().toArray());
		assertArrayEquals(firstManager.getPrioritizedTasks().toArray(), secondManager.getPrioritizedTasks().toArray());
	}
}