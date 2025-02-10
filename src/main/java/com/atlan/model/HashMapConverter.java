package com.atlan.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter
public class HashMapConverter implements AttributeConverter<Map<String, String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, String> attributeMap) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(attributeMap);
        } catch (final JsonProcessingException e) {
            System.err.println("JSON writing error" + e);
        }
        return jsonString;
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String dbData) {
        Map<String, String> attributeMap = null;
        try {
            attributeMap = objectMapper.readValue(dbData, HashMap.class);
        } catch (final IOException e) {
            System.err.println("JSON reading error" + e);
        }
        return attributeMap;
    }
}