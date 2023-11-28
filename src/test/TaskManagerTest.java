package test;

import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import static model.Status.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

abstract class TaskManagerTest <T extends TaskManager> {
	T manager;

	/*
	Task CRUD tests
	 */
	@Test
	public void task_manager_should_add_task() {
		assertEquals(new Task("", "", NEW), manager.addTask(new Task("", "", NEW)));
		assertEquals(new Task("", "", NEW), manager.getTask(1));
	}

	@Test
	public void task_manager_should_not_add_task_when_adding_already_existing_task() {
		manager.addTask(new Task("", "", NEW));

		assertNull(manager.addTask(new Task("", "", NEW)));
		assertNull(manager.getTask(2));
	}

	@Test
	public void task_manager_should_return_added_task() {
		manager.addTask(new Task("", "", NEW));

		assertEquals(new Task("", "", NEW), manager.getTask(1));
	}

	@Test
	public void task_manager_should_return_null_when_getting_not_existing_task() {
		assertNull(manager.getTask(1));
	}

	@Test
	public void task_manager_should_return_all_tasks() {
		manager.addTask(new Task("first task", "", NEW));
		manager.addTask(new Task("second task", "", DONE));
		List<Task> tasks = List.of(new Task("first task", "", NEW),
				new Task("second task", "", DONE));

		assertArrayEquals(tasks.toArray(), manager.getTasks().toArray());
	}

	@Test
	public void task_manager_should_delete_task() {
		manager.addTask(new Task("first task", "", NEW));
		manager.addTask(new Task("second task", "", DONE));
		manager.deleteTask(1);
		List<Task> tasks = List.of(new Task("second task", "", DONE));

		assertNull(manager.getTask(1));
		assertArrayEquals(tasks.toArray(), manager.getTasks().toArray());
	}

	@Test
	public void task_manager_should_not_delete_not_existing_task_and_should_return_null() {
		manager.addTask(new Task("first task", "", NEW));
		manager.addTask(new Task("second task", "", DONE));
		List<Task> tasks = List.of(new Task("first task", "", NEW),
				new Task("second task", "", DONE));

		assertNull(manager.deleteTask(555));
		assertArrayEquals(tasks.toArray(), manager.getTasks().toArray());
	}

	@Test
	public void task_manager_should_delete_all_tasks() {
		manager.addTask(new Task("first task", "", NEW));
		manager.addTask(new Task("second task", "", DONE));
		manager.deleteTasks();

		assertNull(manager.getTask(1));
		assertNull(manager.getTask(2));
		assertArrayEquals(List.of().toArray(), manager.getTasks().toArray());
	}

	@Test
	public void task_manager_should_update_task() {
		manager.addTask(new Task("first task", "", NEW));
		Task task = new Task("updated task", "", NEW);
		task.setId(1);

		assertEquals(task, manager.updateTask(task));
		assertEquals(task, manager.getTask(1));
	}

	@Test
	public void task_manager_should_not_update_not_existing_task() {
		Task task = new Task("first task", "", NEW);
		task.setId(12);

		assertNull(manager.updateTask(task));
		assertNull(manager.getTask(12));
	}

	/*
	Epic CRUD tests
	 */
	@Test
	public void task_manager_should_add_epic() {
		Epic epic = new Epic("epic", "");

		assertEquals(epic, manager.addEpic(new Epic("epic", "")));
		assertEquals(epic, manager.getEpic(1));
	}

	@Test
	public void task_manager_should_not_add_epic_when_adding_already_existing_epic() {
		Epic epic = new Epic("epic", "");
		manager.addEpic(epic);

		assertNull(manager.addEpic(epic));
		assertNull(manager.getEpic(2));
	}

	@Test
	public void task_manager_should_return_added_epic() {
		Epic epic = new Epic("epic", "");
		manager.addEpic(epic);

		assertEquals(epic, manager.getEpic(1));
	}

	@Test
	public void task_manager_should_return_null_when_getting_not_existing_epic() {
		assertNull(manager.getEpic(999));
	}

	@Test
	public void task_manager_should_return_all_epics() {
		List<Epic> epics = List.of(new Epic("1", ""),
				new Epic("2", ""));
		manager.addEpic(epics.get(0));
		manager.addEpic(epics.get(1));

		assertArrayEquals(epics.toArray(), manager.getEpics().toArray());
	}

	@Test
	public void task_manager_should_delete_epic_and_his_subtasks() {
		manager.addEpic(new Epic("", ""));
		manager.addSubtask(new Subtask(1, "1", "", NEW));
		manager.addSubtask(new Subtask(1, "2", "", NEW));
		manager.deleteEpic(1);

		assertArrayEquals(List.of().toArray(), manager.getEpics().toArray());
		assertArrayEquals(List.of().toArray(), manager.getSubtasks(1).toArray());
	}

	@Test
	public void task_manager_should_not_delete_not_existing_epic_and_should_return_null() {
		List<Epic> epics = List.of(new Epic("1", ""),
				new Epic("2", ""));
		manager.addEpic(epics.get(0));
		manager.addEpic(epics.get(1));

		assertNull(manager.deleteEpic(21));
		assertArrayEquals(epics.toArray(), manager.getEpics().toArray());
	}

	@Test
	public void task_manager_should_delete_all_epics_and_subtasks() {
		manager.addEpic(new Epic("1", ""));
		manager.addEpic(new Epic("2", ""));
		manager.addSubtask(new Subtask(1, "3", "", NEW));
		manager.addSubtask(new Subtask(1, "4", "", NEW));
		manager.deleteEpics();

		assertArrayEquals(List.of().toArray(), manager.getEpics().toArray());
		assertArrayEquals(List.of().toArray(), manager.getSubtasks(1).toArray());
	}

	@Test
	public void task_manager_should_update_epic() {
		manager.addEpic(new Epic("", ""));
		Epic epic = new Epic("", "");
		epic.setName("new name");
		epic.setId(1);
		manager.updateEpic(epic);

		assertEquals(epic, manager.getEpic(1));
	}

	@Test
	public void task_manager_should_not_update_not_existing_epic() {
		Epic epic = new Epic("", "");
		epic.setName("new name");
		epic.setId(12);

		assertNull(manager.updateEpic(epic));
		assertNull(manager.getEpic(12));
	}


	/*
	Subtask CRUD tests
	 */
	@Test
	public void task_manager_should_add_subtask() {
		manager.addEpic(new Epic("", ""));
		Subtask subtask = new Subtask(1, "", "", NEW);
		assertEquals(subtask,manager.addSubtask(new Subtask(1, "", "", NEW)));
		subtask.setId(2);

		assertEquals(subtask, manager.getSubtask(2));
	}

	@Test
	public void task_manager_should_not_add_subtask_when_adding_already_existing_subtask() {
		manager.addEpic(new Epic("", ""));
		manager.addSubtask(new Subtask(1, "", "", NEW));

		assertNull(manager.addSubtask(new Subtask(1, "", "", NEW)));
		assertNull(manager.getSubtask(3));
	}

	@Test
	public void task_manager_should_not_add_subtask_when_epic_is_not_exist() {
		assertNull(manager.addSubtask(new Subtask(1, "", "", NEW)));
		assertNull(manager.getSubtask(1));
	}

	@Test
	public void task_manager_should_return_added_subtask() {
		manager.addEpic(new Epic("", ""));
		Subtask subtask = new Subtask(1, "", "", NEW);
		manager.addSubtask(new Subtask(1, "", "", NEW));
		subtask.setId(2);

		assertEquals(subtask, manager.getSubtask(2));
	}

	@Test
	public void task_manager_should_return_null_when_getting_not_existing_subtask() {
		assertNull(manager.getSubtask(22));
	}

	@Test
	public void task_manager_should_return_all_epics_subtask() {
		manager.addEpic(new Epic("", ""));
		manager.addSubtask(new Subtask(1, "first", "", NEW));
		manager.addSubtask(new Subtask(1, "second", "", NEW));
		Subtask firstSubtask = new Subtask(1, "first", "", NEW);
		Subtask secondSubtask = new Subtask(1, "second", "", NEW);
		firstSubtask.setId(2);
		secondSubtask.setId(3);

		assertArrayEquals(List.of(firstSubtask, secondSubtask).toArray(), manager.getSubtasks(1).toArray());
	}

	@Test
	public void task_manager_should_delete_subtask() {
		manager.addEpic(new Epic("", ""));
		manager.addSubtask(new Subtask(1, "first", "", NEW));
		manager.addSubtask(new Subtask(1, "second", "", NEW));
		Subtask subtask = new Subtask(1, "first", "", NEW);
		subtask.setId(2);

		assertEquals(subtask, manager.deleteSubtask(2));
		assertNull(manager.getSubtask(2));
	}

	@Test
	public void task_manager_should_not_delete_not_existing_subtask() {
		manager.addEpic(new Epic("", ""));
		manager.addSubtask(new Subtask(1, "first", "", NEW));

		assertNull(manager.deleteSubtask(22));
		assertNull(manager.getSubtask(22));
	}

	@Test
	public void task_manager_should_update_subtask() {
		manager.addEpic(new Epic("", ""));
		manager.addSubtask(new Subtask(1, "", "", NEW));
		Subtask subtask = new Subtask(1, "updated name", "", NEW);
		subtask.setId(2);

		assertEquals(subtask, manager.updateSubtask(subtask));
	}

	@Test
	public void task_manager_should_not_update_not_existing_subtask() {
		manager.addEpic(new Epic("", ""));
		Subtask subtask = new Subtask(1, "updated name", "", NEW);
		subtask.setId(2);

		assertNull(manager.updateSubtask(subtask));
	}


	/*
	Epic status tests
	 */
	@Test
	public void epic_status_should_be_NEW_with_NEW_empty_epic() {
		Epic epic = manager.addEpic(new Epic("", ""));
		assertEquals(NEW, epic.getStatus());
	}

	@Test
	public void epic_status_should_be_NEW_with_NEW_subtask() {
		Epic epic = manager.addEpic(new Epic("", ""));
		manager.addSubtask(new Subtask(1, "subtask", "", NEW));

		assertEquals(NEW, epic.getStatus());
	}

	@Test
	public void epic_status_should_be_DONE_with_DONE_subtask() {
		Epic epic = manager.addEpic(new Epic("", ""));
		manager.addSubtask(new Subtask(1, "subtask", "", DONE));

		assertEquals(DONE, epic.getStatus());
	}

	@Test
	public void epic_status_should_be_IN_PROGRESS_with_IN_PROGRESS_subtask() {
		Epic epic = manager.addEpic(new Epic("", ""));
		manager.addSubtask(new Subtask(1, "subtask", "", IN_PROGRESS));

		assertEquals(IN_PROGRESS, epic.getStatus());
	}

	@Test
	public void epic_status_should_be_IN_PROGRESS_with_NEW_and_DONE_subtasks() {
		Epic epic = manager.addEpic(new Epic("", ""));
		manager.addSubtask(new Subtask(1, "first subtask", "", NEW));
		manager.addSubtask(new Subtask(1, "second subtask", "", DONE));

		assertEquals(IN_PROGRESS, epic.getStatus());
	}

	@Test
	public void epic_status_should_be_NEW_when_all_epic_subtasks_is_deleted() {
		Epic epic = manager.addEpic(new Epic("", ""));
		manager.addSubtask(new Subtask(1, "first subtask", "", NEW));
		manager.addSubtask(new Subtask(1, "second subtask", "", DONE));
		manager.deleteSubtask(2);
		manager.deleteSubtask(3);

		assertEquals(NEW, epic.getStatus());
	}

	@Test
	public void epic_status_should_be_DONE_with_NEW_and_DONE_subtasks_when_NEW_subtask_is_deleted() {
		Epic epic = manager.addEpic(new Epic("", ""));
		manager.addSubtask(new Subtask(1, "first subtask", "", NEW));
		manager.addSubtask(new Subtask(1, "second subtask", "", DONE));
		manager.deleteSubtask(2);

		assertEquals(DONE, epic.getStatus());
	}

	@Test
	public void epic_status_should_be_IN_PROGRESS_with_NEW_subtask_when_subtask_is_updated_on_IN_PROGRESS() {
		Epic epic = manager.addEpic(new Epic("", ""));
		manager.addSubtask(new Subtask(1, "first subtask", "", NEW));
		Subtask subtask = new Subtask(1, "updated subtask", "", IN_PROGRESS);
		subtask.setId(2);
		manager.updateSubtask(subtask);

		assertEquals(IN_PROGRESS, epic.getStatus());
	}
}
