package hackathon.trimble.trackme;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.UiThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class LoginCancelButtonFragment extends BaseFragment {
    public static final String EXTRA_LOGIN_CANCEL_BUTTON_TITLE = "com.trimble.trimfleet.login_cancel_button_title";
    public static final String EXTRA_LOGIN_CANCEL_BUTTON_DRAWABLE = "com.trimble.trimfleet.login_cancel_button_drawable";
    private View view;
    private Integer mTitleResource;
    private Integer mDrawableResource;
    private Callbacks mCallbacks;

    // Empty fragment constructor
    public LoginCancelButtonFragment() {
        mRegisterForEventBusEvents = false;
    }

    // New instance method used to create all new fragments
    public static LoginCancelButtonFragment newInstance(Integer title, Integer drawable) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_LOGIN_CANCEL_BUTTON_TITLE, title);
        args.putInt(EXTRA_LOGIN_CANCEL_BUTTON_DRAWABLE, drawable);

        LoginCancelButtonFragment fragment = new LoginCancelButtonFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            setTitleResource(args.getInt(EXTRA_LOGIN_CANCEL_BUTTON_TITLE));
            setDrawableResource(args.getInt(EXTRA_LOGIN_CANCEL_BUTTON_DRAWABLE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_image_button, null);

        if (view == null) return null;

        if (savedInstanceState != null) {

        }

        // Set layout params to match parent
        LinearLayout.LayoutParams newParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(newParams);

        // Set onClickListener to the parent view
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.onLoginCancelButtonClicked();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUi();
    }

    /**
     * Updates the UI with the current data
     */
    @UiThread
    private void updateUi() {
        if (view == null) return;

        AutoAdjustTextView textView = (AutoAdjustTextView) view.findViewById(R.id.title);
        if (textView != null && mTitleResource != null) {
            textView.setText(mTitleResource);
            textView.setMaxTextSize(textView.getTextSize());
        }

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        boolean removeImageView = false;
        if (imageView != null && mDrawableResource != null) {
            if (mDrawableResource != 0) {
                // Add image view
                imageView.setImageResource(mDrawableResource);
            } else {
                // Clear image view
                imageView.setImageBitmap(null);
                removeImageView = true;
            }
        } else if (imageView != null) {
            // Clear image view
            imageView.setImageBitmap(null);
            removeImageView = true;
        }

        if (removeImageView) {
            RelativeLayout imageLayout = (RelativeLayout) view.findViewById(R.id.image_wrapper);
            if (imageLayout != null) {
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.title_wrapper);
                layout.removeView(imageLayout);
            }

            // remove spacer as well
            RelativeLayout spacerLayout = (RelativeLayout) view.findViewById(R.id.spacer);
            if (spacerLayout != null) {
                LinearLayout layout = (LinearLayout) view.findViewById(R.id.title_wrapper);
                layout.removeView(spacerLayout);
            }
        }
    }

    public Integer getTitleResource() {
        return mTitleResource;
    }

    public void setTitleResource(Integer titleResource) {
        this.mTitleResource = titleResource;
    }

    public Integer getDrawableResource() {
        return mDrawableResource;
    }

    public void setDrawableResource(Integer drawableResource) {
        this.mDrawableResource = drawableResource;
    }

    public interface Callbacks {
        void onLoginCancelButtonClicked();
    }
}
