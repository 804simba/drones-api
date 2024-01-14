package com.musalasoft.dronesapi.core.utils.time;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;

public class PayloadLocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    public PayloadLocalDateTimeSerializer() {
        this(null);
    }
    public PayloadLocalDateTimeSerializer(Class<LocalDateTime> t) {
        super(t);
    }

    @Override
    public void serialize(LocalDateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(PayloadLocalDateTimeFormatterFactory.getInstance().parseLocalDateTimeToString(dateTime, "yyyy-MM-dd HH:mm:ss"));
    }
}
