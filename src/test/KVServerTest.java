package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class KVServerTest {
	HttpClient client = HttpClient.newHttpClient();
	KVServer server;

	String key = "leto";
	String data = "\nВот и лето прошло,\n" +
			"Словно и не бывало.\n" +
			"На пригреве тепло.\n" +
			"Только этого мало.";
	@BeforeEach
	void createAndStartServer() throws IOException {
		server = new KVServer();
		server.start();
	}
	@AfterEach
	void stopServer() {
		server.stop();
	}

	@Test
	void serverTest() throws IOException, InterruptedException {
		HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.uri(URI.create("http://localhost:8070/register"))
				.build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		String apiToken = response.body();

		request = HttpRequest.newBuilder()
				.POST(HttpRequest.BodyPublishers.ofString(data))
				.uri(URI.create("http://localhost:8070/save/" + key + "?API_TOKEN=" + apiToken))
				.build();
		client.send(request, HttpResponse.BodyHandlers.ofString());

		request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8070/load/" + key + "?API_TOKEN=" + apiToken))
				.build();
		response = client.send(request, HttpResponse.BodyHandlers.ofString());

		assertEquals(200, response.statusCode());
		assertEquals(data, response.body());
	}
}