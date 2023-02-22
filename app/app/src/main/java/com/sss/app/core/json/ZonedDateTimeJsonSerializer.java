package com.sss.app.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class ZonedDateTimeJsonSerializer extends JsonSerializer<ZonedDateTime> {
    public ZonedDateTimeJsonSerializer() {
    }

    public void serialize(ZonedDateTime rawValue, JsonGenerator jsonGen, SerializerProvider serProv) throws IOException, JsonProcessingException {
        String dateStr = rawValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
        jsonGen.writeString(dateStr);
    }
}

