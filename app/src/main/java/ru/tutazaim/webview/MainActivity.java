package ru.tutazaim.webview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    WebView myWebView;
    ProgressBar progressBar;
    //LinearLayout errorView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_circular);
        myWebView = findViewById(R.id.web_view);
        //errorView = findViewById(R.id.error_view);

        WebSettings settings = myWebView.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.loadUrl("https://tutazaim.ru");

        FloatingActionButton fab = findViewById(R.id.reload_button);
        fab.setOnClickListener(view -> myWebView.reload());

        subscribeToDefaultTopic();
        printKeyHash();
    }
    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager()
                    .getPackageInfo("io.github.ziginsider.facebooksdkdemo",
                            PackageManager.GET_SIGNATURES);
            for (Signature signature:info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", "KeyHash: " + Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    void subscribeToDefaultTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.default_topic))
                .addOnCompleteListener(task -> {
                    Log.d("Firebase", "Success subscribe to default topic");
                    if (!task.isSuccessful()) {
                        Log.d("Firebase", "Error subscribe to default topic");
                    }
                });
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("WebView", String.format("shouldOverrideUrlLoading %s", url));
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e("WebView", String.format("Error %s code %d", error.getDescription(), error.getErrorCode()));
            } else {
                Log.e("WebView", "Some error");
            }
            //view.setVisibility(View.INVISIBLE);
            //errorView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("WebView", String.format("onPageStarted %s", url));
            //view.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            //errorView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d("WebView", String.format("onPageFinished %s", url));
            Log.d("WebView", String.format("onPageFinished %s", url));
            progressBar.setVisibility(View.INVISIBLE);
            view.clearCache(true);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            Log.d("WebView", String.format("Updated %s is reload=%b", url, isReload));
            super.doUpdateVisitedHistory(view, url, isReload);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e("WebView", String.format("Error code %d", errorResponse.getStatusCode()));
            } else {
                Log.e("WebView", "Some error");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            Log.d("WebView", "canGoBack");
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}