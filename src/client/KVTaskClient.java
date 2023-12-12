package client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
	private final String URL;
	private final String apiToken;
	private final HttpClient client;
	private HttpRequest request;
	private HttpResponse<String> response;

	public KVTaskClient(String URL) throws IOException, InterruptedException {
		this.URL = URL;
		this.client = HttpClient.newHttpClient();
		this.apiToken = register();
	}

	public KVTaskClient(String URL, String apiToken) {
		this.URL = URL;
		this.client = HttpClient.newHttpClient();
		this.apiToken = apiToken;
	}

	public void put(String key, String data) throws IOException, InterruptedException {
		request = HttpRequest
				.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(data))
				.uri(URI.create(URL + "save/" + key + "?API_TOKEN=" + apiToken))
				.build();
		response = client.send(request, HttpResponse.BodyHandlers.ofString());
		int statusCode = response.statusCode();
		if (statusCode != 200) {
			throw new KVTaskClientSaveException("Save data error." +
					" Response code from server " + statusCode);
		}
	}

	public String load(String key) throws  IOException, InterruptedException {
		request = HttpRequest
				.newBuilder()
				.GET()
				.uri(URI.create(URL + "load/" + key + "?API_TOKEN=" + apiToken))
				.build();
		response = client.send(request, HttpResponse.BodyHandlers.ofString());
		int statusCode = response.statusCode();
		if (statusCode != 200) {
			throw new KVTaskClientLoadException("Load data error." +
					" Response code from server " + statusCode);
		}
		return response.body();
	}

	public String getApiToken() {
		return apiToken;
	}

	private String register() throws IOException, InterruptedException {
		request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(URL + "register"))
				.build();
		response = client.send(request, HttpResponse.BodyHandlers.ofString());
		int statusCode = response.statusCode();
		if (statusCode != 200) {
			throw new KVTaskClientRegistrationException("Client registration error." +
					" Response code from server " + statusCode);
		}
		return response.body();
	}

	static class KVTaskClientSaveException extends IOException {
		public KVTaskClientSaveException(String message) {
			super(message);
		}
	}

	static class KVTaskClientLoadException extends IOException {
		public KVTaskClientLoadException(String message) {
			super(message);
		}
	}

	static class KVTaskClientRegistrationException extends IOException {
		public KVTaskClientRegistrationException(String message) {
			super(message);
		}
	}
}
