package ru.luvas.dk.client.api;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by RinesThaix on 23.11.16.
 */

public class Params {

    private final Set<Param> params = new HashSet<>();

    public Params(Object... args) {
        for(int i = 0; i < args.length; i += 2)
            params.add(new Param(args[i].toString(), args[i + 1]));
    }

    public Params param(String name, Object value) {
        params.add(new Param(name, value));
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(Param param : params) {
            sb.append(first ? '?' : '&').append(param.name).append('=').append(param.value.toString());
            first = false;
        }
        return sb.toString();
    }

    private static class Param {

        private final String name;
        private final Object value;

        public Param(String name, Object value) {
            this.name = name;
            this.value = value;
        }

    }
}
