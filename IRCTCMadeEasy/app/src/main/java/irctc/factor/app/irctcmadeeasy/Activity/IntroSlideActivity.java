package irctc.factor.app.irctcmadeeasy.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import irctc.factor.app.irctcmadeeasy.R;

/**
 * Created by AHAMED on 8/4/2016.
 */
public class IntroSlideActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        getSupportActionBar().hide();
        addSlide(AppIntroFragment.newInstance("1.Add Train Deatils", "description", R.drawable.screen_background, R.color.material_blue_grey_800));
        addSlide(AppIntroFragment.newInstance("2. Choose Passenger ", "description", R.drawable.screen_background, R.color.material_blue_grey_800));
        addSlide(AppIntroFragment.newInstance("3. Select Saved Cards", "description", R.drawable.screen_background, R.color.material_blue_grey_800));
        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        //showSkipButton(false);
        setProgressButtonEnabled(false);


        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
