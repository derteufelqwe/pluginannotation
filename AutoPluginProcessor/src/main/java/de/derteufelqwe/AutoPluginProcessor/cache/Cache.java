package de.derteufelqwe.AutoPluginProcessor.cache;

import com.squareup.javapoet.ClassName;
import de.derteufelqwe.AutoPluginProcessor.misc.Pair;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class Cache {

    private Map<String, Pair<ClassName, ClassName>> commands = new HashMap<>();
    private List<ClassName> listeners = new ArrayList<>();
    

    public Cache(Map<String, Pair<ClassName, ClassName>> commands, List<ClassName> listeners) {
        this.commands = commands;
        this.listeners = listeners;
    }


    public Map<String, Object> getAsMap() {
        Map<String, Object> resmap = new HashMap<>();

        resmap.put("commands", commands);
        resmap.put("listeners", listeners);

        return resmap;
    }

}
