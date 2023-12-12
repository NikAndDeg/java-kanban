package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.adapter.DurationAdapter;
import server.adapter.LocalDateTimeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {
	private static final Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(Duration.class, new DurationAdapter())
			.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
			.create();

	public static TaskManager getDefault() {
		return new InMemoryTaskManager();
	}

	public static HistoryManager getDefaultHistory() {
		return new InMemoryHistoryManager();
	}

	public static PrioritizedManager getPrioritizedManager() {
		return new PrioritizedManager();
	}

	public static Gson getGson() {
		return gson;
	}
}
