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
		client = HttpClient.newHttpClient();
		request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create(URL + "register"))
				.build();
		response = client.send(request, HttpResponse.BodyHandlers.ofString());
		this.apiToken = response.body();
	}

	public void put(String key, String data) throws IOException, InterruptedException {
		request = HttpRequest
				.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(data))
				.uri(URI.create(URL + "save/" + key + "?API_TOKEN=" + apiToken))
				.build();
		client.send(request, HttpResponse.BodyHandlers.ofString());
	}

	public String load(String key) throws  IOException, InterruptedException {
		request = HttpRequest
				.newBuilder()
				.GET()
				.uri(URI.create(URL + "load/" + key + "?API_TOKEN=" + apiToken))
				.build();
		response = client.send(request, HttpResponse.BodyHandlers.ofString());
		return response.body();
	}

	public String getApiToken() {
		return apiToken;
	}
}
