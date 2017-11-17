package hackathon.trimble.trackme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * Created by Shashadhar on 17-Nov-2017.
 */
public class ScanQRCodeActivity extends AppCompatActivity {
    //    TextView qrCodeResult;
    public final static String QR_CODE = "qrcode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_scan_qrcode);
            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan quarry QR code");
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.setBeepEnabled(true);
            integrator.initiateScan();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getData(String data) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return "";
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            String contentStr = null;
            if (resultCode == RESULT_OK && result != null) {
                if (result.getContents() != null) {
                    Log.e("Scan QR code", result.getContents());
                }
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
