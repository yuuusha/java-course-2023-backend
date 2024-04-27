package edu.java.supplier.github.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import edu.java.supplier.github.data.GithubEventInfo;
import edu.java.supplier.github.data.GithubEventsCollector;
import java.io.IOException;
import java.util.Arrays;

public class GithubEventsCollectorDeserializer extends StdDeserializer<GithubEventsCollector> {
    protected GithubEventsCollectorDeserializer() {
        super(GithubEventsCollector.class);
    }

    @Override
    public GithubEventsCollector deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        return new GithubEventsCollector(
            Arrays.stream(mapper.readValue(jsonParser, GithubEventInfo[].class)).toList()
        );
    }
}
