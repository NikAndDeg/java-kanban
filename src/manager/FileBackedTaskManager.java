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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBackedTaskManager extends InMemoryTaskManager {

	private static final String FIRST_LINE = "type,id,name,description,status,duration,startTime,endTime,epicId";
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
			uploadToManager(reader.lines(), manager);
		} catch (FileNotFoundException exception) {
			System.out.println(exception.getMessage());
		} catch (IOException exception) {
			throw new LoadingSaveFileException(exception.getMessage(), exception.getCause());
		}
		return manager;
	}

	private static void uploadToManager(Stream<String> lines, FileBackedTaskManager manager) {
		lines.forEach(s -> {
			if (s.startsWith(Task.class.getSimpleName())) {
				uploadTask(s, manager);
			}
			if (s.startsWith(Epic.class.getSimpleName()))
				uploadEpic(s, manager);
			if (s.startsWith(Subtask.class.getSimpleName()))
				uploadSubtask(s, manager);
			if (s.startsWith("["))
				uploadHistory(s, manager);
		});
	}

	private static void uploadTask(String line, FileBackedTaskManager manager) {
		Task task = fromString(line);
		manager.idCounter++;
		manager.tasks.put(task.getId(), task);
	}

	private static void uploadEpic(String line, FileBackedTaskManager manager) {
		Epic epic = (Epic) fromString(line);
		manager.idCounter++;
		manager.epics.put(epic.getId(), epic);
	}

	private static void uploadSubtask(String line, FileBackedTaskManager manager) {
		Subtask subtask = (Subtask) fromString(line);
		int epicId = subtask.getEpicId();
		int subtaskId = subtask.getId();
		manager.epics.get(epicId).addSubtaskId(subtaskId);
		manager.idCounter++;
		manager.subtasks.put(subtask.getId(), subtask);
	}

	private static Task fromString(String line) {
		String[] lineElements = line.split(",");
		if (lineElements[0].equals(Task.class.getSimpleName())) {
			Task task = new Task(lineElements[2],
					lineElements[3],
					Status.valueOf(lineElements[4]),
					Duration.parse(lineElements[5]),
					LocalDateTime.parse(lineElements[6]));
			task.setId(Integer.parseInt(lineElements[1]));
			return task;
		}
		if (lineElements[0].equals(Epic.class.getSimpleName())) {
			Epic epic = new Epic(lineElements[2], lineElements[3]);
			epic.setStatus(Status.valueOf(lineElements[4]));
			epic.setId(Integer.parseInt(lineElements[1]));
			if (lineElements[5].equals("null"))
				epic.setDuration(null);
			else
				epic.setDuration(Duration.parse(lineElements[5]));
			if (lineElements[6].equals("null"))
				epic.setStartTime(null);
			else
				epic.setStartTime(LocalDateTime.parse(lineElements[6]));
			if (lineElements[7].equals("null"))
				epic.setEndTime(null);
			else
				epic.setEndTime(LocalDateTime.parse(lineElements[7]));
			return epic;
		}
		Subtask subtask = new Subtask(Integer.parseInt(lineElements[8]),
				lineElements[2],
				lineElements[3],
				Status.valueOf(lineElements[4]),
				Duration.parse(lineElements[5]),
				LocalDateTime.parse(lineElements[6]));
		subtask.setId(Integer.parseInt(lineElements[1]));
		return subtask;
	}

	private static void uploadHistory(String line, FileBackedTaskManager manager) {
		if (line.equals("[]"))
			return;
		String[] lineElements = line.substring(1, line.length() - 1).split(",");
		for (String s : lineElements) {
			int id = Integer.parseInt(s.trim());
			if (manager.tasks.containsKey(id))
				manager.historyManager.add(manager.tasks.get(id));
			if (manager.epics.containsKey(id))
				manager.historyManager.add(manager.epics.get(id));
			if (manager.subtasks.containsKey(id))
				manager.historyManager.add(manager.subtasks.get(id));
		}
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
			saveHistory(writer);
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
					"," + task.getDuration() +
					"," + task.getStartTime() +
					"," + task.getEndTime() +
					"," + subtask.getEpicId();
		}
		return task.getClass().getSimpleName() +
				"," + task.getId() +
				"," + task.getName() +
				"," + task.getDescription() +
				"," + task.getStatus() +
				"," + task.getDuration() +
				"," + task.getStartTime() +
				"," + task.getEndTime();
	}

	private void saveHistory(Writer writer) throws IOException {
		writer.write("\n");
		List<Integer> history = historyManager.getHistory().stream()
				.map(Task::getId)
				.collect(Collectors.toList());
		writer.write(history.toString());
	}
}
