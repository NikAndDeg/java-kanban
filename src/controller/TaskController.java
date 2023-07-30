package controller;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskController implements Controller<Task> {
	private int idCounter;
	HashMap<Integer, Task> tasks;

	public TaskController() {
		idCounter = 0;
		tasks = new HashMap<>();
	}

	@Override
	public List<Task> getAll() {
		return new ArrayList<>(tasks.values());
	}

	@Override
	public void deleteAll() {
		tasks = new HashMap<>();
	}

	@Override
	public Task getById(int id) {
		return tasks.remove(id);
	}

	@Override
	public Task add(Task task) {
		if (tasks.containsValue(task))
			return null;
		task.setId(++idCounter);
		tasks.put(task.getId(), task);
		return task;
	}

	@Override
	public Task update(Task updatedTask) {
		Task task = tasks.get(updatedTask.getId());
		if (task == null)
			return null;
		task.setName(updatedTask.getName());
		task.setDescription(updatedTask.getDescription());
		task.setStatus(updatedTask.getStatus());
		return task;
	}

	@Override
	public Task delete(int id) {
		return tasks.remove(id);
	}
}
