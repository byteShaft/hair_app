package com.byteshaft.hairrestorationcenter.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.byteshaft.hairrestorationcenter.R;
import com.byteshaft.hairrestorationcenter.utils.AppGlobals;
import com.byteshaft.hairrestorationcenter.utils.Helpers;
import com.byteshaft.hairrestorationcenter.utils.WebServiceHelpers;
import com.byteshaft.requests.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class AboutUsFragment extends Fragment implements HttpRequest.OnReadyStateChangeListener,
        HttpRequest.OnErrorListener{

    private View mBaseView;
    private TextView mAboutUsTextView;
    private String aboutUs;
    private  WebView mWebView;
    private boolean foreground = false;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.aboutus_fragment, container, false);
        setHasOptionsMenu(true);
        progressDialog = Helpers.getProgressDialog(getActivity());
        mAboutUsTextView = (TextView) mBaseView.findViewById(R.id.textview_about_us);
        mWebView = (WebView) mBaseView.findViewById(R.id.about_us_image);
        HttpRequest request = new HttpRequest(AppGlobals.getContext());
        request.setOnReadyStateChangeListener(this);
        request.setOnErrorListener(this);
        request.open("GET", "http://affordablehairtransplants.com/app/api/aboutus.php");
        request.send();
        if (AppGlobals.sIsInternetAvailable) {
            progressDialog.show();
        } else {
            Helpers.alertDialog(getActivity(), "No internet", "Please check your internet connection",
                    executeTask(true));
        }

        return mBaseView;
    }

    private Runnable executeTask(final boolean value) {
        Runnable runnable = new Runnable() {


            @Override
            public void run() {
//                new AboutUsTask(value).execute();
            }
        };
        return runnable;
    }

    @Override
    public void onResume() {
        super.onResume();
        foreground = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        foreground = false;
    }

    @Override
    public void onError(HttpRequest request, short error, Exception exception) {

    }

    @Override
    public void onReadyStateChange(HttpRequest request, int readyState) {
        if (readyState == HttpRequest.STATE_DONE) {
            progressDialog.dismiss();
            try {
                JSONObject obj = new JSONObject(request.getResponseText());
                String data = (String) obj.getJSONObject("details").get("aboutus");
                mWebView.loadData(data, "text/html; charset=utf-8", "UTF-8");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

//    private class AboutUsTask extends AsyncTask<String, String, String> {
//
//        private boolean checkInternet = false;
//
//        public AboutUsTask(boolean checkInternet) {
//            this.checkInternet = checkInternet;
//        }
//
//        private JSONObject jsonObject;
//        private ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog = Helpers.getProgressDialog(getActivity());
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... strings) {
//            if (AppGlobals.sIsInternetAvailable){
//                sendRequest();
//            } else if (checkInternet) {
//                if (WebServiceHelpers.isNetworkAvailable()) {
//                    sendRequest();
//                }
//            }
//            return aboutUs;
//        }
//
//        private void sendRequest() {
//            try {
//                jsonObject = WebServiceHelpers.aboutUs();
//                if (jsonObject.getString("Message").equals("Successfully")) {
//                    JSONObject data = jsonObject.getJSONObject("details");
//                    aboutUs = data.getString("aboutus");
//                    Log.i("String object", data.toString()  );
//                }
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            if (foreground) {
//                progressDialog.dismiss();
//                if (s != null) {
//                    mAboutUsTextView.setText(Html.fromHtml(s));
//                } else {
//                    Helpers.alertDialog(getActivity(), "No internet", "Please check your internet " +
//                            "connection", executeTask(true));
//                }
//            }
//        }
//    }
}
