package com.outfit7.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtils {

    private static final ObjectMapper MAPPER;

    static {
        ObjectMapper m = new ObjectMapper();
        m.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        MAPPER = m;
    }

    public static <T> List<T> deserializeToList(String json, Class<T> elementType) {
        return deserialize(json, constructListType(elementType));
    }

    public static <T> List<T> deserializeToList(InputStream inputStream, Class<T> elementType) throws JAXBException {
        return deserialize(inputStream, constructListType(elementType));
    }

    @SneakyThrows
    public static <T> T deserialize(String json, Class<T> type) {
        return MAPPER.readValue(json, type);
    }

    private static <T> T deserialize(InputStream inputStream, JavaType javaType) throws JAXBException {
        try {
            return MAPPER.readValue(inputStream, javaType);
        } catch (IOException e) {
            throw new JAXBException(deserializationErrorMsg(javaType.getTypeName(), inputStream), e);
        }
    }

    @SneakyThrows
    private static <T> T deserialize(String json, JavaType javaType) {
        return MAPPER.readValue(json, javaType);
    }

    private static <T> CollectionType constructListType(Class<T> elementType) {
        return MAPPER.getTypeFactory().constructCollectionType(List.class, elementType);
    }

    private static String deserializationErrorMsg(String typeName, Object value) {
        return String.format("Failed deserializing %s from JSON: %s", typeName, value);
    }

}
