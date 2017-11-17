/*
 *
 * Copyright (c), Trimble Construction Logistics
 *
 *
 */
package hackathon.trimble.trackme;


//region import directives

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
//endregion import directives


/**
 * Encapsulates functionality common amongst multiple
 * fragment classes
 * <p/>
 * Created by BrushFire (samhoefer) on 8/7/13.
 * Cleaned up a bit on 12/4/2015 by tsunder
 */
public abstract class BaseFragment
        extends Fragment {

    //region data members

    /**
     * Flag indicating whether or not we
     * should register for event bus events
     */
    protected boolean mRegisterForEventBusEvents = true;
    //endregion data members


    //region lifecycle overrides

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p/>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
        } catch (Exception ex) {

        }
    }

    /**
     * Called when the fragment is no longer in use. This is called after onStop() and before onDetach().
     */
    @Override
    public void onDestroy() {
        try {
        super.onDestroy();
        } catch (Exception ex) {

        }
    }
    //endregion lifecycle overrides
}
