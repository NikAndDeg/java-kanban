package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import model.Epic;
import model.Status;
import model.Subtask;

/*
Epic и Subtask неразрывные сущности, поэтому контроллер для них один.
 */
public class EpicController implements Controller<Epic>, SubtaskController {
	private int idCounter;
	private final HashMap<Integer, Epic> epics;
	private final HashMap<Integer, Subtask> subtasks;

	public EpicController() {
		idCounter = 0;
		epics = new HashMap<>();
		subtasks = new HashMap<>();
	}

	@Override
	public List<Epic> getAll() {
		return new ArrayList<>(epics.values());
	}

	@Override
	public void deleteAll() {
		epics.clear();
		subtasks.clear();
	}

	@Override
	public Epic getById(int id) {
		return epics.get(id);
	}

	@Override
	public Epic add(Epic epic) {
		if (epics.containsValue(epic))
			return null;
		epic.setId(++idCounter);
		epics.put(epic.getId(), epic);
		return epic;
	}

	@Override
	public Epic update(Epic updatedEpic) {
		Epic epic = epics.get(updatedEpic.getId());
		if (epic == null)
			return null;
		epic.setName(updatedEpic.getName());
		epic.setDescription(updatedEpic.getDescription());
		return epic;
	}

	@Override
	public Epic delete(int id) {
		Epic epic = epics.remove(id);
		if (epic != null)
			deleteAllByEpicId(id);
		return epics.remove(id);
	}

	private void deleteAllByEpicId(int epicId) {
		for (Subtask s : subtasks.values()) {
			if (s.getEpicId() == epicId)
				subtasks.remove(s.getId());
		}
	}

	@Override
	public List<Subtask> getAllByEpicId(int epicId) {
		return subtasks.values().stream()
				.filter(subtask -> subtask.getEpicId() == epicId)
				.collect(Collectors.toList());
	}

	@Override
	public Subtask getSubtaskById(int id) {
		return subtasks.get(id);
	}

	@Override
	public Subtask add(Subtask subtask) {
		Epic epic = epics.get(subtask.getEpicId());
		if (epic == null)
			return null;
		subtask.setId(++idCounter);
		subtasks.put(subtask.getId(), subtask);
		epic.setStatus(findEpicStatusById(epic.getId()));
		return subtask;
	}

	private Status findEpicStatusById(int id) {
		List<Subtask> epicSubtasks = getAllByEpicId(id);
		int statusDoneCounter = 0;
		for (Subtask subtask : epicSubtasks) {
			if (subtask.getStatus() == Status.IN_PROGRESS)
				return Status.IN_PROGRESS;
			if (subtask.getStatus() == Status.DONE) {
				statusDoneCounter++;
			}
		}
		if (epicSubtasks.size() == statusDoneCounter)
			return Status.DONE;
		else return Status.NEW;
	}

	@Override
	public Subtask update(Subtask updatedSubtask) {
		Subtask subtask = subtasks.get(updatedSubtask.getId());
		if (subtask == null)
			return null;
		subtask.setName(updatedSubtask.getName());
		subtask.setDescription(updatedSubtask.getDescription());
		subtask.setStatus(updatedSubtask.getStatus());
		Epic epic = epics.get(subtask.getEpicId());
		epic.setStatus(findEpicStatusById(epic.getId()));
		return subtask;
	}

	@Override
	public Subtask deleteSubtask(int id) {
		Subtask subtask = subtasks.remove(id);
		if (subtask != null) {
			Epic epic = epics.get(subtask.getEpicId());
			epic.setStatus(findEpicStatusById(epic.getId()));
		}
		return subtask;
	}
}
