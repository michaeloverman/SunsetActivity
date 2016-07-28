package tech.michaeloverman.android.sunsetactivity;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by Michael on 7/28/2016.
 */
public class SunsetFragment extends android.support.v4.app.Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSkyView;
    private boolean mSunIsUp = true;

    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;

    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);

        mSceneView = view;
        mSunView = view.findViewById(R.id.sun);
        mSkyView = view.findViewById(R.id.sky);

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor(R.color.blue_sky);
        mSunsetSkyColor = resources.getColor(R.color.sunset_sky);
        mNightSkyColor = resources.getColor(R.color.night_sky);

        mSceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAnimation();
            }
        });

        return view;
    }

    private void startAnimation() {
        float sunYStart;
        float sunYEnd;
        int skyColorStart;
        int skyColorEnd;
        int nightColorStart;
        int nightColorEnd;

        if (!mSunIsUp) {
            sunYEnd = mSunView.getTop();
            sunYStart = mSkyView.getHeight();
            skyColorEnd = mBlueSkyColor;
            skyColorStart = mSunsetSkyColor;
            nightColorStart = mNightSkyColor;
            nightColorEnd = mSunsetSkyColor;
        } else {
            sunYStart = mSunView.getTop();
            sunYEnd = mSkyView.getHeight();
            skyColorStart = mBlueSkyColor;
            skyColorEnd = mSunsetSkyColor;
            nightColorStart = mSunsetSkyColor;
            nightColorEnd = mNightSkyColor;
        }
        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(mSunView, "y", sunYStart, sunYEnd)
                .setDuration(3000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", skyColorStart, skyColorEnd)
                .setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(mSkyView, "backgroundColor", nightColorStart, nightColorEnd)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        if (mSunIsUp) {
            animatorSet
                    .play(heightAnimator)
                    .with(sunsetSkyAnimator)
                    .before(nightSkyAnimator);
        } else {
            animatorSet
                    .play(heightAnimator)
                    .with(sunsetSkyAnimator)
                    .after(nightSkyAnimator);
        }
        animatorSet.start();
        
        mSunIsUp = !mSunIsUp;
    }
}
