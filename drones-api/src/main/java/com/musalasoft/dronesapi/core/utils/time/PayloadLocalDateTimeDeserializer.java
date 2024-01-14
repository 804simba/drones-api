package com.musalasoft.dronesapi.core.utils.time;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

public class PayloadLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {
    public PayloadLocalDateTimeDeserializer() {
        this(null);
    }

    public PayloadLocalDateTimeDeserializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String dateTimeString = jsonParser.getText();
        if (dateTimeString.length() >= 10) {
            return PayloadLocalDateTimeFormatterFactory.getInstance().parseStringToLocalDateTime(dateTimeString, "yyyy-MM-dd HH:mm:ss");
        }
        return null;
    }
}
