package hackathon.trimble.trackme;

import android.app.Application;

/**
 * Created by Shashadhar on 17-Nov-2017.
 */


public class TrackMeApplication extends Application {

    private static TrackMeApplication instance = null;

    public boolean isSessionInvalid = false;

    private ActivityLifeCycleCallback activityLifeCycleCallback = new ActivityLifeCycleCallback();

    public static String file="TrackMe";
    public static String ID="das";

    public static TrackMeApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registerActivityLifecycleCallbacks(activityLifeCycleCallback);
    }


    public void clearBackstack(){
        activityLifeCycleCallback.clearBackstack();
    }
}
