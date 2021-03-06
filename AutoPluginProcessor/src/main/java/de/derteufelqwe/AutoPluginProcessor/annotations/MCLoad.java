package de.derteufelqwe.AutoPluginProcessor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface MCLoad {
    Values value();

    public enum Values {
        STARTUP("STARTUP"),
        POSTWORLD("POSTWORLD")
        ;

        private String name;

        private Values(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
