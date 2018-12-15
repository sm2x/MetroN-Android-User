package com.tronline.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tronline.user.Adapter.GetCardsAdapter;
import com.tronline.user.HttpRequester.AsyncTaskCompleteListener;
import com.tronline.user.HttpRequester.VollyRequester;
import com.tronline.user.Models.Cards;
import com.tronline.user.Utils.AndyUtils;
import com.tronline.user.Utils.Commonutils;
import com.tronline.user.Utils.Const;
import com.tronline.user.Utils.PreferenceHelper;
import com.tronline.user.Utils.RecyclerLongPressClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 1/21/2017.
 */

public class AddPaymentActivity extends AppCompatActivity implements AsyncTaskCompleteListener {
    private Toolbar cardToolbar;
    private ArrayList<Cards> cardslst;
    private ImageButton payment_back;
    private static final int REQUEST_CODE = 133;
    private FloatingActionButton addCardButton;
    private RecyclerView rv_add_card;
    private GetCardsAdapter adapter;
    ImageView imageView;
    boolean addCard = false;
    Dialog payStackDialog;
    LinearLayout addCardLayout, showQRLayout;
    TextView tv_trx_address, tv_trx_value, tv_trx_value_amount,doneQR;
    private EditText et_enter_amount;
    String trxValue,text,marketPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cardslst = new ArrayList<Cards>();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        setContentView(R.layout.activity_addpayment);
        cardToolbar = (Toolbar) findViewById(R.id.toolbar_addcard);
        addCardButton = (FloatingActionButton) findViewById(R.id.bn_add_card);
        rv_add_card = (RecyclerView) findViewById(R.id.rv_add_card);
        rv_add_card.addOnItemTouchListener(new RecyclerLongPressClickListener(this, rv_add_card, new RecyclerLongPressClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {
                if (!isFinishing()) {

                    showdeletecard(cardslst.get(position).getCardId());

                }

            }
        }));

        setSupportActionBar(cardToolbar);
        getSupportActionBar().setTitle(null);
        payment_back = (ImageButton) findViewById(R.id.payment_back);
        payment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        addCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //      getBrainTreeClientToken();
                openPaymentOptionsDialog();
            }
        });

        getAddedCard();

    }

    private void openPaymentOptionsDialog() {
        payStackDialog = new Dialog(this, R.style.DialogThemeforview);
        Log.e("asher", "inside pay dialog");
        payStackDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        payStackDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.fade_drawable));
        payStackDialog.setCancelable(true);
        payStackDialog.setContentView(R.layout.dialog_cvv_layout);
        final TextView cardNumberCVV = (TextView) payStackDialog.findViewById(R.id.cardNumberCVV);
        final TextView paymentHeader = (TextView) payStackDialog.findViewById(R.id.paymentHeader);
        final EditText CVVcard = (EditText) payStackDialog.findViewById(R.id.CVVcard);
        final TextView confirmCard = (TextView) payStackDialog.findViewById(R.id.confirmCard);
        final TextView confirmQR = (TextView) payStackDialog.findViewById(R.id.confirmQR);
        addCardLayout = (LinearLayout) payStackDialog.findViewById(R.id.addCardLayout);
        showQRLayout = (LinearLayout) payStackDialog.findViewById(R.id.qrLayout);
        imageView = (ImageView) payStackDialog.findViewById(R.id.imageQr);
        tv_trx_address = (TextView) payStackDialog.findViewById(R.id.tv_trx_addressQR);
        tv_trx_value = (TextView) payStackDialog.findViewById(R.id.tv_trx_valueQR);
        tv_trx_value_amount = (TextView) payStackDialog.findViewById(R.id.tv_trx_value_amountQR);
        doneQR = (TextView) payStackDialog.findViewById(R.id.doneQR);
        doneQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(AddPaymentActivity.this);
                builder.setMessage("TRX deposits may take a while to reflect in your wallet ( this is normal ). Atleast 19 network confirmations are required. Please try hitting the Refresh button in the Tron wallet screen ( Present on top right corner)");
                builder.setCancelable(false);
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(AddPaymentActivity.this,NikolaWalletActivity.class);
                        startActivity(intent);
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        ImageView back = (ImageView) payStackDialog.findViewById(R.id.cvvBack);
        et_enter_amount = (EditText) payStackDialog.findViewById(R.id.et_enter_amountQR);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        tv_trx_address.setText(text);
        tv_trx_value.setText(marketPrice);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     showpaydialog();
                if (addCardLayout.getVisibility() == View.VISIBLE) {
                    payStackDialog.dismiss();
                } else {
                    paymentHeader.setText("Payment Options");
                    showQRLayout.setVisibility(View.GONE);
                    addCardLayout.setVisibility(View.VISIBLE);

                }
            }
        });
        payStackDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (addCardLayout.getVisibility() == View.VISIBLE) {
                    payStackDialog.dismiss();
                } else {
                    paymentHeader.setText("Payment Options");
                    showQRLayout.setVisibility(View.GONE);
                    addCardLayout.setVisibility(View.VISIBLE);

                }
            }
        });
        confirmCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     showpaydialog();
          //      getBrainTreeClientToken();
                //       payStackDialog.dismiss();
            }
        });

        confirmQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     showpaydialog();

                addCardLayout.setVisibility(View.GONE);
                showQRLayout.setVisibility(View.VISIBLE);
                paymentHeader.setText("Send TRX to your Wallet Address");

                //    payStackDialog.dismiss();
            }
        });

  //      cardNumberCVV.setText(getResources().getString(R.string.enter_your_cvv_cvc_of_your_card_ending_with));

        et_enter_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                Log.e("asher", "amount1");
                et_enter_amount.removeTextChangedListener(this);
                s.replace(0, s.length(), et_enter_amount.getText().toString());
                if (s.length() < 1 || s.equals("")) {
                    tv_trx_value_amount.setText("0");
                }

                et_enter_amount.addTextChangedListener(this);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                Log.e("asher", "amount2 ");

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //    tv_trx_value_amount.setVisibility(View.VISIBLE);
                if (s.length() > 0) {
                    //       tv_trx_value_amount.setVisibility(View.VISIBLE);
                    Log.e("asher", "amount " + Double.valueOf(s.toString()) + " value: " + Double.valueOf(s.toString()) / Double.valueOf(String.valueOf(trxValue)));
                    tv_trx_value_amount.setText(String.valueOf(Double.valueOf(s.toString()) / Double.valueOf(String.valueOf(trxValue))));
                } else {
                    //     tv_trx_value_amount.setVisibility(View.GONE);
                }
            }
        });


        payStackDialog.show();

    }

    private void showdeletecard(final String cardId) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        dialog.dismiss();
                        removecard(cardId);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure? You want to remove this card?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void removecard(String cardId) {
        if (!AndyUtils.isNetworkAvailable(this)) {

            return;
        }

        Commonutils.progressdialog_show(this, "Removing...");
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.REMOVE_CARD);
        map.put(Const.Params.ID, new PreferenceHelper(this).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(this).getSessionToken());
        map.put(Const.Params.CARD_ID, cardId);

        AndyUtils.appLog("mahi", "remove card" + map);

        new VollyRequester(this, Const.POST, map, Const.ServiceCode.REMOVE_CARD, this);
    }

    private void getBrainTreeClientToken() {
        if (!AndyUtils.isNetworkAvailable(this)) {

            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.GET_BRAIN_TREE_TOKEN_URL);
        map.put(Const.Params.ID, new PreferenceHelper(this).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(this).getSessionToken());

        AndyUtils.appLog("mahi", "BrainTreeClientTokenMap" + map);

        new VollyRequester(this, Const.POST, map, Const.ServiceCode.GET_BRAIN_TREE_TOKEN_URL, this);

    }

    private void getAddedCard() {
        if (!AndyUtils.isNetworkAvailable(this)) {

            return;
        }
Commonutils.progressdialog_show(this,"");
        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.GET_ADDED_CARDS_URL + Const.Params.ID + "="
                + new PreferenceHelper(this).getUserId() + "&" + Const.Params.TOKEN + "="
                + new PreferenceHelper(this).getSessionToken());

        AndyUtils.appLog("Ashutosh", "GetAddedCardMap" + map);

        new VollyRequester(this, Const.GET, map, Const.ServiceCode.GET_ADDED_CARDS_URL, this);


    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                        BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                );
                String nonce = paymentMethodNonce.getNonce();
                // Log.d("mahi","none value"+nonce);
                // Send the nonce to your server.
                postNonceToServer(nonce);
            } else {
                // handle errors here, an exception may be available in
                Exception error = null;
                try {
                    error = (Exception) data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE);
                    Log.d("mahi", "error message" + error);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

    void postNonceToServer(String nonce) {
        addCard = true;
        Commonutils.progress_show(this);
        if (!AndyUtils.isNetworkAvailable(this)) {

            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Const.Params.URL, Const.ServiceType.CREATE_ADD_CARD_URL);
        map.put(Const.Params.ID, new PreferenceHelper(this).getUserId());
        map.put(Const.Params.TOKEN, new PreferenceHelper(this).getSessionToken());
        map.put(Const.Params.PAYMENT_METHOD_NONCE, nonce);

        AndyUtils.appLog("mahi", "BrainTreeADDCARDMap" + map);

        new VollyRequester(this, Const.POST, map, Const.ServiceCode.CREATE_ADD_CARD_URL, this);

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode) {
            case Const.ServiceCode.GET_BRAIN_TREE_TOKEN_URL:
                AndyUtils.appLog("mahi", "BrainTreeClientTokenResponse" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        String clientToken = jsonObject.optString("client_token");

                        PaymentRequest paymentRequest = new PaymentRequest()
                                .clientToken(clientToken)
                                .submitButtonText("Add Amount");
                        startActivityForResult(paymentRequest.getIntent(this), REQUEST_CODE);


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Const.ServiceCode.CREATE_ADD_CARD_URL:
                AndyUtils.appLog("Ashutosh", "BrainTreeClientTokenResponse" + response);
                payStackDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {

                        AndyUtils.showShortToast("Card Added Successfully!", this);
                        getAddedCard();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Const.ServiceCode.REMOVE_CARD:
                Log.d("mahi", "delete card" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")) {
                        Commonutils.progressdialog_hide();
                        AndyUtils.showShortToast("Card Removed Successfully!", this);

                        getAddedCard();


                    } else {
                        Commonutils.progressdialog_hide();
                        String error_msg = jsonObject.getString("message");
                        Commonutils.showtoast(error_msg, this);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case Const.ServiceCode.GET_ADDED_CARDS_URL:
                AndyUtils.appLog("Ashutosh", "GetAddedCardResponse" + response);
Commonutils.progressdialog_hide();
                try {


                    JSONObject jsonObject = new JSONObject(response);
                    cardslst.clear();

                    if (jsonObject.getString("success").equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("cards");
                        if (jsonArray != null && jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject cardObject = jsonArray.getJSONObject(i);
                                Cards cardDetails = new Cards();
                                cardDetails.setCardId(cardObject.optString("id"));
                                cardDetails.setCardNumber(cardObject.optString("last_four"));
                                cardDetails.setIsDefault(cardObject.optString("is_default"));
                                cardDetails.setCardtype(cardObject.optString("card_type"));
                                cardDetails.setType(cardObject.optString("type"));
                                cardDetails.setEmail(cardObject.optString("email"));
                                cardslst.add(cardDetails);
                            }
                            if (cardslst != null) {
                                LinearLayoutManager layoutManager = new LinearLayoutManager(this);
                                rv_add_card.setLayoutManager(layoutManager);
                                adapter = new GetCardsAdapter(this, cardslst);
                                rv_add_card.setAdapter(adapter);
                                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(this, getResources().getIdentifier("layout_animation_from_left", "anim", getPackageName()));
                                rv_add_card.setLayoutAnimation(animation);
                                adapter.notifyDataSetChanged();
                                rv_add_card.scheduleLayoutAnimation();
                            }
                        }
                        JSONObject object = jsonObject.getJSONObject("tron_wallet");
                        text = object.getString("address_base58");// Whatever you need to encode in the QR code


                     marketPrice= " $" + jsonObject.getString("market_price")  ;

                        trxValue = jsonObject.getString("market_price");
                        if (Commonutils.mProgressDialog != null) {
                            Commonutils.progressdialog_hide();
                            //        AndyUtils.showSimpleProgressDialog(mContext, "Fetching All Cards...", false);
                        }
                        if (addCard == true && jsonArray.length() == 1) {
                            Intent intent = new Intent(this, NikolaWalletActivity.class);
                            startActivity(intent);
                            addCard = false;
                        }

                    } else {
                        if (Commonutils.mProgressDialog != null) {
                            Commonutils.progressdialog_hide();
                            //        AndyUtils.showSimpleProgressDialog(mContext, "Fetching All Cards...", false);
                        }
                        JSONObject object = jsonObject.getJSONObject("tron_wallet");
                        text = object.getString("address_base58");// Whatever you need to encode in the QR code


                        marketPrice= " $" + jsonObject.getString("market_price")  ;

                        trxValue = jsonObject.getString("market_price");
                        AndyUtils.showShortToast(jsonObject.getString("error_message"), this);
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }


}
