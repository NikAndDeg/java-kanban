package manager;

import exception.CreatingSaveFileException;
import exception.LoadingSaveFileException;
import exception.ManagerSaveException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBackedTaskManager extends InMemoryTaskManager {
	public static void main(String[] args) {
		String defaultSaveDirectory = "resources/saves";
		String defaultSaveFile = "default-save.csv";

		File saveFile = new File(defaultSaveDirectory, defaultSaveFile);

		FileBackedTaskManager tm = new FileBackedTaskManager(saveFile);

		tm.addTask(new Task("Task 1", "description", Status.NEW));
		tm.addTask(new Task("Task 2", "description", Status.IN_PROGRESS));
		tm.addEpic(new Epic("Epic 1", ""));
		tm.addEpic(new Epic("Epic 2", "description"));
		tm.addSubtask(new Subtask(3, "Subtask 1", "description", Status.DONE));
		tm.addSubtask(new Subtask(3, "Subtask 2", "", Status.IN_PROGRESS));
		tm.addSubtask(new Subtask(3, "Subtask 3", "", Status.NEW));
		tm.addSubtask(new Subtask(4, "Subtask 4", "", Status.NEW));
		tm.addSubtask(new Subtask(4, "Subtask 5", "", Status.NEW));

		tm.getTask(1);
		tm.getEpic(4);
		tm.getSubtask(7);

		System.out.println(tm.getTasks());
		System.out.println(tm.getEpics());
		System.out.println(tm.getAllSubtasks());
		tm.getHistory().forEach(System.out::print);

		System.out.println();
		System.out.println();

		tm = FileBackedTaskManager.loadFromFile(saveFile);

		System.out.println(tm.getTasks());
		System.out.println(tm.getEpics());
		System.out.println(tm.getAllSubtasks());
		tm.getHistory().forEach(System.out::print);
	}

	private static final String FIRST_LINE = "type,id,name,description,status,epicId";
	private final File saveFile;

	public FileBackedTaskManager(File saveFile) {
		super();
		this.saveFile = saveFile;
	}

	@Override
	public void deleteTasks() {
		super.deleteTasks();
		save();
	}

	@Override
	public Task getTask(int id) {
		Task t = super.getTask(id);
		save();
		return t;
	}

	@Override
	public Task addTask(Task task) {
		Task t = super.addTask(task);
		save();
		return t;
	}

	@Override
	public Task updateTask(Task updatedTask) {
		Task t = super.updateTask(updatedTask);
		save();
		return t;
	}

	@Override
	public Task deleteTask(int id) {
		Task t = super.deleteTask(id);
		save();
		return t;
	}

	@Override
	public void deleteEpics() {
		super.deleteEpics();
		save();
	}

	@Override
	public Epic getEpic(int id) {
		Epic e= super.getEpic(id);
		save();
		return e;
	}

	@Override
	public Epic addEpic(Epic epic) {
		Epic e = super.addEpic(epic);
		save();
		return e;
	}

	@Override
	public Epic updateEpic(Epic updatedEpic) {
		Epic e = super.updateEpic(updatedEpic);
		save();
		return e;
	}

	@Override
	public Epic deleteEpic(int id) {
		Epic e = super.deleteEpic(id);
		save();
		return e;
	}

	@Override
	public Subtask getSubtask(int id) {
		Subtask s = super.getSubtask(id);
		save();
		return s;
	}

	@Override
	public Subtask addSubtask(Subtask subtask) {
		Subtask s = super.addSubtask(subtask);
		save();
		return s;
	}

	@Override
	public Subtask updateSubtask(Subtask updatedSubtask) {
		Subtask s = super.updateSubtask(updatedSubtask);
		save();
		return s;
	}

	@Override
	public Subtask deleteSubtask(int id) {
		Subtask s = super.deleteSubtask(id);
		save();
		return s;
	}

	public static FileBackedTaskManager loadFromFile(File saveFile) {
		FileBackedTaskManager manager = new FileBackedTaskManager(saveFile);
		try (BufferedReader reader = new BufferedReader(new FileReader(saveFile.getPath()))) {
			manager.uploadToManager(reader.lines());
		} catch (FileNotFoundException exception) {
			System.out.println(exception.getMessage());
		} catch (IOException exception) {
			throw new LoadingSaveFileException(exception.getMessage(), exception.getCause());
		}
		return manager;
	}

	private void uploadToManager(Stream<String> lines) {
		lines.forEach(s -> {
			if (s.startsWith(Task.class.getSimpleName())) {
				uploadTask(s);
			}
			if (s.startsWith(Epic.class.getSimpleName()))
				uploadEpic(s);
			if (s.startsWith(Subtask.class.getSimpleName()))
				uploadSubtask(s);
			if (s.startsWith("["))
				uploadHistory(s);
		});
	}

	private void uploadTask(String line) {
		Task task = fromString(line);
		tasks.put(task.getId(), task);
		countLoadedTask();
	}

	private void countLoadedTask() {
		idCounter++;
	}

	private void uploadEpic(String line) {
		Epic epic = (Epic) fromString(line);
		epics.put(epic.getId(), epic);
		countLoadedTask();
	}

	private void uploadSubtask(String line) {
		Subtask subtask = (Subtask) fromString(line);
		int epicId = subtask.getEpicId();
		int subtaskId = subtask.getId();
		epics.get(epicId).addSubtaskId(subtaskId);
		subtasks.put(subtask.getId(), subtask);
		countLoadedTask();
	}

	private Task fromString(String line) {
		String[] lineElements = line.split(",");
		String taskClass = lineElements[0];
		if (taskClass.equals(Task.class.getSimpleName())) {
			Task task = new Task(lineElements[2], lineElements[3], Status.valueOf(lineElements[4]));
			task.setId(Integer.parseInt(lineElements[1]));
			return task;
		}
		if (taskClass.equals(Epic.class.getSimpleName())) {
			Epic epic = new Epic(lineElements[2], lineElements[3]);
			epic.setStatus(Status.valueOf(lineElements[4]));
			epic.setId(Integer.parseInt(lineElements[1]));
			return epic;
		}
		Subtask subtask = new Subtask(Integer.parseInt(lineElements[5]),
				lineElements[2], lineElements[3], Status.valueOf(lineElements[4]));
		subtask.setId(Integer.parseInt(lineElements[1]));
		return subtask;
	}

	private void uploadHistory(String line) {
		if (line.equals("[]"))
			return;
		historyFromString(line).forEach(id -> {
			if (tasks.containsKey(id))
				historyManager.add(tasks.get(id));
			if (epics.containsKey(id))
				historyManager.add(epics.get(id));
			if (subtasks.containsKey(id))
				historyManager.add(subtasks.get(id));
		});
	}

	private static List<Integer> historyFromString(String line) {
		String[] lineElements = line.substring(1, line.length() - 1).split(",");
		return Arrays.stream(lineElements)
				.map(s -> Integer.parseInt(s.trim()))
				.collect(Collectors.toList());
	}

	private void save() {
		Path path = createSaveFile(Paths.get(saveFile.getPath()));
		try (Writer writer = new FileWriter(path.toFile())) {
			writer.write(FIRST_LINE);
			for (Task task : tasks.values()) {
				writer.write("\n" + toString(task));
			}
			for (Epic epic : epics.values()) {
				writer.write("\n" + toString(epic));
			}
			for (Subtask subtask : subtasks.values()) {
				writer.write("\n" + toString(subtask));
			}
			writer.write("\n" + historyToString(historyManager));
		} catch (IOException exception) {
			throw new ManagerSaveException(exception.getMessage(), exception.getCause());
		}
	}

	private Path createSaveFile(Path path) {
		try {
			Files.createDirectories(path.getParent());
			Files.createFile(path);
		} catch (FileAlreadyExistsException ignored) {
		} catch (IOException exception) {
			throw new CreatingSaveFileException(exception.getMessage(), exception.getCause());
		}
		return path;
	}

	private String toString(Task task) {
		if (task instanceof Subtask) {
			Subtask subtask = (Subtask) task;
			return subtask.getClass().getSimpleName() +
					"," + subtask.getId() +
					"," + subtask.getName() +
					"," + subtask.getDescription() +
					"," + subtask.getStatus() +
					"," + subtask.getEpicId();
		}
		return task.getClass().getSimpleName() +
				"," + task.getId() +
				"," + task.getName() +
				"," + task.getDescription() +
				"," + task.getStatus();
	}

	private static String historyToString(HistoryManager manager) {
		return manager.getHistory().stream()
				.map(Task::getId)
				.collect(Collectors.toList())
				.toString();
	}
}
