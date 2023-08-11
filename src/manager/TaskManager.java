package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {

	public List<Task> getTasks();
	public void deleteTasks();
	public Task getTask(int id);
	public Task addTask(Task task);
	public Task updateTask(Task updatedTask);
	public Task deleteTask(int id);

	public List<Epic> getEpics();
	public void deleteEpics();
	public Epic getEpic(int id);
	public Epic addEpic(Epic epic);
	public Epic updateEpic(Epic updatedEpic);
	public Epic deleteEpic(int id);

	public List<Subtask> getSubtasks(int epicId);
	public Subtask getSubtask(int id);
	public Subtask addSubtask(Subtask subtask);
	public Subtask updateSubtask(Subtask updatedSubtask);
	public Subtask deleteSubtask(int id);

	public List<Task> getHistory();
}
