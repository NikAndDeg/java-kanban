package manager;

import client.KVTaskClient;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;
import server.adapter.DurationAdapter;
import server.adapter.LocalDateTimeAdapter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class HttpTaskManager extends FileBackedTaskManager{
	private static final Gson gson = new GsonBuilder()
			.registerTypeAdapter(Duration.class, new DurationAdapter())
			.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
			.create();
	private static final String TASK_KEY = "task";
	private static final String EPIC_KEY = "epic";
	private static final String SUBTASK_KEY = "subtask";
	private static final String HISTORY_KEY = "history";
	private static final String PRIORITIZED_KEY = "prioritized";
	private static final String COUNTER_KEY = "counter";

	private final static Type TYPE_HASH_MAP_INTEGER_TASK = new TypeToken<HashMap<Integer, Task>>(){}.getType();
	private final static Type TYPE_HASH_MAP_INTEGER_EPIC = new TypeToken<HashMap<Integer, Epic>>(){}.getType();
	private final static Type TYPE_HASH_MAP_INTEGER_SUBTASK = new TypeToken<HashMap<Integer, Subtask>>(){}.getType();

	private final KVTaskClient client;

	public HttpTaskManager(String URL) throws IOException, InterruptedException {
		super(null);
		this.client = new KVTaskClient(URL);
	}

	@Override
	protected void save() {
		try {
			client.put(COUNTER_KEY, gson.toJson(idCounter));
			client.put(TASK_KEY, gson.toJson(tasks));
			client.put(EPIC_KEY, gson.toJson(epics));
			client.put(SUBTASK_KEY, gson.toJson(subtasks));
			client.put(HISTORY_KEY, gson.toJson(historyManager.getHistory()));
			client.put(PRIORITIZED_KEY, gson.toJson(prioritizedManager.getPrioritizedTasks()));
		} catch (IOException | InterruptedException exp) {
			System.out.println("Saving error.");
			System.out.println(exp.getMessage());
		}
	}

	public static HttpTaskManager load(String URL, String apiToken) {
		try {
			HttpTaskManager manager = new HttpTaskManager(URL);
			manager.idCounter = gson.fromJson(manager.client.load(COUNTER_KEY), Integer.class);
			manager.tasks = gson.fromJson(manager.client.load(TASK_KEY), TYPE_HASH_MAP_INTEGER_TASK);
			manager.epics = gson.fromJson(manager.client.load(EPIC_KEY), TYPE_HASH_MAP_INTEGER_EPIC);
			manager.subtasks = gson.fromJson(manager.client.load(SUBTASK_KEY), TYPE_HASH_MAP_INTEGER_SUBTASK);
			loadHistory(manager);
			loadPrioritizedTasks(manager);
			return manager;
		} catch (IOException | InterruptedException exp) {
			System.out.println("Loading error.");
			System.out.println(exp.getMessage());
			return null;
		}
	}

	public String getClientAPIToken() {
		return client.getApiToken();
	}

	private static void loadHistory(HttpTaskManager manager) throws IOException, InterruptedException {
		JsonElement jsonElement = JsonParser.parseString(manager.client.load(HISTORY_KEY));
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		jsonArray.forEach(jE -> {
			JsonObject jsonObject = jE.getAsJsonObject();
			if (jsonObject.get("epicId") != null)
				manager.historyManager.add(gson.fromJson(jsonObject, Subtask.class));
			else if (jsonObject.get("subtasksId") != null)
				manager.historyManager.add(gson.fromJson(jsonObject, Epic.class));
			else
				manager.historyManager.add(gson.fromJson(jsonObject, Task.class));
		});
	}

	private static void loadPrioritizedTasks(HttpTaskManager manager) throws IOException, InterruptedException {
		JsonElement jsonElement = JsonParser.parseString(manager.client.load(PRIORITIZED_KEY));
		JsonArray jsonArray = jsonElement.getAsJsonArray();
		jsonArray.forEach(jE -> {
			JsonObject jsonObject = jE.getAsJsonObject();
			if (jsonObject.get("epicId") != null)
				manager.prioritizedManager.add(gson.fromJson(jsonObject, Subtask.class));
			else
				manager.prioritizedManager.add(gson.fromJson(jsonObject, Task.class));
		});
	}

	public static FileBackedTaskManager loadFromFile(File saveFile) {
		System.out.println("Method not supported.");
		return null;
	}
}
