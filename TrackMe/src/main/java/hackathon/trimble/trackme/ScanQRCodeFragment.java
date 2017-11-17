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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScanQRCodeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScanQRCodeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanQRCodeFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    TextView contentTxt;

    public ScanQRCodeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ScanQRCodeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanQRCodeFragment newInstance() {
        ScanQRCodeFragment fragment = new ScanQRCodeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= null;
        try {
            view = inflater.inflate(R.layout.fragment_scan_qrcode, container, false);
            Button btn= view.findViewById(R.id.btnScanQRcode);
            Button startUpdatingLocation=view.findViewById(R.id.btnUpdate);
            startUpdatingLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null!=mListener){
                        mListener.startPublishing();
                    }
                }
            });
            contentTxt=view.findViewById(R.id.scanContent);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   QrScanner();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void startPublishing();
    }
    public void QrScanner() {
        try {
            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan quarry QR code");
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.setBeepEnabled(true);
            integrator.initiateScan();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
