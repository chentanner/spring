package com.sss.app.core.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class EndsWithFilter extends SimpleBeanPropertyFilter {
    protected final Set<String> _propertiesToExclude;

    public EndsWithFilter(String... propertyArray) {
        _propertiesToExclude = new HashSet<>(propertyArray.length);
        Collections.addAll(_propertiesToExclude, propertyArray);
    }

    @Override
    public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider provider, PropertyWriter writer) throws Exception {
        if (!stringContainsItemFromList(writer.getName(), this._propertiesToExclude)) {
            writer.serializeAsField(bean, jgen, provider);
        }
    }

    private boolean stringContainsItemFromList(String inputStr, Set<String> items) {
        return items.stream().anyMatch(inputStr::endsWith);
    }
}
