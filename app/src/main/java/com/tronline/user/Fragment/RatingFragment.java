package com.tronline.user.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aurelhubert.simpleratingbar.SimpleRatingBar;
import com.bumptech.glide.Glide;
import com.tronline.user.DatabaseHandler;
import com.tronline.user.HttpRequester.VollyRequester;
import com.tronline.user.MainActivity;
import com.tronline.user.Models.RequestDetail;
import com.tronline.user.R;
import com.tronline.user.Utils.AndyUtils;
import com.tronline.user.Utils.Commonutils;
import com.tronline.user.Utils.Const;
import com.tronline.user.Utils.PreferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 1/12/2017.
 */

public class RatingFragment extends BaseMapFragment {
    private RequestDetail requestDetail;
    private TextView tv_total, text_time, text_distance, btn_submit_rating, tv_cancellation_fee;
    private SimpleRatingBar simple_rating_bar;
    private CircleImageView iv_feedback_vehicle, iv_feedback_user, iv_feedback_location;
    String google_img_url = "";
    int rating = 0;
    private AlertDialog.Builder paybuilder;
    private boolean ispayshowing = false;
    DatabaseHandler db;
    private LinearLayout layout_distance, toll_layout;
    private TextView tv_no_tolls, tv_payment_type,tv_total_usd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_feedback_layout, container,
                false);
        tv_total = (TextView) view.findViewById(R.id.tv_total);
        text_time = (TextView) view.findViewById(R.id.text_time);
        text_distance = (TextView) view.findViewById(R.id.text_distance);
        simple_rating_bar = (SimpleRatingBar) view.findViewById(R.id.simple_rating_bar);
        btn_submit_rating = (TextView) view.findViewById(R.id.btn_submit_rating);
        tv_no_tolls = (TextView) view.findViewById(R.id.tv_no_tolls);
        tv_payment_type = (TextView) view.findViewById(R.id.tv_payment_type);
        tv_total_usd= (TextView) view.findViewById(R.id.tv_total_usd);
        tv_cancellation_fee = (TextView) view.findViewById(R.id.tv_cancellation_fee);

        iv_feedback_vehicle = (CircleImageView) view.findViewById(R.id.iv_feedback_vehicle);
        iv_feedback_user = (CircleImageView) view.findViewById(R.id.iv_feedback_user);
        iv_feedback_location = (CircleImageView) view.findViewById(R.id.iv_feedback_location);
        layout_distance = (LinearLayout) view.findViewById(R.id.layout_distance);
        toll_layout = (LinearLayout) view.findViewById(R.id.toll_layout);
        rating = simple_rating_bar.getRating();
        simple_rating_bar.setListener(new SimpleRatingBar.SimpleRatingBarListener() {
            @Override
            public void onValueChanged(int value) {
                rating = value;

            }
        });

        btn_submit_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giverating();
            }
        });


        return view;

    }

    private void showpaydialog() {
        ispayshowing = true;
        String Mesaage = getResources().getString(R.string.txt_ride_cmplt) + "\n" + "\n" + getResources().getString(R.string.pay) + requestDetail.getCurrnecy_unit() + " " + requestDetail.getTrip_total_price();
        paybuilder = new AlertDialog.Builder(activity);
        paybuilder.setMessage(Mesaage)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.pay_now), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        sendpay();
                    }
                });
        AlertDialog alert = paybuilder.create();
        alert.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

          /*TO clear all views */
        ViewGroup mContainer = (ViewGroup) getActivity().findViewById(R.id.content_frame);
        mContainer.removeAllViews();
    }

    private void sendpay() {
        if (!AndyUtils.isNetworkAvailable(activity)) {

            return;
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.PAYNOW);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());
        map.put(Const.Params.IS_PAID, "1");
        map.put(Const.Params.PAYMENT_MODE, requestDetail.getPayment_mode());
        map.put(Const.Params.REQUEST_ID, String.valueOf(new PreferenceHelper(activity).getRequestId()));

        Log.d("mahi", map.toString());
        new VollyRequester(activity, Const.POST, map, Const.ServiceCode.PAYNOW,
                this);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mBundle = getArguments();
        db = new DatabaseHandler(activity);
        AndyUtils.removeProgressDialog();

        if (db != null) {
            db.DeleteChat(String.valueOf(new PreferenceHelper(activity).getRequestId()));
        }
        activity.currentFragment = Const.RATING_FRAGMENT;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            requestDetail = (RequestDetail) mBundle.getSerializable(
                    Const.REQUEST_DETAIL);
            google_img_url = getGoogleMapThumbnail(Double.valueOf(requestDetail.getD_lat()), Double.valueOf(requestDetail.getD_lon()));
            //AndyUtils.removeLoader();
            tv_total.setText(requestDetail.getCurrnecy_unit() + " " + requestDetail.getTrip_total_price());
            text_time.setText(requestDetail.getTrip_time() + " " + getResources().getString(R.string.min));
            if(requestDetail.getPayment_mode().equalsIgnoreCase("tron_wallet")) {
                tv_payment_type.setText(getResources().getString(R.string.txt_payment_type) + "Tron Wallet");
            }
            tv_total_usd.setText("≈$ "+requestDetail.getUsdTotal());
            Glide.with(activity).load(requestDetail.getDriver_picture()).error(R.drawable.driver).into(iv_feedback_user);
            Glide.with(activity).load(google_img_url).into(iv_feedback_location);
            Glide.with(activity).load(requestDetail.getVehical_img()).into(iv_feedback_vehicle);
            text_distance.setText(requestDetail.getTrip_distance() + " " + requestDetail.getDistance_unit());
            if (requestDetail.getDriverStatus() == 3) {
                if (isAdded() && ispayshowing == false && activity.currentFragment.equals(Const.RATING_FRAGMENT)) {

                    showpaydialog();
                }

            }

            if (null != requestDetail.getCancellationFee() &&!requestDetail.getCancellationFee().equals("0")) {
                tv_cancellation_fee.setVisibility(View.VISIBLE);
                tv_cancellation_fee.setText(getResources().getString(R.string.txt_trip_cancel_fee) + " " + requestDetail.getCurrnecy_unit() + " " + requestDetail.getCancellationFee());
            } else {
                tv_cancellation_fee.setVisibility(View.GONE);
            }

            if (requestDetail.getRequest_type().equals("1") || requestDetail.getRequest_type().equals("2")) {
                toll_layout.setVisibility(View.GONE);
            } else {
                toll_layout.setVisibility(View.VISIBLE);
                tv_no_tolls.setText(getResources().getString(R.string.txt_toll)+":" + " " + requestDetail.getNo_tolls());
            }
            if (requestDetail.getRequest_type().equals("2") || requestDetail.getRequest_type().equals("3")) {
                layout_distance.setVisibility(View.GONE);
                iv_feedback_location.setVisibility(View.GONE);
            } else {
                layout_distance.setVisibility(View.VISIBLE);
                iv_feedback_location.setVisibility(View.VISIBLE);
            }

        }
    }

    public static String getGoogleMapThumbnail(double lati, double longi) {
        String staticMapUrl = "http://maps.google.com/maps/api/staticmap?center=" + lati + "," + longi + "&markers=" + lati + "," + longi + "&zoom=14&size=150x120&sensor=false&key="+Const.GOOGLE_API_KEY;
        return staticMapUrl;
    }

    @Override
    public void onResume() {
        super.onResume();
        activity.currentFragment = Const.RATING_FRAGMENT;

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        switch (serviceCode) {
            case Const.ServiceCode.PAYNOW:
                Log.d("mahi", "pay provider" + response);
                if (response != null) {

                    try {
                        JSONObject job = new JSONObject(response);
                        if (job.getString("success").equals("true")) {
                            AndyUtils.showShortToast(getResources().getString(R.string.txt_payment_success), activity);
                        } else {
                            String error = job.getString("error");
                            showDebtDialog(error);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case Const.ServiceCode.RATE_PROVIDER:
                Log.d("mahi", "rate provider" + response);
                try {
                    JSONObject job = new JSONObject(response);
                    if (job.getString("success").equals("true")) {
                        Commonutils.progressdialog_hide();
                        new PreferenceHelper(activity).clearRequestData();
                        Intent i = new Intent(activity, MainActivity.class);
                        startActivity(i);
                    } else {
                        Commonutils.progressdialog_hide();
                        String error_msg = job.getString("error");
                        Commonutils.showtoast(error_msg, activity);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    private void showDebtDialog(String error) {

        AlertDialog.Builder debtbuilder = new AlertDialog.Builder(activity);
        debtbuilder.setMessage(error)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.pay_now), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = debtbuilder.create();
        alert.show();
    }

    private void giverating() {
        if (!AndyUtils.isNetworkAvailable(activity)) {

            return;
        }
        Commonutils.progressdialog_show(activity, "Rating...");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Const.Params.URL, Const.ServiceType.RATE_PROVIDER);
        map.put(Const.Params.ID, new PreferenceHelper(activity).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(activity).getSessionToken());
        map.put(Const.Params.REQUEST_ID, String.valueOf(new PreferenceHelper(activity).getRequestId()));
        map.put(Const.Params.COMMENT, " ");
        map.put(Const.Params.RATING, String.valueOf(rating));
        Log.d("mahi", map.toString());
        new VollyRequester(activity, Const.POST, map, Const.ServiceCode.RATE_PROVIDER,
                this);

    }


}
