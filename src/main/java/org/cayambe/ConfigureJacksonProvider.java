package org.cayambe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Das hier ist eine Provider, der für JAX-RS den Jackson-JSON-Mapper erweitert. In diesem Beispiel registrieren wir eine Erweiterung, damit java 8 Zeit-Klassen
 * richtig convertiert werden
 */
@Provider
public class ConfigureJacksonProvider implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public ObjectMapper getContext(Class<?> aClass) {
        return mapper;
    }
}
