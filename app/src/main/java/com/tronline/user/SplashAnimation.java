package com.tronline.user;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import com.tronline.user.HttpRequester.AsyncTaskCompleteListener;
import com.tronline.user.HttpRequester.VollyRequester;
import com.tronline.user.Utils.AndyUtils;
import com.tronline.user.Utils.Const;
import com.tronline.user.Utils.PreferenceHelper;
import com.tronline.user.Utils.splash.SplashAnimationHelper;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mahesh on 7/19/2017.
 */

public class SplashAnimation extends AppCompatActivity implements AsyncTaskCompleteListener {
    RelativeLayout splashAnimationLayout;
    private SplashAnimationHelper.SplashRouteAnimation splashRouteAnimation;
    int versionCode;
    int mDriver_Version_Code;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_animation);
        splashAnimationLayout = (RelativeLayout)findViewById(R.id.splashAnimationLayout);
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pInfo.versionCode;
            new PreferenceHelper(this).putAppVersion(versionCode);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Log.e("asher","version "+versionCode+" pref: "+new PreferenceHelper(this).getAppVersion());
        getVersionCheck();
        animateToHomeScreen();
        startProgressAnimation();


   /*     new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashAnimation.this, WelcomeActivity.class);
                startActivity(i);

                // close this activity
                finish();

            }
        },9000);*/
    }

    private void getVersionCheck() {
        if (!AndyUtils.isNetworkAvailable(this)) {
            return;
        }
        //    AndyUtils.showSimpleProgressDialog(this, "", false);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.GET_VERSION);
//        map.put(Const.Params.ID, new PreferenceHelper(this).getUserId());
//        map.put(Const.Params.TOKEN, new PreferenceHelper(this).getSessionToken());

        Log.d("asher ","version map "+ map.toString());
        new VollyRequester(this, Const.GET, map, Const.ServiceCode.GET_VERSION,
                this);
    }

    private void startProgressAnimation()
    {
        {
            this.splashRouteAnimation = new SplashAnimationHelper().createSplashAnimation(this);
            this.splashRouteAnimation.startAnimation(this.splashAnimationLayout);
        }
    }


    private void animateToHomeScreen()
    {

        AnimatorSet localAnimatorSet1 = new AnimatorSet();
        AnimatorSet localAnimatorSet2 = new AnimatorSet();
        AnimatorSet localAnimatorSet3 = new AnimatorSet();
        ArrayList localArrayList1 = new ArrayList();
        ArrayList localArrayList2 = new ArrayList();
        localArrayList1.add(ObjectAnimator.ofFloat(this.splashAnimationLayout, "alpha", new float[] { 1.0F, 0.0F }));

        localAnimatorSet2.setDuration(20000);
        localAnimatorSet2.playTogether(localArrayList1);

        localAnimatorSet3.playSequentially(localArrayList2);
        localAnimatorSet3.setDuration(500L);
        localAnimatorSet3.setStartDelay(50L);
        localAnimatorSet1.playSequentially(new Animator[] { localAnimatorSet2, localAnimatorSet3 });
        localAnimatorSet1.addListener(new Animator.AnimatorListener()
        {
            public void onAnimationCancel(Animator paramAnonymousAnimator) {}

            public void onAnimationEnd(Animator paramAnonymousAnimator)
            {
                if (SplashAnimation.this.splashRouteAnimation != null) {
                   // SplashAnimation.this.splashRouteAnimation.stopAnimation();
                }
            }

            public void onAnimationRepeat(Animator paramAnonymousAnimator) {}

            public void onAnimationStart(Animator paramAnonymousAnimator) {}
        });
        localAnimatorSet1.start();
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }





    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.GET_VERSION:
                //     AndyUtils.removeProgressDialog();
                Log.d("asher", "version res" + response);
                if (response != null) {
                    try {
                        JSONObject mResponse_Obj = new JSONObject(response);
                        if (mResponse_Obj.optString("success").equalsIgnoreCase("true")) {
                            mDriver_Version_Code = Integer.parseInt(mResponse_Obj.optString("android_driver_version"));
                            if (mDriver_Version_Code > versionCode) {
                                AlertDialog.Builder versionbuilder = new AlertDialog.Builder(SplashAnimation.this);
                                versionbuilder.setTitle(getResources().getString(R.string.update_txt_available));
                                versionbuilder.setMessage(getResources().getString(R.string.update_txt))
                                        .setCancelable(false)
                                        .setPositiveButton(getResources().getString(R.string.update_txt_btn), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //do things
                                                String mPlayStoreLink = "https://play.google.com/store/apps/details?id=com.tronline.user";
                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPlayStoreLink));
                                                startActivity(intent);
                                            }
                                        });
                                AlertDialog alert = versionbuilder.create();
                                alert.show();
                            } else {
                                new Handler().postDelayed(
                                        new Runnable() {
                                            @Override
                                            public void run() {

                                                Intent i = new Intent(SplashAnimation.this, WelcomeActivity.class);
                                                startActivity(i);

                                                // close this activity
                                                finish();

                                            }
                                        }, 3000);
                            }
                        } else if (mResponse_Obj.optString("success").equalsIgnoreCase("false")) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent(SplashAnimation.this, WelcomeActivity.class);
                                    startActivity(i);

                                    // close this activity
                                    finish();

                                }
                            }, 3000);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }








}
