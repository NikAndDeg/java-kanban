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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileBackedTaskManager extends InMemoryTaskManager {
	public static final String DEFAULT_SAVE_FILE = "default-save.csv";
	public static final String DEFAULT_SAVE_DIRECTORY = "resources/saves";
	private static final String FIRST_LINE = "type,id,name,description,status,epicId";

	public void loadFromFile(String path) {
		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
			serialize(reader.lines());
		} catch (FileNotFoundException exception) {
			System.out.println(exception.getMessage());
		} catch (IOException exception) {
			throw new LoadingSaveFileException(exception.getMessage(), exception.getCause());
		}
	}

	private void serialize(Stream<String> lines) {
		lines.forEach(s -> {
			if (s.startsWith(Task.class.getSimpleName()))
				serializeTask(s);
			if (s.startsWith(Epic.class.getSimpleName()))
				serializeEpic(s);
			if (s.startsWith(Subtask.class.getSimpleName()))
				serializeSubtask(s);
			if (s.startsWith("["))
				serializeHistory(s);
		});
	}

	private void serializeTask(String line) {
		Task task = getTaskFromCSVLine(line);
		idCounter++;
		tasks.put(task.getId(), task);
	}

	private Task getTaskFromCSVLine(String line) {
		String[] lineElements = line.split(",");
		Task task = new Task(lineElements[2], lineElements[3], Status.valueOf(lineElements[4]));
		task.setId(Integer.parseInt(lineElements[1]));
		return task;
	}

	private void serializeEpic(String line) {
		Epic epic = getEpicFromCSVLine(line);
		idCounter++;
		epics.put(epic.getId(), epic);
	}

	private Epic getEpicFromCSVLine(String line) {
		String[] lineElements = line.split(",");
		Epic epic = new Epic(lineElements[2], lineElements[3]);
		epic.setStatus(Status.valueOf(lineElements[4]));
		epic.setId(Integer.parseInt(lineElements[1]));
		return epic;
	}

	private void serializeSubtask(String line) {
		Subtask subtask = getSubtaskFromCSVLine(line);
		int epicId = subtask.getEpicId();
		int subtaskId = subtask.getId();
		epics.get(epicId).addSubtaskId(subtaskId);
		idCounter++;
		subtasks.put(subtask.getId(), subtask);
	}

	private Subtask getSubtaskFromCSVLine(String line) {
		String[] lineElements = line.split(",");
		Subtask subtask = new Subtask(Integer.parseInt(lineElements[5]),
				lineElements[2], lineElements[3], Status.valueOf(lineElements[4]));
		subtask.setId(Integer.parseInt(lineElements[1]));
		return subtask;
	}

	private void serializeHistory(String line) {
		if (line.equals("[]"))
			return;
		String[] lineElements = line.substring(1, line.length() - 1).split(",");
		for (String s : lineElements) {
			int id = Integer.parseInt(s.trim());
			if (tasks.containsKey(id))
				historyManager.add(tasks.get(id));
			if (epics.containsKey(id))
				historyManager.add(epics.get(id));
			if (subtasks.containsKey(id))
				historyManager.add(subtasks.get(id));
		}
	}

	public void save() {
		Path path = createDefaultSaveFile();
		try (Writer writer = new FileWriter(path.toFile())) {
			writer.write(FIRST_LINE);
			saveTasks(writer);
			saveEpics(writer);
			saveSubtasks(writer);
			saveHistory(writer);
		} catch (IOException exception) {
			throw new ManagerSaveException(exception.getMessage(), exception.getCause());
		}
	}

	private void saveTasks(Writer writer) throws IOException {
		for (Task task : tasks.values()) {
			writer.write("\n" + task.toStringForCSV());
		}
	}

	private void saveEpics(Writer writer) throws IOException {
		for (Epic epic : epics.values()) {
			writer.write("\n" + epic.toStringForCSV());
		}
	}

	private void saveSubtasks(Writer writer) throws IOException {
		for (Subtask subtask : subtasks.values()) {
			writer.write("\n" + subtask.toStringForCSV());
		}
	}

	private void saveHistory(Writer writer) throws IOException {
		writer.write("\n");
		List<Integer> history = historyManager.getHistory().stream()
				.map(Task::getId)
				.collect(Collectors.toList());
		writer.write(history.toString());
	}

	private Path createDefaultSaveFile() {
		Path path = Paths.get(DEFAULT_SAVE_DIRECTORY, DEFAULT_SAVE_FILE);
		try {
			Files.createDirectories(Paths.get(DEFAULT_SAVE_DIRECTORY));
			Files.createFile(path);
		} catch (FileAlreadyExistsException ignored) {
		} catch (IOException exception) {
			throw new CreatingSaveFileException(exception.getMessage(), exception.getCause());
		}
		return path;
	}

	public List<Subtask> getAllSubtasks() { //для тестов
		return new ArrayList<>(subtasks.values());
	}
}
