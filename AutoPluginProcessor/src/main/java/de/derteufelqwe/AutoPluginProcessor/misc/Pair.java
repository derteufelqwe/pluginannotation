package de.derteufelqwe.AutoPluginProcessor.misc;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Pair<A, B> {

    public A first;
    public B second;


    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }
}
