package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
	private int idCounter;
	private final HashMap<Integer, Task> tasks;
	private final HashMap<Integer, Epic> epics;
	private final HashMap<Integer, Subtask> subtasks;

	public TaskManager() {
		idCounter = 0;
		tasks = new HashMap<>();
		epics = new HashMap<>();
		subtasks = new HashMap<>();
	}

	public List<Task> getTasks() {
		return new ArrayList<>(tasks.values());
	}

	public void deleteTasks() {
		tasks.clear();
	}

	public Task getTask(int id) {
		return tasks.remove(id);
	}

	public Task addTask(Task task) {
		if (tasks.containsValue(task)) {
			return null;
		}
		task.setId(++idCounter);
		tasks.put(task.getId(), task);
		return task;
	}

	public Task update(Task updatedTask) {
		Task task = tasks.get(updatedTask.getId());
		if (task == null) {
			return null;
		}
		task.setName(updatedTask.getName());
		task.setDescription(updatedTask.getDescription());
		task.setStatus(updatedTask.getStatus());
		return task;
	}

	public Task deleteTask(int id) {
		return tasks.remove(id);
	}

	public List<Epic> getEpics() {
		return new ArrayList<>(epics.values());
	}

	public void deleteEpics() {
		epics.clear();
		subtasks.clear();
	}

	public Epic getEpic(int id) {
		return epics.get(id);
	}

	public Epic addEpic(Epic epic) {
		if (epics.containsValue(epic)) {
			return null;
		}
		epic.setId(++idCounter);
		epics.put(epic.getId(), epic);
		return epic;
	}

	public Epic update(Epic updatedEpic) {
		Epic epic = epics.get(updatedEpic.getId());
		if (epic == null) {
			return null;
		}
		epic.setName(updatedEpic.getName());
		epic.setDescription(updatedEpic.getDescription());
		return epic;
	}

	public Epic deleteEpic(int id) {
		Epic epic = epics.remove(id);
		if (epic != null) {
			deleteSubtasks(id);
		}
		return epic;
	}

	private void deleteSubtasks(int epicId) {
		for (Subtask s : subtasks.values()) {
			if (s.getEpicId() == epicId) {
				subtasks.remove(s.getId());
			}
		}
	}

	public List<Subtask> getSubtasks(int epicId) {
		return subtasks.values().stream()
				.filter(subtask -> subtask.getEpicId() == epicId)
				.collect(Collectors.toList());
	}

	public Subtask getSubtask(int id) {
		return subtasks.get(id);
	}

	public Subtask addSubtask(Subtask subtask) {
		Epic epic = epics.get(subtask.getEpicId());
		if (epic == null) {
			return null;
		}
		subtask.setId(++idCounter);
		subtasks.put(subtask.getId(), subtask);
		epic.setStatus(findEpicStatusById(epic.getId()));
		epic.addSubtaskId(subtask.getId());
		return subtask;
	}

	private Status findEpicStatusById(int id) {
		List<Subtask> epicSubtasks = getSubtasks(id);
		int statusDoneCounter = 0;
		for (Subtask subtask : epicSubtasks) {
			if (subtask.getStatus() == Status.IN_PROGRESS) {
				return Status.IN_PROGRESS;
			}
			if (subtask.getStatus() == Status.DONE) {
				statusDoneCounter++;
			}
		}
		if (epicSubtasks.size() == statusDoneCounter) {
			return Status.DONE;
		}
		else return Status.NEW;
	}

	public Subtask update(Subtask updatedSubtask) {
		Subtask subtask = subtasks.get(updatedSubtask.getId());
		if (subtask == null) {
			return null;
		}
		subtask.setName(updatedSubtask.getName());
		subtask.setDescription(updatedSubtask.getDescription());
		subtask.setStatus(updatedSubtask.getStatus());
		Epic epic = epics.get(subtask.getEpicId());
		epic.setStatus(findEpicStatusById(epic.getId()));
		return subtask;
	}

	public Subtask deleteSubtask(int id) {
		Subtask subtask = subtasks.remove(id);
		if (subtask != null) {
			Epic epic = epics.get(subtask.getEpicId());
			epic.setStatus(findEpicStatusById(epic.getId()));
			epic.deleteSubtaskId(subtask.getId());
		}
		return subtask;
	}
}
