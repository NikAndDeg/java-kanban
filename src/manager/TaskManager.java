package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {

	List<Task> getTasks();
	void deleteTasks();
	Task getTask(int id);
	Task addTask(Task task);
	Task updateTask(Task updatedTask);
	Task deleteTask(int id);

	List<Epic> getEpics();
	void deleteEpics();
	Epic getEpic(int id);
	Epic addEpic(Epic epic);
	Epic updateEpic(Epic updatedEpic);
	Epic deleteEpic(int id);

	List<Subtask> getSubtasks(int epicId);
	Subtask getSubtask(int id);
	Subtask addSubtask(Subtask subtask);
	Subtask updateSubtask(Subtask updatedSubtask);
	Subtask deleteSubtask(int id);

	List<Task> getHistory();
}
