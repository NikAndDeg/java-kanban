package server.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
	@Override
	public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
		if (duration == null)
			jsonWriter.value((String) null);
		else
			jsonWriter.value(duration.toNanos());
	}

	@Override
	public Duration read(final JsonReader jsonReader) throws IOException {
		String nextString = jsonReader.nextString();
		if (nextString == null)
			return null;
		else
			return Duration.ofNanos(Integer.parseInt(nextString));
	}
}
