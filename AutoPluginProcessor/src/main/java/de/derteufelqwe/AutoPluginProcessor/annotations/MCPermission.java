package de.derteufelqwe.AutoPluginProcessor.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface MCPermission {
    String name();
    String description() default "";
    Defaults default_() default Defaults.NONE;


    public enum Defaults {
        NONE(""),
        TRUE("true"),
        FALSE("false"),
        OP("op"),
        NOT_OP("not op")
        ;

        private String name;

        private Defaults(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
