package server;

import static server.Endpoint.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import exception.SubtaskAlreadyExistException;
import exception.TimeOverlapException;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import server.adapter.DurationAdapter;
import server.adapter.LocalDateTimeAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class HttpTaskServer {
	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
	private static final Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.registerTypeAdapter(Duration.class, new DurationAdapter())
			.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
			.create();

	private final TaskManager manager;
	private final HttpServer server;

	public HttpTaskServer(TaskManager manager, int port) throws IOException {
		this.manager = manager;
		server = HttpServer.create();
		server.bind(new InetSocketAddress(port), 0);
		server.createContext("/tasks", new TasksHandler());
	}

	public void start() {
		server.start();
	}

	public void stop() {
		server.stop(1);
	}

	class TasksHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			Endpoint endpoint = getEndpoint(exchange);
			if (isTaskEndpoint(endpoint))
				handleTaskEndpoint(exchange, endpoint);
			if (isEpicEndpoint(endpoint))
				handleEpicEndpoint(exchange, endpoint);
			if (isSubtaskEndpoint(endpoint))
				handleSubtaskEndpoint(exchange, endpoint);
			if (endpoint.equals(GET_HISTORY))
				handleHistoryEndpoint(exchange);
			if (endpoint.equals(GET_PRIORITIZED_TASKS))
				handlePrioritizedTasksEndpoint(exchange);
			if (endpoint.equals(UNSUPPORTED))
				handleUnsupportedEndpoint(exchange);
		}

		private void handleUnsupportedEndpoint(HttpExchange exchange) throws IOException {
			String response = "Not Allowed.";
			int responseCode = 405;
			writeResponse(exchange, response, responseCode);
		}

		private void handlePrioritizedTasksEndpoint(HttpExchange exchange) throws IOException {
			Set<Task> prioritizedTasks = manager.getPrioritizedTasks();
			String response = gson.toJson(prioritizedTasks);
			int responseCode = 200;
			writeResponse(exchange, response, responseCode);
		}

		private void handleHistoryEndpoint(HttpExchange exchange) throws IOException {
			List<Task> history = manager.getHistory();
			String response = gson.toJson(history);
			int responseCode = 200;
			writeResponse(exchange, response, responseCode);
		}

		private void handleSubtaskEndpoint(HttpExchange exchange, Endpoint endpoint) throws IOException {
			String response = "Something went wrong.";
			int responseCode = 500;
			switch (endpoint) {
				case GET_SUBTASKS_BY_EPIC_ID:
					try {
						int epicId = Integer.parseInt(getPathParam(exchange)[1]);
						List<Subtask> subtasks = manager.getSubtasks(epicId);
						if (subtasks == null) {
							response = "Epic not found.";
							responseCode = 404;
						} else {
							response = gson.toJson(subtasks);
							responseCode = 200;
						}
					} catch (NumberFormatException exp) {
						response = "Invalid ID.";
						responseCode = 400;
					}
					break;
				case GET_SUBTASKS:
					List<Subtask> subtasks = manager.getAllSubtasks();
					response = gson.toJson(subtasks);
					responseCode = 200;
					break;
				case GET_SUBTASK_BY_ID:
					try {
						int id = Integer.parseInt(getPathParam(exchange)[1]);
						Subtask subtask = manager.getSubtask(id);
						if (subtask == null) {
							response = "Subtask not found";
							responseCode = 404;
						} else {
							response = gson.toJson(subtask);
							responseCode = 200;
						}
					} catch (NumberFormatException exp) {
						response = "Invalid ID.";
						responseCode = 400;
					}
					break;
				case POST_SUBTASK:
					try {
						Subtask subtask = getSubtaskFromBody(exchange);
						subtask = manager.addSubtask(subtask);
						if (subtask == null) {
							response = "Epic not found";
							responseCode = 404;
						} else {
							response = gson.toJson(subtask);
							responseCode = 201;
						}
					} catch (SubtaskAlreadyExistException exp) {
						response = "Subtask already exist.";
						responseCode = 400;
					} catch (IOException | JsonSyntaxException exp) {
						response = "Wrong JSON.";
						responseCode = 400;
					} catch (TimeOverlapException exp) {
						response = "Time overlap.";
						responseCode = 400;
					}
					break;
				case PATCH_SUBTASK:
					try {
						Subtask subtask = getSubtaskFromBody(exchange);
						subtask = manager.updateSubtask(subtask);
						if (subtask == null) {
							response = "Subtask not found";
							responseCode = 404;
						} else {
							response = gson.toJson(subtask);
							responseCode = 202;
						}
					} catch (IOException | JsonSyntaxException exp) {
						response = "Wrong JSON.";
						responseCode = 400;
					} catch (TimeOverlapException exp) {
						response = "Time overlap.";
						responseCode = 400;
					}
					break;
				case DELETE_SUBTASK_BY_ID:
					try {
						int id = Integer.parseInt(getPathParam(exchange)[1]);
						Subtask subtask = manager.deleteSubtask(id);
						if (subtask == null) {
							response = "Subtask nou found.";
							responseCode = 404;
						} else {
							response = gson.toJson(subtask);
							responseCode = 200;
						}
					} catch (NumberFormatException exp) {
						response = "Invalid ID.";
						responseCode = 400;
					}
					break;
			}
			writeResponse(exchange, response, responseCode);
		}

		private void handleEpicEndpoint(HttpExchange exchange, Endpoint endpoint) throws IOException {
			String response = "Something went wrong.";
			int responseCode = 500;
			switch (endpoint) {
				case GET_EPICS:
					List<Epic> epics = manager.getEpics();
					response = gson.toJson(epics);
					responseCode = 200;
					break;
				case DELETE_EPICS:
					manager.deleteEpics();
					response = "All epics have been deleted.";
					responseCode = 200;
					break;
				case GET_EPIC_BY_ID:
					try {
						int id = Integer.parseInt(getPathParam(exchange)[1]);
						Epic epic = manager.getEpic(id);
						if (epic == null) {
							response = "Epic not found.";
							responseCode = 404;
						} else {
							response = gson.toJson(epic);
							responseCode = 200;
						}
					} catch (NumberFormatException exp) {
						response = "Invalid ID.";
						responseCode = 400;
					}
					break;
				case POST_EPIC:
					try {
						Epic epic = getEpicFromBody(exchange);
						epic = manager.addEpic(epic);
						if (epic == null) {
							response = "Epic already created.";
							responseCode = 400;
						} else {
							response = gson.toJson(epic);
							responseCode = 201;
						}
					} catch (IOException | JsonSyntaxException exp) {
						response = "Invalid JSON.";
						responseCode = 400;
					}
					break;
				case PATCH_EPIC:
					try {
						Epic epic = getEpicFromBody(exchange);
						epic = manager.updateEpic(epic);
						if (epic == null) {
							response = "Epic not found.";
							responseCode = 404;
						} else {
							response = gson.toJson(epic);
							responseCode = 202;
						}
					} catch (IOException | JsonSyntaxException exp) {
						response = "Invalid JSON.";
						responseCode = 400;
					}
					break;
				case DELETE_EPIC_BY_ID:
					try {
						int id = Integer.parseInt(getPathParam(exchange)[1]);
						Epic epic = manager.deleteEpic(id);
						if (epic == null) {
							response = "Epic not found.";
							responseCode = 404;
						} else {
							response = gson.toJson(epic);
							responseCode = 200;
						}
					} catch (NumberFormatException exp) {
						response = "Invalid ID.";
						responseCode = 400;
					}
					break;
			}
			writeResponse(exchange, response, responseCode);
		}

		private void handleTaskEndpoint(HttpExchange exchange, Endpoint endpoint) throws IOException {
			String response = "Something went wrong.";
			int responseCode = 500;
			switch (endpoint) {
				case GET_TASKS:
					List<Task> tasks = manager.getTasks();
					response = gson.toJson(tasks);
					responseCode = 200;
					break;
				case DELETE_TASKS:
					manager.deleteTasks();
					response = "All tasks have been deleted.";
					responseCode = 200;
					break;
				case GET_TASK_BY_ID:
					try {
						int id = Integer.parseInt(getPathParam(exchange)[1]);
						Task task = manager.getTask(id);
						if (task == null) {
							response = "Task not found.";
							responseCode = 404;
						}
						else {
							response = gson.toJson(task);
							responseCode = 200;
						}
					} catch (NumberFormatException exp) {
						response = "Invalid ID.";
						responseCode = 400;
					}
					break;
				case POST_TASK:
					try {
						Task task = getTaskFromBody(exchange);
						task = manager.addTask(task);
						if (task == null) {
							response = "Task already added.";
							responseCode = 400;
						} else {
							response = gson.toJson(task);
							responseCode = 201;
						}
					} catch (IOException | JsonSyntaxException exp) {
						response = "Invalid JSON.";
						responseCode = 400;
					} catch (TimeOverlapException exp) {
						response = "Time overlap.";
						responseCode = 400;
					}
					break;
				case PATCH_TASK:
					try {
						Task task = getTaskFromBody(exchange);
						task = manager.updateTask(task);
						if (task == null) {
							response = "Task not found.";
							responseCode = 404;
						} else {
							response = gson.toJson(task);
							responseCode = 202;
						}
					} catch (IOException | JsonSyntaxException exp) {
						response = "Invalid JSON.";
						responseCode = 400;
					} catch (TimeOverlapException exp) {
						response = "Time overlap.";
						responseCode = 400;
					}
					break;
				case DELETE_TASK_BY_ID:
					try {
						int id = Integer.parseInt(getPathParam(exchange)[1]);
						Task task = manager.deleteTask(id);
						if (task == null) {
							response = "Task not found.";
							responseCode = 404;
						} else {
							response = gson.toJson(task);
							responseCode = 200;
						}
					} catch (NumberFormatException exp) {
						response = "Invalid task ID.";
						responseCode = 400;
						writeResponse(exchange, response, responseCode);
					}
			}
			writeResponse(exchange, response, responseCode);
		}

		private Endpoint getEndpoint(HttpExchange exchange) {
			String method = exchange.getRequestMethod();
			String[] path = exchange.getRequestURI().getPath().split("/");
			String query = exchange.getRequestURI().getQuery();
			switch (method) {
				case "GET":
					if (path.length == 2 && query == null)
						return GET_PRIORITIZED_TASKS;
					if (path.length == 3 && path[2].equals("history") && query == null)
						return GET_HISTORY;
					if (path.length == 3 && path[2].equals("task") && query == null)
						return GET_TASKS;
					if (path.length == 3 && path[2].equals("task") && query.contains("id="))
						return GET_TASK_BY_ID;
					if (path.length == 3 && path[2].equals("epic") && query == null)
						return GET_EPICS;
					if (path.length == 3 && path[2].equals("epic") && query.contains("id="))
						return GET_EPIC_BY_ID;
					if (path.length == 3 && path[2].equals("subtask") && query == null)
						return GET_SUBTASKS;
					if (path.length == 3 && path[2].equals("subtask") && query.contains("id=") && !query.contains("epic"))
						return GET_SUBTASK_BY_ID;
					if (path.length == 3 && path[2].equals("subtask") && query.contains("epicId="))
						return GET_SUBTASKS_BY_EPIC_ID;
					break;
				case "DELETE":
					if (path.length == 3 && path[2].equals("task") && query == null)
						return DELETE_TASKS;
					if (path.length == 3 && path[2].equals("task") && query.contains("id="))
						return DELETE_TASK_BY_ID;
					if (path.length == 3 && path[2].equals("epic") && query == null)
						return DELETE_EPICS;
					if (path.length == 3 && path[2].equals("epic") && query.contains("id="))
						return DELETE_EPIC_BY_ID;
					if (path.length == 3 && path[2].equals("subtask") && query.contains("id="))
						return DELETE_SUBTASK_BY_ID;
					break;
				case "POST":
					if (path.length == 3 && path[2].equals("task") && query == null)
						return POST_TASK;
					if (path.length == 3 && path[2].equals("epic") && query == null)
						return POST_EPIC;
					if (path.length == 3 && path[2].equals("subtask") && query == null)
						return POST_SUBTASK;
					break;
				case "PATCH":
					if (path.length == 3 && path[2].equals("task") && query == null)
						return PATCH_TASK;
					if (path.length == 3 && path[2].equals("epic") && query == null)
						return PATCH_EPIC;
					if (path.length == 3 && path[2].equals("subtask") && query == null)
						return PATCH_SUBTASK;
					break;
			}
			return UNSUPPORTED;
		}

		private boolean isTaskEndpoint(Endpoint endpoint) {
			return endpoint.equals(GET_TASKS) ||
					endpoint.equals(DELETE_TASKS) ||
					endpoint.equals(GET_TASK_BY_ID) ||
					endpoint.equals(POST_TASK) ||
					endpoint.equals(PATCH_TASK) ||
					endpoint.equals(DELETE_TASK_BY_ID);
		}

		private boolean isEpicEndpoint(Endpoint endpoint) {
			return endpoint.equals(GET_EPICS) ||
					endpoint.equals(DELETE_EPICS) ||
					endpoint.equals(GET_EPIC_BY_ID) ||
					endpoint.equals(POST_EPIC) ||
					endpoint.equals(PATCH_EPIC) ||
					endpoint.equals(DELETE_EPIC_BY_ID);
		}

		private boolean isSubtaskEndpoint(Endpoint endpoint) {
			return endpoint.equals(GET_SUBTASKS_BY_EPIC_ID) ||
					endpoint.equals(GET_SUBTASKS) ||
					endpoint.equals(GET_SUBTASK_BY_ID) ||
					endpoint.equals(POST_SUBTASK) ||
					endpoint.equals(PATCH_SUBTASK) ||
					endpoint.equals(DELETE_SUBTASK_BY_ID);
		}

		private String[] getPathParam(HttpExchange exchange) {
			return exchange.getRequestURI().getQuery().split("=");
		}

		private Task getTaskFromBody(HttpExchange exchange) throws IOException, JsonSyntaxException {
			String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
			return gson.fromJson(body, Task.class);
		}

		private Epic getEpicFromBody(HttpExchange exchange) throws IOException, JsonSyntaxException {
			String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
			return gson.fromJson(body, Epic.class);
		}

		private Subtask getSubtaskFromBody(HttpExchange exchange) throws IOException, JsonSyntaxException {
			String body = new String(exchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
			return gson.fromJson(body, Subtask.class);
		}

		private void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
			if(responseString.isBlank()) {
				exchange.sendResponseHeaders(responseCode, 0);
			} else {
				byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
				exchange.sendResponseHeaders(responseCode, bytes.length);
				try (OutputStream os = exchange.getResponseBody()) {
					os.write(bytes);
				}
			}
			exchange.close();
		}
	}
}
