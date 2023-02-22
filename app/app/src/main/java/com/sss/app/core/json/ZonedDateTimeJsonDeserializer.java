package com.sss.app.core.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ZonedDateTimeJsonDeserializer extends JsonDeserializer<ZonedDateTime> {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = DATE_FORMAT + "'T'HH:mm:ss";
    public static final String DATE_TIME_MILLIS_FORMAT = DATE_TIME_FORMAT + ".SSS";

    public ZonedDateTimeJsonDeserializer() {
    }

    public ZonedDateTime deserialize(JsonParser parser, DeserializationContext deserCtx) throws IOException, JsonProcessingException {
        ObjectCodec oc = parser.getCodec();
        JsonNode node = oc.readTree(parser);
        return convertToDate(node.asText());
    }

    public static ZonedDateTime convertToDate(String inputStr) {
        if (inputStr == null)
            return null;
        ZonedDateTime zonedDateTime;
        try {
            zonedDateTime = ZonedDateTime.parse(inputStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
        } catch (DateTimeParseException dtpe) {
            zonedDateTime = ZonedDateTime.parse(inputStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX"));
        }

        return zonedDateTime;
    }
}
