package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
	private int idCounter;
	private final HashMap<Integer, Task> tasks;
	private final HashMap<Integer, Epic> epics;
	private final HashMap<Integer, Subtask> subtasks;
	private final HistoryManager historyManager;

	public InMemoryTaskManager() {
		idCounter = 0;
		tasks = new HashMap<>();
		epics = new HashMap<>();
		subtasks = new HashMap<>();
		historyManager = Managers.getDefaultHistory();
	}

	@Override
	public List<Task> getTasks() {
		return new ArrayList<>(tasks.values());
	}

	@Override
	public void deleteTasks() {
		tasks.clear();
	}

	@Override
	public Task getTask(int id) {
		Task task = tasks.get(id);
		historyManager.add(task);
		return task;
	}

	@Override
	public Task addTask(Task task) {
		if (tasks.containsValue(task)) {
			return null;
		}
		task.setId(++idCounter);
		tasks.put(task.getId(), task);
		return task;
	}

	@Override
	public Task updateTask(Task updatedTask) {
		Task task = tasks.get(updatedTask.getId());
		if (task == null) {
			return null;
		}
		task.setName(updatedTask.getName());
		task.setDescription(updatedTask.getDescription());
		task.setStatus(updatedTask.getStatus());
		return task;
	}

	@Override
	public Task deleteTask(int id) {
		Task task = tasks.remove(id);
		historyManager.delete(task);
		return task;
	}

	@Override
	public List<Epic> getEpics() {
		return new ArrayList<>(epics.values());
	}

	@Override
	public void deleteEpics() {
		epics.clear();
		subtasks.clear();
	}

	@Override
	public Epic getEpic(int id) {
		Epic epic = epics.get(id);
		historyManager.add(epic);
		return epic;
	}

	@Override
	public Epic addEpic(Epic epic) {
		if (epics.containsValue(epic)) {
			return null;
		}
		epic.setId(++idCounter);
		epics.put(epic.getId(), epic);
		return epic;
	}

	@Override
	public Epic updateEpic(Epic updatedEpic) {
		Epic epic = epics.get(updatedEpic.getId());
		if (epic == null) {
			return null;
		}
		epic.setName(updatedEpic.getName());
		epic.setDescription(updatedEpic.getDescription());
		return epic;
	}

	@Override
	public Epic deleteEpic(int id) {
		Epic epic = epics.remove(id);
		if (epic != null) {
			deleteSubtasks(epic);
		}
		historyManager.delete(epic);
		return epic;
	}

	private void deleteSubtasks(Epic epic) {
		if (epic.getSubtasksId().isEmpty())
			return;
		for (Integer subtaskId: epic.getSubtasksId()) {
			historyManager.delete(subtasks.remove(subtaskId));
		}
	}

	@Override
	public List<Subtask> getSubtasks(int epicId) {
		Epic epic = epics.get(epicId);
		List<Subtask> epicSubtasks = new ArrayList<>();

		if (epic == null) {
			return epicSubtasks;
		}

		for (int id : epic.getSubtasksId()) {
			epicSubtasks.add(subtasks.get(id));
		}
		return epicSubtasks;
	}

	@Override
	public Subtask getSubtask(int id) {
		Subtask subtask = subtasks.get(id);
		historyManager.add(subtask);
		return subtask;
	}

	@Override
	public Subtask addSubtask(Subtask subtask) {
		Epic epic = epics.get(subtask.getEpicId());
		if (epic == null) {
			return null;
		}
		subtask.setId(++idCounter);
		subtasks.put(subtask.getId(), subtask);
		epic.addSubtaskId(subtask.getId());
		epic.setStatus(updateEpicStatusById(epic.getId()));
		return subtask;
	}

	private Status updateEpicStatusById(int id) {
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
		else {
			return Status.NEW;
		}
	}

	@Override
	public Subtask updateSubtask(Subtask updatedSubtask) {
		Subtask subtask = subtasks.get(updatedSubtask.getId());
		if (subtask == null) {
			return null;
		}
		subtask.setName(updatedSubtask.getName());
		subtask.setDescription(updatedSubtask.getDescription());
		subtask.setStatus(updatedSubtask.getStatus());
		Epic epic = epics.get(subtask.getEpicId());
		epic.setStatus(updateEpicStatusById(epic.getId()));
		return subtask;
	}

	@Override
	public Subtask deleteSubtask(int id) {
		Subtask subtask = subtasks.remove(id);
		if (subtask != null) {
			Epic epic = epics.get(subtask.getEpicId());
			epic.deleteSubtaskId(subtask.getId());
			epic.setStatus(updateEpicStatusById(epic.getId()));
		}
		historyManager.delete(subtask);
		return subtask;
	}

	@Override
	public List<Task> getHistory() {
		return historyManager.getHistory();
	}
}
