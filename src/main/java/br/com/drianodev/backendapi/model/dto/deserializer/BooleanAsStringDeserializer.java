package br.com.drianodev.backendapi.model.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class BooleanAsStringDeserializer extends StdDeserializer<Boolean> {

    public BooleanAsStringDeserializer() {
        this(null);
    }

    public BooleanAsStringDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        String value = node.asText().toLowerCase().replaceAll("[^a-z]", "");
        return "sim".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value);
    }
}
