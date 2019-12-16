package de.derteufelqwe.AutoPluginProcessor.cache;

import com.google.gson.*;
import com.squareup.javapoet.ClassName;

import java.lang.reflect.Type;

public class ClassNameDeserializer implements JsonDeserializer<ClassName> {

    @Override
    public ClassName deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        return ClassName.get(object.get("package").getAsString(), object.get("class").getAsString());
    }
}
