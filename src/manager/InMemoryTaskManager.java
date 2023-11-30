package manager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
	protected int idCounter;
	protected final HashMap<Integer, Task> tasks;
	protected final HashMap<Integer, Epic> epics;
	protected final HashMap<Integer, Subtask> subtasks;
	protected final HistoryManager historyManager;

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
		tasks.values().forEach(historyManager::delete);
		tasks.clear();
	}

	@Override
	public Task getTask(int id) {
		Task task = tasks.get(id);
		if (task != null)
			historyManager.add(task);

		return task;
	}

	@Override
	public Task addTask(Task task) {
		if (tasks.containsValue(task))
			return null;
		task.setId(++idCounter);
		tasks.put(task.getId(), task);

		return task;
	}

	@Override
	public Task updateTask(Task updatedTask) {
		Task task = tasks.get(updatedTask.getId());
		if (task == null)
			return null;
		task.update(updatedTask);

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
		epics.values().forEach(historyManager::delete);
		subtasks.values().forEach(historyManager::delete);
		epics.clear();
		subtasks.clear();
	}

	@Override
	public Epic getEpic(int id) {
		Epic epic = epics.get(id);
		if (epic != null)
			historyManager.add(epic);

		return epic;
	}

	@Override
	public Epic addEpic(Epic epic) {
		if (epics.containsValue(epic))
			return null;
		epic.setId(++idCounter);
		epics.put(epic.getId(), epic);

		return epic;
	}

	@Override
	public Epic updateEpic(Epic updatedEpic) {
		Epic epic = epics.get(updatedEpic.getId());
		if (epic == null)
			return null;
		epic.update(updatedEpic);

		return epic;
	}

	@Override
	public Epic deleteEpic(int id) {
		Epic epic = epics.remove(id);
		if (epic != null)
			deleteSubtasks(epic);
		historyManager.delete(epic);

		return epic;
	}

	private void deleteSubtasks(Epic epic) {
		if (epic.getSubtasksId().isEmpty())
			return;
		epic.getSubtasksId().forEach(id -> historyManager.delete(subtasks.remove(id)));
	}

	@Override
	public List<Subtask> getSubtasks(int epicId) {
		Epic epic = epics.get(epicId);
		List<Subtask> epicSubtasks = new ArrayList<>();
		if (epic == null)
			return epicSubtasks;
		epic.getSubtasksId().forEach(id -> epicSubtasks.add(subtasks.get(id)));

		return epicSubtasks;
	}

	@Override
	public List<Subtask> getAllSubtasks() {
		return new ArrayList<>(subtasks.values());
	}

	@Override
	public Subtask getSubtask(int id) {
		Subtask subtask = subtasks.get(id);
		if (subtask != null)
			historyManager.add(subtask);

		return subtask;
	}

	@Override
	public Subtask addSubtask(Subtask subtask) {
		if (subtasks.containsValue(subtask))
			return null;
		Epic epic = epics.get(subtask.getEpicId());
		if (epic == null)
			return null;
		subtask.setId(++idCounter);
		subtasks.put(subtask.getId(), subtask);
		epic.addSubtaskId(subtask.getId());
		updateEpicStatus(epic);
		updateEpicTime(epic);

		return subtask;
	}

	private Status getEpicStatusById(int id) {
		List<Subtask> epicSubtasks = getSubtasks(id);
		int statusDoneCounter = 0;
		for (Subtask subtask : epicSubtasks) {
			if (subtask.getStatus() == Status.IN_PROGRESS)
				return Status.IN_PROGRESS;
			if (subtask.getStatus() == Status.DONE)
				statusDoneCounter++;
		}
		if (statusDoneCounter == 0)
			return Status.NEW;
		if (epicSubtasks.size() == statusDoneCounter)
			return Status.DONE;
		return Status.IN_PROGRESS;
	}

	private void updateEpicStatus(Epic epic) {
		epic.setStatus(getEpicStatusById(epic.getId()));
	}

	@Override
	public Subtask updateSubtask(Subtask updatedSubtask) {
		Subtask subtask = subtasks.get(updatedSubtask.getId());
		if (subtask == null)
			return null;
		subtask.update(updatedSubtask);
		Epic epic = epics.get(subtask.getEpicId());
		updateEpicStatus(epic);
		updateEpicTime(epic);

		return subtask;
	}

	private void updateEpicTime(Epic epic) {
		List<Subtask> subtasks = getSubtasks(epic.getId());
		if (subtasks.isEmpty()) {
			epic.setStartTime(Epic.DEFAULT_TIME);
			epic.setEndTime(Epic.DEFAULT_TIME);
			epic.setDuration(Duration.ZERO);
		} else if (subtasks.size() > 1) {
			subtasks.sort(Comparator.comparing(Task::getStartTime));
			epic.setStartTime(subtasks.get(0).getStartTime());

			subtasks.sort(Comparator.comparing(Task::getEndTime));
			epic.setEndTime(subtasks.get(subtasks.size() - 1).getEndTime());

			long durationInSeconds = subtasks.stream()
					.map(s -> s.getDuration().toSeconds())
					.mapToLong(Long::longValue)
					.sum();
			epic.setDuration(Duration.ofSeconds(durationInSeconds));
		} else {
			epic.setStartTime(subtasks.get(0).getStartTime());
			epic.setDuration(subtasks.get(0).getDuration());
			epic.setEndTime(subtasks.get(0).getEndTime());
		}
	}

	@Override
	public Subtask deleteSubtask(int id) {
		Subtask subtask = subtasks.remove(id);
		if (subtask != null) {
			Epic epic = epics.get(subtask.getEpicId());
			epic.deleteSubtaskId(subtask.getId());
			updateEpicStatus(epic);
			updateEpicTime(epic);
		}
		historyManager.delete(subtask);

		return subtask;
	}

	@Override
	public List<Task> getHistory() {
		return historyManager.getHistory();
	}

	@Override
	public Set<Task> getPrioritizedTasks() {
		return new HashSet<>();
	}
}
