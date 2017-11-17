package hackathon.trimble.trackme.utils;

import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import hackathon.trimble.trackme.R;
import hackathon.trimble.trackme.TrackMeApplication;

/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class AppProperties extends Properties {

    private static final String DEFAULT_VALUE = "";

    private static AppProperties instance = null;

    public static AppProperties getInstance(){

        if(instance == null){
            instance = new AppProperties();
            instance.loadProperties();
        }
        return instance;
    }

    private AppProperties(){

    }

    private void loadProperties(){
        loadProperties(R.raw.app);
        loadProperties(R.raw.endpoint);
    }

    private void loadProperties(int resourceId){
        Resources resources = TrackMeApplication.getInstance().getApplicationContext().getResources();
        InputStream rawResource = resources.openRawResource(resourceId);
        try {
            load(rawResource);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getProperty(String key){
        return getProperty(key, DEFAULT_VALUE);
    }

    public String getOpenTokApiKey(){
        return getProperty("openTokApiKey", DEFAULT_VALUE);
    }
}
