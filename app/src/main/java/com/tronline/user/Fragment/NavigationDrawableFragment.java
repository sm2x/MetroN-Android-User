package com.tronline.user.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tronline.user.Adapter.UserSettingsAdapter;
import com.tronline.user.AddPaymentActivity;
import com.tronline.user.BuildConfig;
import com.tronline.user.HelpwebActivity;
import com.tronline.user.HistoryActivity;
import com.tronline.user.HttpRequester.VollyRequester;
import com.tronline.user.LaterRequestsActivity;
import com.tronline.user.MainActivity;
import com.tronline.user.Models.UserSettings;
import com.tronline.user.NikolaWalletActivity;
import com.tronline.user.ProfileActivity;
import com.tronline.user.R;
import com.tronline.user.Utils.AndyUtils;
import com.tronline.user.Utils.Commonutils;
import com.tronline.user.Utils.Const;
import com.tronline.user.Utils.PreferenceHelper;
import com.tronline.user.WelcomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by user on 12/28/2016.
 */
public class NavigationDrawableFragment extends BaseMapFragment implements AdapterView.OnItemClickListener {

    private ListView userSettingsListView;
    private MainActivity activity;
    private ImageView userIcon;
    private TextView userName, tv_build_version;
    Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_drawer_layout, container, false);
        activity = (MainActivity) getActivity();
        userSettingsListView = (ListView) view.findViewById(R.id.lv_drawer_user_settings);
        userIcon = (ImageView) view.findViewById(R.id.iv_user_icon);
        userName = (TextView) view.findViewById(R.id.tv_user_name);
        String pictureUrl = new PreferenceHelper(activity).getPicture();
        String name = new PreferenceHelper(activity).getUser_name();
        tv_build_version = (TextView) view.findViewById(R.id.tv_build_version);
        tv_build_version.setText("V:" + BuildConfig.VERSION_NAME);
        if (!pictureUrl.equals("")) {
            Log.e("asher","nav pic "+pictureUrl);
            Glide.with(activity).load(pictureUrl).error(R.drawable.defult_user).into(userIcon);
        }
        if (!name.equals("")) {
            userName.setText(name);
        }
        UserSettingsAdapter settingsAdapter = new UserSettingsAdapter(activity, getUserSettingsList());
        userSettingsListView.setAdapter(settingsAdapter);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(activity, getResources().getIdentifier("layout_animation_from_left", "anim", activity.getPackageName()));
        userSettingsListView.setLayoutAnimation(animation);
        settingsAdapter.notifyDataSetChanged();
        userSettingsListView.scheduleLayoutAnimation();
        userSettingsListView.setOnItemClickListener(this);
        userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ProfileActivity.class);
                startActivity(i);
            }
        });


        return view;
    }


    private List<UserSettings> getUserSettingsList() {
        List<UserSettings> userSettingsList = new ArrayList<>();
        userSettingsList.add(new UserSettings(R.drawable.home_map_marker, getString(R.string.my_home)));
      //  userSettingsList.add(new UserSettings(R.drawable.flash, getString(R.string.ask_bot)));
        userSettingsList.add(new UserSettings(R.drawable.credit_card, getString(R.string.my_payment)));
        userSettingsList.add(new UserSettings(R.drawable.wallet, getString(R.string.nikola_wallet)));
        userSettingsList.add(new UserSettings(R.drawable.sale, getString(R.string.referral_title)));
        //   userSettingsList.add(new UserSettings(R.drawable.ic_favorite_heart_button, getString(R.string.saved_places)));
        userSettingsList.add(new UserSettings(R.drawable.clock_alert, getString(R.string.ride_history)));
        userSettingsList.add(new UserSettings(R.drawable.calendar_clock, getString(R.string.later_title)));
        userSettingsList.add(new UserSettings(R.drawable.ic_clock_map, getResources().getString(R.string.title_rentale)));
        userSettingsList.add(new UserSettings(R.drawable.ic_airplane_flight, getResources().getString(R.string.airport_title)));
        userSettingsList.add(new UserSettings(R.drawable.help_circle, getString(R.string.my_help)));
        userSettingsList.add(new UserSettings(R.drawable.ic_power_off, getString(R.string.txt_logout)));
        return userSettingsList;
    }

    @Override
    public void onResume() {
        super.onResume();
//        activity.currentFragment = Const.UserSettingsFragment;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        activity.closeDrawer();
        switch (position) {
            case 0:

                break;
          /*  case 1:
                startActivity(new Intent(activity, AskBotActivity.class));

                break;*/
            case 1:
                startActivity(new Intent(activity, AddPaymentActivity.class));
                break;
            case 2:
                startActivity(new Intent(activity, NikolaWalletActivity.class));
                break;
            case 3:
                showrefferal();
                break;
          /*  case 5 :
                startActivity(new Intent(activity, SavedPlacesActivity.class));
                break;*/
            case 4:
                startActivity(new Intent(activity, HistoryActivity.class));
                break;
            case 5:
                startActivity(new Intent(activity, LaterRequestsActivity.class));
                break;
            case 6:
                HourlyBookngFragment hourlyfragment = new HourlyBookngFragment();
                Bundle nbundle = new Bundle();
                nbundle.putString("pickup_address", Home_Map_Fragment.pickup_add);
                hourlyfragment.setArguments(nbundle);
                activity.addFragment(hourlyfragment, false, Const.HOURLY_FRAGMENT, true);
                break;
            case 7:
                activity.addFragment(new AirportBookingFragment(), false, Const.AIRPORT_FRAGMENT, true);
                break;
            case 8:
                startActivity(new Intent(activity, HelpwebActivity.class));
                break;

            case 9:
                showlogoutdailog();
                break;

        }

    }

    private void showhelp() {
        final Dialog help_dialog = new Dialog(activity, R.style.DialogSlideAnim_leftright);
        help_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        help_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        help_dialog.setCancelable(true);
        help_dialog.setContentView(R.layout.help_layout);

        help_dialog.show();
    }


    private void showrefferal() {
        final Dialog refrel_dialog = new Dialog(activity, R.style.DialogThemeforview);
        refrel_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        refrel_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_black)));
        refrel_dialog.setCancelable(true);
        refrel_dialog.setContentView(R.layout.refferalcode_layout);
        //  user = RealmController.with(this).getUser(Integer.valueOf(new PreferenceHelper(activity).getUserId()));
        final TextView refCode=refrel_dialog.findViewById(R.id.refCode);
        refCode.setText(new PreferenceHelper(activity).getReferralCode());
        ((ImageButton)refrel_dialog.findViewById(R.id.referral_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refrel_dialog.dismiss();
            }
        });

        ((ImageView)refrel_dialog.findViewById(R.id.twitter_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refrel_dialog.dismiss();
            }
        });
        if(new PreferenceHelper(activity).getReferralBONUS().isEmpty()){
            ((TextView) refrel_dialog.findViewById(R.id.txt_referl_earn)).setText("00");
        }else {
            ((TextView) refrel_dialog.findViewById(R.id.txt_referl_earn)).setText(new PreferenceHelper(activity).getReferralBONUS());
        }
        ((TextView)refrel_dialog.findViewById(R.id.gm_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey check out my app and earn by entering my referral code:" + refCode.getText().toString()+" while registering" + "https://play.google.com/store/apps/details?id=com.tronline.user");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                refrel_dialog.dismiss();

            }
        });
        ((ImageView)refrel_dialog.findViewById(R.id.fb_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent
                        .putExtra(Intent.EXTRA_TEXT, "Hey check out my app and earn by entering my referral code:" + refCode.getText().toString()+" while registering.  " + "https://play.google.com/store/apps/details?id=com.tronline.user");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.facebook.orca");
                try {
                    startActivity(sendIntent);
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(activity,"Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
                }
                refrel_dialog.dismiss();
            }
        });
        refrel_dialog.show();
    }

    private void showlogoutdailog() {

        dialog = new Dialog(activity, R.style.DialogThemeforview);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.logout_dialog);
        TextView btn_logout_yes = (TextView) dialog.findViewById(R.id.btn_logout_yes);
        btn_logout_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
         /*       dialog.dismiss();
                new PreferenceHelper(activity).Logout();

                BaseMapFragment.drop_latlan = null;
                BaseMapFragment.pic_latlan = null;
                BaseMapFragment.s_address = "";
                BaseMapFragment.d_address = "";

                new PreferenceHelper(activity).Logout();
                Intent i = new Intent(activity, WelcomeActivity.class);
                startActivity(i);
                activity.finish();*/
                logoutApi();
            }
        });
        TextView btn_logout_no = (TextView) dialog.findViewById(R.id.btn_logout_no);
        btn_logout_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

    private void logoutApi() {
        if (!AndyUtils.isNetworkAvailable(activity)) {

            return;
        }
        Commonutils.progressdialog_show(activity, getString(R.string.logout_txt));
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.LOGOUT);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());
        Log.d("asher", "logout map " + map.toString());
        new VollyRequester(activity, Const.POST, map, Const.ServiceCode.LOGOUT, this);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {

            case Const.ServiceCode.LOGOUT:
                Log.d("asher", "logout Response" + response);
                AndyUtils.removeProgressDialog();
                try {
                    JSONObject job = new JSONObject(response);
                    if (job.getString("success").equals("true")) {
                        dialog.dismiss();
                        new PreferenceHelper(activity).Logout();

                        BaseMapFragment.drop_latlan = null;
                        BaseMapFragment.pic_latlan = null;
                        BaseMapFragment.s_address = "";
                        BaseMapFragment.d_address = "";

                        new PreferenceHelper(activity).Logout();
                        Intent i = new Intent(activity, WelcomeActivity.class);
                        startActivity(i);
                        activity.finish();
                    } else {
                        String error_code = job.optString("error_code");
                        if (error_code.equals("104")) {
                            AndyUtils.showShortToast("You have logged in other device!", activity);
                            new PreferenceHelper(activity).Logout();
                            startActivity(new Intent(activity, WelcomeActivity.class));
                            activity.finish();

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                break;
        }

    }
}
