package mx.bytecraft.app.transitodf.utils;

import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

public class JsonReader {

    private static final String TAG = JsonReader.class.getSimpleName();
    private String mJsonString;

    public JsonReader(Resources resources, int id) {
        InputStream resourceReader = resources.openRawResource(id);
        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceReader, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception parsing Json", e);
        } finally {
            try {
                resourceReader.close();
            } catch (Exception e) {
                Log.e(TAG, "Exception parsing Json", e);
            }
        }

        mJsonString = writer.toString();
    }

    public <T> T getObjects(Class<T> type) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(mJsonString, type);
    }
}
