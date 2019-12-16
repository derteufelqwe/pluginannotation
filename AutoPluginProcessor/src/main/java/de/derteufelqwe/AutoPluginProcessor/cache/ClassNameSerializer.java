package de.derteufelqwe.AutoPluginProcessor.cache;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.squareup.javapoet.ClassName;

import java.lang.reflect.Type;

public class ClassNameSerializer implements JsonSerializer<ClassName> {

    @Override
    public JsonElement serialize(ClassName src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("package", src.packageName());
        jsonObject.addProperty("class", src.simpleName());

        return jsonObject;
    }
}
