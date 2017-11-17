package hackathon.trimble.trackme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class LogInActivity extends Activity implements LoginButtonFragment.Callbacks,
        LoginCancelButtonFragment.Callbacks{
    private Context appContext;
    private TextView transcriptTextView;
    private TextView responseTextView;
    private static String TAG="LogInActivity";
    /**
     * Used to find the fragment by tag from the FragmentManager
     */
    private static final String LOGIN_BUTTON_TAG = "LoginButton";

    /**
     * Used to find the fragment by tag from the FragmentManager
     */
    private static final String LOGIN_CANCEL_BUTTON_TAG = "LoginCancelButton";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // Add login button fragment
        if (null == getFragmentManager().findFragmentByTag(LOGIN_BUTTON_TAG)) {
            // Add login button fragment
            LoginButtonFragment frag = LoginButtonFragment.newInstance(R.string.login_button_text, R.drawable.icon_login);
            getFragmentManager().beginTransaction().add(R.id.login_button_container, frag, LOGIN_BUTTON_TAG).commit();
        }


    }


    @Override
    public void onLoginButtonClicked() {
        Intent intent = new Intent(LogInActivity.this, NavDrawerActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginCancelButtonClicked() {

    }
}
