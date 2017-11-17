package hackathon.trimble.trackme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import hackathon.trimble.trackme.model.Locator;
import hackathon.trimble.trackme.net.NetworkException;
import hackathon.trimble.trackme.net.NetworkResponse;
import hackathon.trimble.trackme.net.VolleyJavaCommunicator;

import static android.app.Activity.RESULT_OK;
import static hackathon.trimble.trackme.TrackMeApplication.ID;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AboutUsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutUsFragment extends Fragment {

    private ScanQRCodeFragment.OnFragmentInteractionListener mListener;
    TextView contentTxt;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ScanQRCodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutUsFragment newInstance() {
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= null;
        try {
            view = inflater.inflate(R.layout.fragment_about_us, container, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       if (context instanceof ScanQRCodeFragment.OnFragmentInteractionListener) {
            mListener = (ScanQRCodeFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public  void setContent(@NonNull  String contents){

        try {
            if(contents!=null)
            if(null!=contentTxt){
                contentTxt.setVisibility(View.VISIBLE);
                contentTxt.setText(contents);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
