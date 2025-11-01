package com.jasonkaranik.json.simple.extended;

import java.util.Arrays;
import java.util.Map;

/**
 * An extended version of the JSON.simple library, with added features to simplify working with JSON in Java.
 * <p>
 * This class extends the basic JSONObject functionality by allowing access to nested objects
 * using dot-separated path strings (e.g., "parent.child.value").
 */
public class JSONObject extends org.json.simple.JSONObject {

    /**
     * Constructs an empty JSONObject.
     */
    public JSONObject() {
        super();
    }

    /**
     * Constructs a JSONObject populated with the key-value mappings from the specified map.
     *
     * @param map The map whose mappings are to be placed in this JSONObject.
     */
    public JSONObject(Map map) {
        super(map);
        wrapNestedObjects();
    }

    /**
     * Constructs a JSONObject by parsing the provided JSON string.
     *
     * @param json The string containing valid JSON to be parsed.
     */
    public JSONObject(String json) {
        super();
        Object obj = org.json.simple.JSONValue.parse(json);
        if (obj != null) {
            if (obj instanceof org.json.simple.JSONObject) {
                super.putAll((org.json.simple.JSONObject) obj);
                wrapNestedObjects();
            }
        }
    }

    /**
     * Constructs a JSONObject by copying the contents of another JSONObject.
     *
     * @param obj The JSONObject to copy from.
     */
    public JSONObject(org.json.simple.JSONObject obj) {
        super();
        if (obj != null) {
            super.putAll(obj);
            wrapNestedObjects();
        }
    }

    /**
     * Wraps all nested org.json.simple.JSONObject instances with this custom JSONObject class.
     */
    private void wrapNestedObjects() {
        for (Object key : super.keySet()) {
            Object value = super.get(key);
            if (value instanceof org.json.simple.JSONObject && !(value instanceof JSONObject)) {
                super.put(key, new JSONObject((org.json.simple.JSONObject) value));
            }
        }
    }

    /**
     * Overrides the get method to always return custom JSONObject instances for nested objects.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the key, with nested JSONObjects wrapped in the custom class.
     */
    @Override
    public Object get(Object key) {
        Object value = super.get(key);
        if (value instanceof org.json.simple.JSONObject && !(value instanceof JSONObject)) {
            JSONObject wrapped = new JSONObject((org.json.simple.JSONObject) value);
            super.put(key, wrapped);
            return wrapped;
        }
        return value;
    }

    /**
     * Retrieves a value from the JSONObject using a dot-notation path.
     * <p>
     * For example, if you have a JSON structure like {"a":{"b":{"c": 1}}},
     * you can access the value of "c" using the path "a.b.c".
     * </p>
     *
     * @param path The dot-separated path to the desired value (e.g., "parent.child.value").
     * @return The value at the specified path, or null if the path doesn't exist or is invalid.
     */
    public Object get(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }

        return findObject(this, path.split("\\."));
    }

    /**
     * Checks if a value exists at the specified dot-notation path.
     *
     * @param path The dot-separated path to check (e.g., "parent.child.value").
     * @return true If a value exists at the specified path, false otherwise.
     */
    public boolean containsKey(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }

        return findObject(this, path.split("\\.")) != null;
    }

    private Object findObject(org.json.simple.JSONObject current, String[] path) {
        if (current == null || path == null || path.length == 0) {
            return null;
        }

        String currentPath = path[0];
        if (currentPath != null) {
            Object value = current.get(currentPath);

            if (value != null) {
                if (value instanceof org.json.simple.JSONObject && path.length > 1) {
                    return findObject((org.json.simple.JSONObject) value, Arrays.copyOfRange(path, 1, path.length));
                } else {
                    return value instanceof org.json.simple.JSONObject && !(value instanceof JSONObject) ? new JSONObject((org.json.simple.JSONObject) value) : value;
                }
            }
        }
        return null;
    }

    /**
     * Sets or updates a value at the specified dot-notation path.
     * <p>
     * If intermediate objects in the path don't exist, they will not be created.
     * To set a value, the entire path except the final segment must already exist.
     * </p>
     *
     * @param path     The dot-separated path where the value should be set (e.g., "parent.child.value").
     * @param newValue The value to set at the specified path. If null, the key will be removed.
     */
    public void put(String path, Object newValue) {
        if (path == null || path.isEmpty()) {
            return;
        }

        changeObject(this, path.split("\\."), newValue);
    }

    /**
     * Overrides the put method to wrap org.json.simple.JSONObject values in the custom JSONObject class.
     *
     * @param key   The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     * @return The previous value associated with the key, or null if there was no mapping.
     */
    @Override
    public Object put(Object key, Object value) {
        if (value instanceof org.json.simple.JSONObject && !(value instanceof JSONObject)) {
            value = new JSONObject((org.json.simple.JSONObject) value);
        }
        return super.put(key, value);
    }

    private void changeObject(org.json.simple.JSONObject current, String[] path, Object newValue) {
        if (current == null || path == null || path.length == 0) {
            return;
        }

        String currentPath = path[0];
        if (currentPath != null) {
            Object currentValue = current.get(currentPath);

            if (currentValue != null) {
                if (currentValue instanceof org.json.simple.JSONObject && path.length > 1) {
                    changeObject((org.json.simple.JSONObject) currentValue, Arrays.copyOfRange(path, 1, path.length), newValue);
                    return;
                }
            }

            if (newValue == null) {
                current.remove(currentPath);
            } else {
                if (newValue instanceof org.json.simple.JSONObject && !(newValue instanceof JSONObject)) {
                    newValue = new JSONObject((org.json.simple.JSONObject) newValue);
                }
                current.put(currentPath, newValue);
            }
        }
    }
}