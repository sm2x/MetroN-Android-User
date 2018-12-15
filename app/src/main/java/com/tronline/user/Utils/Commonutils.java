package com.tronline.user.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.tronline.user.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Amal on 28-06-2015.
 */
public class Commonutils {
    public static Dialog mProgressDialog;


    public static void showtoast(String msg, Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void progressdialog_show(Context context, String msg) {
        if (context != null) {


            mProgressDialog = new Dialog(context, R.style.DialogSlideAnim_leftright);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setContentView(R.layout.animation_loading);
            TextView tv_title = (TextView) mProgressDialog.findViewById(R.id.tv_title);
            tv_title.setText(msg);
            mProgressDialog.show();
        }
    }




    public static void progress_show(Context context) {
        if (context != null) {


            mProgressDialog = new Dialog(context, R.style.DialogSlideAnim_leftright_Fullscreen);
            mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setContentView(R.layout.load_add_payment);
            ImageView load=(ImageView)mProgressDialog.findViewById(R.id.centerImagePay);
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(load);
            Glide.with(context).load(R.raw.loading_gif).into(imageViewTarget);
            mProgressDialog.show();
        }
    }


    public static String message_json(String message_id, String type, String driver_id,
                                      String client_id, String request_id, String message) {

        JSONObject jObject = new JSONObject();
        try {

            jObject.put("id", message_id);
            jObject.put("type", type);
            jObject.put("driver_id", driver_id);
            jObject.put("client_id", client_id);
            jObject.put("request_id", request_id);
            jObject.put("message", message);

            return jObject.toString();

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

    public static void progressdialog_hide() {
        try {
            if (mProgressDialog != null) {

                if (mProgressDialog.isShowing()) {

                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
