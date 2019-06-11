package edu.wpi.cs3733d19.teamg;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;

public final class Settings {

    private Map<String, Object> settings;

    private Settings() {
        Gson gson = new Gson();
        InputStream in = Settings.class.getResourceAsStream("/settings.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        settings = gson.fromJson(reader, type);
    }

    private static class SingletonHelper {
        // Lazy instantiation of the Settings object
        // Nested class is referenced after getSettings() is called
        private static final Settings settings = new Settings();
    }

    public static Settings getSettings() {
        return SingletonHelper.settings;
    }

    public Object get(String key) {
        return settings.get(key);
    }

    public void set(String key, Object value) {
        settings.put(key, value);
    }
}
