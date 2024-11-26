package network;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (duration == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.value(duration.toString());
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        String string = jsonReader.nextString();
        if (string.equals("null")) {
            return null;
        }
        return Duration.parse(string);
    }
}
