package test;

import manager.FileBackedTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import static model.Status.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
	private final String defaultSaveDirectory = "resources/saves";
	private final String defaultSaveFile = "default-save.csv";
	private final File saveFile = new File(defaultSaveDirectory, defaultSaveFile);

	@BeforeEach
	public void createNewManager() {
		manager = new FileBackedTaskManager(saveFile);
	}

	@Test
	public void manager_should_be_saved_and_loaded_with_tasks() {
		manager.addTask(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addTask(new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now()));

		assertTaskManagerEquals(manager, FileBackedTaskManager.loadFromFile(saveFile));
	}

	@Test
	public void manager_should_be_saved_and_loaded_with_tasks_and_epics() {
		manager.addTask(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addTask(new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addEpic(new Epic("epic 1", ""));
		manager.addEpic(new Epic("epic 2", ""));

		assertTaskManagerEquals(manager, FileBackedTaskManager.loadFromFile(saveFile));
	}

	@Test
	public void manager_should_be_saved_and_loaded_with_tasks_and_epics_and_subtasks() {
		manager.addTask(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addTask(new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addEpic(new Epic("epic 1", ""));
		manager.addEpic(new Epic("epic 2", ""));
		manager.addSubtask(new Subtask(3, "subtask 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addSubtask(new Subtask(3, "subtask 2", "", DONE, Duration.ZERO, LocalDateTime.now()));

		assertTaskManagerEquals(manager, FileBackedTaskManager.loadFromFile(saveFile));
	}

	@Test
	public void manager_should_be_saved_and_loaded_with_tasks_and_epics_and_subtasks_and_history() {
		manager.addTask(new Task("task 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addTask(new Task("task 2", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addEpic(new Epic("epic 1", ""));
		manager.addEpic(new Epic("epic 2", ""));
		manager.addSubtask(new Subtask(3, "subtask 1", "", NEW, Duration.ZERO, LocalDateTime.now()));
		manager.addSubtask(new Subtask(3, "subtask 2", "", DONE, Duration.ZERO, LocalDateTime.now()));

		manager.getTask(1);
		manager.getSubtask(6);
		manager.getEpic(3);

		assertTaskManagerEquals(manager, FileBackedTaskManager.loadFromFile(saveFile));
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

		assertTaskManagerEquals(manager, FileBackedTaskManager.loadFromFile(saveFile));
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

		assertTaskManagerEquals(manager, FileBackedTaskManager.loadFromFile(saveFile));
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

		assertTaskManagerEquals(manager, FileBackedTaskManager.loadFromFile(saveFile));
	}

	private void assertTaskManagerEquals(TaskManager firstManager, TaskManager secondManager) {
		assertArrayEquals(firstManager.getTasks().toArray(), secondManager.getTasks().toArray());
		assertArrayEquals(firstManager.getEpics().toArray(), secondManager.getEpics().toArray());
		assertArrayEquals(firstManager.getAllSubtasks().toArray(), secondManager.getAllSubtasks().toArray());
		assertArrayEquals(firstManager.getHistory().toArray(), secondManager.getHistory().toArray());
	}

}