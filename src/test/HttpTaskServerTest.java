package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.InMemoryTaskManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.*;
import server.HttpTaskServer;
import server.adapter.DurationAdapter;
import server.adapter.LocalDateTimeAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
	HttpTaskServer server;
	HttpClient client = HttpClient.newHttpClient();
	String url = "http://localhost:8080/tasks";
	HttpRequest request;
	Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(Duration.class, new DurationAdapter())
			.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
			.create();
	URI taskURI = URI.create(url + "/task/");

	@BeforeEach
	void createAndStartServer() throws IOException {
		server = new HttpTaskServer(new InMemoryTaskManager(), 8080);
		server.start();
	}

	@AfterEach
	void stopServer() {
		server.stop();
	}

	@Test
	void post_task() {
		Task task = new Task("task", "", Status.NEW);
		request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
				.uri(taskURI)
				.build();
		task.setId(1);
		assertCodeAndBody(201, gson.toJson(task), sendRequest(request));
	}

	@Test
	void post_task_incorrect_JSON() {
		request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson("{incorrect JSON}")))
				.uri(taskURI)
				.build();
		assertCodeAndBody(400, "Invalid JSON.", sendRequest(request));
	}

	@Test
	void get_tasks() {
		Task task = new Task("task", "", Status.NEW);
		request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
				.uri(taskURI)
				.build();
		sendRequest(request);

		Task task2 = new Task("task2", "", Status.NEW);
		request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2)))
				.uri(taskURI)
				.build();
		sendRequest(request);

		request = HttpRequest.newBuilder()
				.GET()
				.uri(taskURI)
				.build();
		task.setId(1);
		task2.setId(2);
		assertCodeAndBody(200, gson.toJson(List.of(task, task2)), sendRequest(request));
	}

	@Test
	void get_task() {
		Task task = new Task("task", "", Status.NEW);
		request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
				.uri(taskURI)
				.build();
		sendRequest(request);

		request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(taskURI.toString() + "?id=1"))
				.build();
		task.setId(1);
		assertCodeAndBody(200, gson.toJson(task), sendRequest(request));
	}

	@Test
	void get_task_incorrect_id() {
		request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(taskURI.toString() + "?id=1"))
				.build();
		assertCodeAndBody(404, "Task not found.", sendRequest(request));
	}

	@Test
	void delete_tasks() {
		Task task = new Task("task", "", Status.NEW);
		request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
				.uri(taskURI)
				.build();
		sendRequest(request);

		request = HttpRequest.newBuilder()
				.DELETE()
				.uri(taskURI)
				.build();
		assertCodeAndBody(200, "All tasks have been deleted.", sendRequest(request));
	}

	@Test
	void delete_task() {
		Task task = new Task("task", "", Status.NEW);
		request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
				.uri(taskURI)
				.build();
		sendRequest(request);

		request = HttpRequest.newBuilder()
				.DELETE()
				.uri(URI.create(taskURI.toString() + "/?id=1"))
				.build();
		task.setId(1);
		assertCodeAndBody(200, gson.toJson(task), sendRequest(request));
	}

	@Test
	void update_task() {
		Task task = new Task("task", "", Status.NEW);
		request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
				.uri(taskURI)
				.build();
		sendRequest(request);

		task.setId(1);
		task.setName("UpdatedName");
		request = HttpRequest.newBuilder()
				.method("PATCH", HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
				.uri(taskURI)
				.build();
		assertCodeAndBody(202, gson.toJson(task), sendRequest(request));
	}

	@Test
	void update_wrong_task() {
		Task task = new Task("task", "", Status.NEW);
		request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
				.uri(taskURI)
				.build();
		sendRequest(request);

		task.setId(999);
		task.setName("UpdatedName");
		request = HttpRequest.newBuilder()
				.method("PATCH", HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
				.uri(taskURI)
				.build();
		assertCodeAndBody(404, "Task not found.", sendRequest(request));
	}

	private void assertCodeAndBody(int code, String body, HttpResponse<String> response) {
		assertEquals(code, response.statusCode());
		if (body != null)
			assertEquals(body, response.body());
	}

	private HttpResponse<String> sendRequest(HttpRequest request) {
		try {
			return client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException exp) {
			System.out.println("Oh no, some shit happened!");
			return null;
		}
	}
}