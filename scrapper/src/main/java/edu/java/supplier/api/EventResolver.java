package edu.java.supplier.api;

import java.util.HashMap;
import java.util.function.Function;
import lombok.Getter;

@Getter
public abstract class EventResolver<T> {

    private final HashMap<String, Function<T, LinkUpdateEvent>> eventMapConverters;

    protected EventResolver() {
        this.eventMapConverters = new HashMap<>();
    }

    public void registerConverter(String typeEvent, Function<T, LinkUpdateEvent> converter) {
        eventMapConverters.put(typeEvent, converter);
    }

    public Function<T, LinkUpdateEvent> getConverter(String typeEvent) {
        return eventMapConverters.get(typeEvent);
    }

}
