package com.sempal.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.usage.UsageEvents;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import com.onesignal.OneSignal;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import es.dmoral.toasty.Toasty;
@SuppressWarnings("unchecked")


public class MainActivity extends AppCompatActivity
       implements NavigationView.OnNavigationItemSelectedListener, RewardedVideoAdListener {

    Fragment fragment = null;
    // private FirebaseAnalytics mFirebaseAnalytics;
    DrawerLayout drawerLayout;
    WebView mWebView;
    private Menu optionsMenu;
    Toolbar toolbar;
    NavigationView navigationView;
    AdView mAdView;
    private InterstitialAd mInterstitialAd;
    ProgressBar progressBar;
    private RewardedVideoAd mRewardedVideoAd;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private int mOriginalOrientation;
    private int mOriginalSystemUiVisibility;
    NestedScrollView nestedScrollView;
    final String url = "https://www.google.com";

    final String admob_app_id = "ca-app-pub-5402205609052624~5410460116";
    final String admob_banner_id = "ca-app-pub-5402205609052624/3059628949";
    final String admob_inter_id = "ca-app-pub-5402205609052624/6309249442";
    FrameLayout frameLayout;

    // FloatingActionsMenu fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewInit();

        // setTitle("WebView");

        mWebView.loadUrl(url);

        setMySwipeRefreshLayout();

        setSupportActionBar(toolbar);

        //setmFirebaseAnalytics();

      //  floatingActionButton();

        setActionBarToogle();

        //setLocationPermission();

        // oneSignalInit();

        //checkPermission();//storage

        //webSettings();

        setAdmob();

        //setRTL();
    }

   // private void floatingActionButton() {
    //    FloatingActionButton fab = findViewById(R.//id.fab);
      //  fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
         //   final public void onClick(View view) {
           //     Intent callIntent = new Intent(Intent.ACTION_DIAL);
            //    Intent intent = callIntent.setData(Uri.parse(String.format("tel:%s", Uri.encode(ACCESSIBILITY_SERVICE.trim()))));
             //   callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             //   startActivity(intent);
           // }
       // });
   // }

    void setRTL() {
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
    }


    final void setAdmob() {
        MobileAds.initialize(this, admob_app_id);


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(admob_banner_id);


        prepareAd();

        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                Log.i("hello", "world");
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        } else {
                            Log.d("TAG", " Interstitial not loaded");
                        }

                        prepareAd();
                    }
                });
            }
        }, 150, 150, TimeUnit.SECONDS);
    }


    public void prepareAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5402205609052624/6309249442");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        //  requestNewInterstitial(); //add test device


        rewardAds();
    }

    private void rewardAds() {
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(" ca-app-pub-3602303733318285/548015013",//use this id for testing
                new AdRequest.Builder().build());



    }




    final void setMySwipeRefreshLayout() {
        mySwipeRefreshLayout = findViewById(R.id.swipeContainer);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    final public void onRefresh() {
                        mWebView.reload();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }


    final void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("B9C840C4E9AD8EC5D1497C9A62C56374")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    final void setLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, 0);
    }

    final void setActionBarToogle() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.mask);

    }



   /* final void setmFirebaseAnalytics(){
        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("class", "MainActivity");
        params.putString("userid", "12564578");
        firebaseAnalytics.logEvent("MainActivity", params);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params);

    */


    @SuppressLint("MissingSuperCall")
    @Override
    final public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebViewFragment inside Fragment
            // Use RESULT_OK only if you're implementing WebViewFragment inside an Activity
            Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }


    final void viewInit() {
        drawerLayout = findViewById(R.id.drawer_layout);
        mWebView = findViewById(R.id.mWebView);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        frameLayout = findViewById(R.id.content_frame);
        progressBar = findViewById(R.id.progressBar);
        nestedScrollView = findViewById(R.id.nested);


        mWebView.setWebViewClient(new WebViewClient() {


            public void onReceivedError(WebView mWebView, int i, String s, String d1) {
                Toasty.error(getApplicationContext(), "No Internet Connection!").show();
                mWebView.loadUrl("file:///android_asset/net_error.html");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

                    @Override
                    public void onPageFinished(WebView view, final String url) {
                        super.onPageFinished(view, url);
                        boolean d = false;
                        if (d == false) {
                            nestedScrollView.scrollTo(0, 0);
                            d = true;
                        }



                mWebView.setDownloadListener(new DownloadListener() {


                    @Override
                    public void onDownloadStart(String url, String userAgent,
                                                String contentDisposition, String mimeType,
                                                long contentLength) {
                        DownloadManager.Request request = new DownloadManager.Request(
                                Uri.parse(url));
                        request.setMimeType(mimeType);
                        String cookies = CookieManager.getInstance().getCookie(url);
                        request.addRequestHeader("cookie", cookies);
                        request.addRequestHeader("User-Agent", userAgent);
                        request.setDescription("Downloading file...");
                        request.setTitle(URLUtil.guessFileName(url, contentDisposition,
                                mimeType));
                        request.allowScanningByMediaScanner();
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(
                                        url, contentDisposition, mimeType));
                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                        dm.enqueue(request);
                        Toast.makeText(getApplicationContext(), "Downloading File",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                               //opening link external browser
                /*if(!url.contains("android_asset")){
                    view.setWebViewClient(null);
                } else {
                    view.setWebViewClient(new WebViewClient());
                }*/

                if (url.contains("youtube.com") || url.contains("play.google.com") || url.contains("google.com/maps")
                        || url.contains("facebook.com") || url.contains("twitter.com") || url.contains("instagram.com")
                        || url.contains("https://www.remove.bg/") || url.contains("https://filmyzilla.com.co/") || url.contains("https://wapking.online/")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else if (url.startsWith("mailto")) {
                    handleMailToLink(url);
                    return true;
                } else if (url.startsWith("tel:")) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
                    return true;
                } else if (url.startsWith("sms:")) {
                    // Handle the sms: link
                    handleSMSLink(url);

                    // Return true means, leave the current web view and handle the url itself
                    return true;
                } else if (url.contains("geo:")) {
                    Uri gmmIntentUri = Uri.parse(url);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(mapIntent);
                    }
                    return true;
                }

                view.loadUrl(url);
                return true;
            }

        });


        mWebView.setWebChromeClient(new WebChromeClient() {

            public Bitmap getDefaultVideoPoster() {
                if (mCustomView == null) {
                    return null;
                }
                return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
            }

            public void onHideCustomView() {
                ((FrameLayout) getWindow().getDecorView()).removeView(mCustomView);
                mCustomView = null;
                getWindow().getDecorView().setSystemUiVisibility(mOriginalSystemUiVisibility);
                setRequestedOrientation(mOriginalOrientation);
                mCustomViewCallback.onCustomViewHidden();
                mCustomViewCallback = null;
            }

            public void onShowCustomView(View paramView, CustomViewCallback paramCustomViewCallback) {
                if (mCustomView != null) {
                    onHideCustomView();
                    return;
                }
                mCustomView = paramView;
                mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
                mOriginalOrientation = getRequestedOrientation();
                mCustomViewCallback = paramCustomViewCallback;
                ((FrameLayout) getWindow().getDecorView()).addView(mCustomView, new FrameLayout.LayoutParams(-1, -1));
                getWindow().getDecorView().setSystemUiVisibility(3846);
            }


            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            final protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }


            // For Lollipop 5.0+ Devices
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    //eski---- >   Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }


            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin,
                                                           GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

        WebSettings webSettings = mWebView.getSettings();
        AppUpdateChecker appUpdateChecker=new AppUpdateChecker(this);  //pass the activity in constructure
        appUpdateChecker.checkForUpdate(false); //mannual check false here
        mWebView.getSettings().setBuiltInZoomControls(true);

        //This will zoom out the WebView
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setInitialScale(1);
        webSettings.setDomStorageEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.getSaveFormData();
        webSettings.setDisplayZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(true);
        webSettings.setSupportMultipleWindows(true); //?a href problem
        webSettings.getJavaScriptEnabled();
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setLoadsImagesAutomatically(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); //(popup)
    }


    protected void handleSMSLink(String url) {
        /*
            If you want to ensure that your intent is handled only by a text messaging app (and not
            other email or social apps), then use the ACTION_SENDTO action
            and include the "smsto:" data scheme
        */

        // Initialize a new intent to send sms message
        Intent intent = new Intent(Intent.ACTION_SENDTO);

        // Extract the phoneNumber from sms url
        String phoneNumber = url.split("[:?]")[1];

        if (!TextUtils.isEmpty(phoneNumber)) {
            // Set intent data
            // This ensures only SMS apps respond
            intent.setData(Uri.parse("smsto:" + phoneNumber));

            // Alternate data scheme
            //intent.setData(Uri.parse("sms:" + phoneNumber));
        } else {
            // If the sms link built without phone number
            intent.setData(Uri.parse("smsto:"));

            // Alternate data scheme
            //intent.setData(Uri.parse("sms:" + phoneNumber));
        }


        // Extract the sms body from sms url
        if (url.contains("body=")) {
            String smsBody = url.split("body=")[1];

            // Encode the sms body
            try {
                smsBody = URLDecoder.decode(smsBody, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (!TextUtils.isEmpty(smsBody)) {
                // Set intent body
                intent.putExtra("sms_body", smsBody);
            }
        }

        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the sms app
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "No SMS app found.", Toast.LENGTH_SHORT).show();
        }
    }


    // Custom method to handle web view mailto link
    protected void handleMailToLink(String url) {
        // Initialize a new intent which action is send
        Intent intent = new Intent(Intent.ACTION_SENDTO);

        // For only email app handle this intent
        intent.setData(Uri.parse("mailto:"));

        String mString = "";
        // Extract the email address from mailto url
        String to = url.split("[:?]")[1];
        if (!TextUtils.isEmpty(to)) {
            String[] toArray = to.split(";");
            // Put the primary email addresses array into intent
            intent.putExtra(Intent.EXTRA_EMAIL, toArray);
            mString += ("TO : " + to);
        }

        // Extract the cc
        if (url.contains("cc=")) {
            String cc = url.split("cc=")[1];
            if (!TextUtils.isEmpty(cc)) {
                //cc = cc.split("&")[0];
                cc = cc.split("&")[0];
                String[] ccArray = cc.split(";");
                // Put the cc email addresses array into intent
                intent.putExtra(Intent.EXTRA_CC, ccArray);
                mString += ("\nCC : " + cc);
            }
        } else {
            mString += ("\n" + "No CC");
        }

        // Extract the bcc
        if (url.contains("bcc=")) {
            String bcc = url.split("bcc=")[1];
            if (!TextUtils.isEmpty(bcc)) {
                //cc = cc.split("&")[0];
                bcc = bcc.split("&")[0];
                String[] bccArray = bcc.split(";");
                // Put the bcc email addresses array into intent
                intent.putExtra(Intent.EXTRA_BCC, bccArray);
                mString += ("\nBCC : " + bcc);
            }
        } else {
            mString += ("\n" + "No BCC");
        }

        // Extract the subject
        if (url.contains("subject=")) {
            String subject = url.split("subject=")[1];
            if (!TextUtils.isEmpty(subject)) {
                subject = subject.split("&")[0];
                // Encode the subject
                try {
                    subject = URLDecoder.decode(subject, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // Put the mail subject into intent
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                mString += ("\nSUBJECT : " + subject);
            }
        } else {
            mString += ("\n" + "No SUBJECT");
        }

        // Extract the body
        if (url.contains("body=")) {
            String body = url.split("body=")[1];
            if (!TextUtils.isEmpty(body)) {
                body = body.split("&")[0];
                // Encode the body text
                try {
                    body = URLDecoder.decode(body, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // Put the mail body into intent
                intent.putExtra(Intent.EXTRA_TEXT, body);
                mString += ("\nBODY : " + body);
            }
        } else {
            mString += ("\n" + "No BODY");
        }

        // Email address not null or empty
        if (!TextUtils.isEmpty(to)) {
            if (intent.resolveActivity(getPackageManager()) != null) {
                // Finally, open the mail client activity
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "No email client found.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    final void oneSignalInit() {
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    protected void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            123
                    );

                } else {
                    // Request permission
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            123
                    );
                }
            } else {
                // Permission already granted
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportActionBar().getTitle().equals("Local Page")) {
            setTitle("WebView");
            FrameLayout frameLayout = findViewById(R.id.content_frame);
            frameLayout.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
            mWebView.loadUrl(url);
        } else if (mWebView.canGoBack())
            mWebView.goBack();
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to exit the application?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        final public void onClick(DialogInterface arg0, int arg1) {
                            MainActivity.super.onBackPressed();
                        }
                    }).create().show();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_back) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            }
            return true;

        } else if (id == R.id.action_forward) {
            if (mWebView.canGoForward()) {
                mWebView.goForward();
            }
        } else if (id == R.id.action_light) {
            Intent intent = new Intent(MainActivity.this, light.class);
            startActivity(intent);

        } else if (id == R.id.phone) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            Intent intent = callIntent.setData(Uri.parse(String.format("tel:%s", Uri.encode(ACCESSIBILITY_SERVICE.trim()))));
            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        } else if (id == R.id.qr) {
            Intent intent = new Intent(MainActivity.this, qr_code.class);
            startActivity(intent);

        } else if (id == R.id.qrcodege) {
            Intent intent = new Intent(MainActivity.this, qrcode_ganretor.class);
            startActivity(intent);


        }

        else if(id == R.id.action_refresh){
            mWebView.reload();
        }
        else if(id == R.id.action_share){
            share();
        }
        else if(id == R.id.action_copy){
            copyToPanel(getApplicationContext(),mWebView.getUrl());
            Snackbar snackbar = Snackbar.make(drawerLayout, " Your Link Copied.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        return super.onOptionsItemSelected(item);
    }


    final public void copyToPanel(Context context, String text) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied.", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    final void share(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
        String shareMessage= "\nLet me recommend you this application\n\n";
        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
        startActivity(Intent.createChooser(shareIntent, "choose one"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.optionsMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homepage_actions, menu);
        getMenuInflater().inflate(R.menu.main, optionsMenu);
        return super.onCreateOptionsMenu(menu);
    }

    final void setFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_contact) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + this.getPackageName())));
            } catch (android.content.ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
            }
        } else if (id == R.id.ads) {
            fragment = null;
            //   setTitle("Home");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://accounts.google.com/signout/chrome/landing?continue=https://apps.admob.com/&oc=https://apps.admob.com/");

        } else if (id==R.id.action_share){
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));

        } else if (id==R.id.action_copy){
            copyToPanel(getApplicationContext(),mWebView.getUrl());
            Snackbar snackbar = Snackbar.make(drawerLayout, "Link Copied.", Snackbar.LENGTH_LONG);
            snackbar.show();


        } else if (id == R.id.nav_home) {
            fragment = null;
            //   setTitle("Home");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);

            mWebView.loadUrl("https://news.google.com/?hl=hi&gl=IN&ceid=IN%3Ahi");

        } else if (id == R.id.more) {
            fragment = null;
            //   setTitle("Home");
            //  mWebView.setVisibility(View.VISIBLE);
            //   frameLayout.setVisibility(View.GONE);
            // mWebView.loadUrl("https://news.google.com/?hl=hi&gl=IN&ceid=IN%3Ahi");

            Uri uriUrl = Uri.parse("https://play.google.com/store/apps/collection/cluster?clp=igM9ChkKEzc4NzM3MzAyNzUzODM0MTQ4NTUQCBgDEh4KGGNvbS5zZW1wYWwubXlhcHBsaWNhdGlvbhABGAMYAQ%3D%3D:S:ANO1ljK11L4&gsr=CkCKAz0KGQoTNzg3MzczMDI3NTM4MzQxNDg1NRAIGAMSHgoYY29tLnNlbXBhbC5teWFwcGxpY2F0aW9uEAEYAxgB:S:ANO1ljJnX1Y");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);

        }else if (id==R.id.langg){
        fragment=null;
        Uri uriUrl=Uri .parse("https://www.learnvern.com/courses");
        Intent launchBrowser=new Intent (Intent.ACTION_VIEW,uriUrl);
        startActivity(launchBrowser);


        } else if (id == R.id.education) {
            fragment = null;
            //   setTitle("Home");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);

            mWebView.loadUrl("https://byjus.com/ncert-solutions-class-10-science/");

        } else if (id == R.id.code) {
            fragment = null;
            //   setTitle("Home");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);

            mWebView.loadUrl("https://www.xda-developers.com/codes-hidden-android/");


        } else if (id == R.id.shortcut) {
            fragment = null;
            //   setTitle("Home");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);

            mWebView.loadUrl("https://www.indiatoday.in/information/story/computer-shortcut-keys-everyone-should-know-1480158-2019-03-17");


        } else if (id == R.id.location) {
            fragment = null;
            //   setTitle("Home");
           Intent intent=new Intent(MainActivity.this,livelocation.class);
           startActivity(intent);

        } else if (id == R.id.age) {
            fragment = null;
            //   setTitle("Home");
            Intent intent=new Intent(MainActivity.this,age_calculator.class);
            startActivity(intent);

        } else if (id == R.id.device) {
            fragment = null;
            //   setTitle("Home");
            Intent intent=new Intent(MainActivity.this,deviceinfo.class);
            startActivity(intent);

        } else if (id == R.id.app) {
            fragment = null;
            //   setTitle("Home");
            Intent intent=new Intent(MainActivity.this,about2.class);
            startActivity(intent);


        } else if (id == R.id.pdfimage) {
            fragment = null;
            //   setTitle("Home");
            Intent intent=new Intent(MainActivity.this,viewpdf.class);
            startActivity(intent);



        } else if (id == R.id.kids) {
            fragment = null;
            //   setTitle("Home");
            Intent intent=new Intent(MainActivity.this,Homescreen.class);
            startActivity(intent);


        } else if (id == R.id.speedometer) {
            fragment = null;
            //   setTitle("Home");
            Intent intent=new Intent(MainActivity.this,speedometer.class);
            startActivity(intent);

        } else if (id == R.id.google) {
           fragment = null;
            // setTitle("Google");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.google.com/");

        } else if (id == R.id.video) {
            fragment = null;
            // setTitle("Google");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.mxplayer.in/music");

        } else if (id == R.id.old) {
            fragment = null;
            // setTitle("Google");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://wynk.in/music");

        } else if (id == R.id.adult) {
            fragment = null;
            // setTitle("Google");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.santabanta.com/sms/hindi-restricted/funny/");

        } else if (id == R.id.arch) {
            fragment = null;
            // setTitle("Google");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://archive.org/");

        } else if (id == R.id.comedy) {
            fragment = null;
            // setTitle("Google");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://sms.hindijokes.co/");

        } else if (id == R.id.comedyy) {
            fragment = null;
            // setTitle("Google");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://news.google.com/?hl=hi&gl=IN&ceid=IN:hi");



        } else if (id == R.id.facebook) {
            fragment = null;
            // setTitle("facebook");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.facebook.com/");

        } else if (id == R.id.whatsapp) {
            fragment = null;
            // setTitle("facebook");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.atmegame.com/online-mobile-games");

          //  Uri uriUrl = Uri.parse("https://www.tiktok.com/trending?lang=en");
          //   Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
          //    startActivity(launchBrowser);


        } else if (id == R.id.mobwal) {
            fragment = null;
            // setTitle("facebook");
         //   mWebView.setVisibility(View.VISIBLE);
         //   frameLayout.setVisibility(View.GONE);
          //  mWebView.loadUrl("https://www.atmegame.com/online-mobile-games");

              Uri uriUrl = Uri.parse("https://www.setaswall.com/1080x1920-wallpapers/");
               Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);


        } else if (id == R.id.bing) {
            fragment = null;
            //  setTitle("Bing");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.bing.com/");

        } else if (id == R.id.yahoo) {
            fragment = null;
            // setTitle("Yahoo !");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://in.yahoo.com/?guccounter=1&guce_referrer=aHR0cHM6Ly93d3cuZ29vZ2xlLmNvbS8&guce_referrer_sig=AQAAAEPVBL56ZxpvNuyKkP8NLUcZCCEWxr8-hgxysfX4TndEPq6MdsE8wxNErqXO-BBaZ5DS1kc1e723YOseZeklmUOQjeuOt4SUVjvjINlXksTMCcOG7yrRu29Fzk5V-iAsCavreAL_H0cxvfM-9KPyq_B4kQ2s7fbY_j6jQs_L95wM");

        } else if (id == R.id.youtube) {
            fragment = null;
            //  setTitle("youtube");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.youtube.com/");

        } else if (id == R.id.wiki) {
            fragment = null;
            // setTitle("wikipedia");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.wikipedia.org/");

        } else if (id == R.id.ask) {
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.ask.com/");

        } else if (id == R.id.twitter) {
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://twitter.com/login?lang=en");


        } else if (id == R.id.ins) {
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.instagram.com/");

        } else if (id == R.id.study) {
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://gk-hindi.in/gk-questions");


        } else if (id == R.id.link) {
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://in.linkedin.com/");


        } else if (id == R.id.amazon) {
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.amazon.in/");

        } else if (id == R.id.flipkart) {
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.flipkart.com/");

        } else if (id == R.id.hotstar) {
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.hotstar.com/in");

        } else if (id ==R.id.mobile) {
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.smartprix.com/");

        } else if (id == R.id.calculator) {
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.calculator.net/");

        } else if (id == R.id.paytm) {
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://paytm.com/");

        } else if (id == R.id.paypal) {
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.paypal.com/in/home");

        } else if (id == R.id.cricket) {
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.cricbuzz.com/cricket-match/live-scores");

        } else if (id == R.id.notepad) {
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://anotepad.com/");

        } else if (id == R.id.weather) {
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.accuweather.com/en/in/india-weather");

         //   Uri uriUrl = Uri.parse("https://www.google.com/maps/@28.865021,78.7589552,15z");
           // Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
          //  startActivity(launchBrowser);


        } else if (id == R.id.lang){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://translate.google.co.in/");

        } else if (id == R.id.dic){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.oxfordlearnersdictionaries.com/");

        } else if (id == R.id.stop){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.timeanddate.com/stopwatch/");

        } else if (id == R.id.speed){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://speedtest.telstra.com/");

        } else if (id == R.id.gmail){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://accounts.google.com/ServiceLogin/identifier?service=mail&passive=true&rm=false&continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&ss=1&scc=1&ltmpl=default&ltmplcache=2&emr=1&osid=1&flowName=GlifWebSignIn&flowEntry=AddSession");

        } else if (id == R.id.googlep){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://aboutme.google.com/u/0/?referer=gplus");

        } else if (id == R.id.curancy){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.xe.com/currencyconverter/");

        } else if (id == R.id.unit){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.unitconverters.net/");

        } else if (id == R.id.fullform){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.javatpoint.com/full-form");

        } else if (id == R.id.pdf){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.pdfdrive.com/");

        } else if (id == R.id.music){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.hungama.com/");

        } else if (id == R.id.ganna){
            fragment = null;
            // setTitle("ask");
           // mWebView.setVisibility(View.VISIBLE);
           // frameLayout.setVisibility(View.GONE);
           // mWebView.loadUrl("https://caneup.in/Default.aspx");

            Uri uriUrl = Uri.parse("https://caneup.in/Default.aspx");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
              startActivity(launchBrowser);


        } else if (id == R.id.sarkari){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://sarkariresults.info/");


        } else if (id == R.id.health){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://navbharattimes.indiatimes.com/lifestyle/health/articlelist/2355115.cms");

        } else if (id == R.id.train){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.makemytrip.com/railways/liveStatus/");

        } else if (id == R.id.car){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://vahan.nic.in/nrservices/faces/user/searchstatus.xhtml");

        } else if (id == R.id.map){
            fragment = null;
            // setTitle("ask");
          //  mWebView.setVisibility(View.VISIBLE);
          //  frameLayout.setVisibility(View.GONE);
          //  mWebView.loadUrl("https://www.google.com/maps/@28.865021,78.7589552,15z");

            Uri uriUrl = Uri.parse("https://www.google.com/maps/@28.865021,78.7589552,15z");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
               startActivity(launchBrowser);


        } else if (id == R.id.download){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://wapking.online/");

          //  Uri uriUrl = Uri.parse("https://wapking.online/");
            //Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
         //   startActivity(launchBrowser);

        } else if (id == R.id.remover){
            fragment = null;
            // setTitle("ask");
           //  mWebView.setVisibility(View.VISIBLE);
           //  frameLayout.setVisibility(View.GONE);
           //   mWebView.loadUrl("https://www.remove.bg/");

            Uri uriUrl = Uri.parse("https://www.remove.bg/");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);

        } else if (id == R.id.movie){
            fragment = null;
            // setTitle("ask");
           // mWebView.setVisibility(View.VISIBLE);
          //  frameLayout.setVisibility(View.GONE);
          //  mWebView.loadUrl("https://filmyzilla.com.co/");

            Uri uriUrl = Uri.parse("https://filmyzilla.com.co/");
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);


        } else if (id == R.id.story){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://hindi.pratilipi.com/");

        } else if (id == R.id.news){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://hindi.indiatvnews.com/livetv");

        } else if (id == R.id.find){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://lamplightdev.github.io/compass/");


        } else if (id == R.id.newss){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://www.indiatoday.in/aajtak-livetv");


        } else if (id == R.id.privacy){
            fragment = null;
            // setTitle("ask");
            mWebView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.GONE);
            mWebView.loadUrl("https://sites.google.com/view/allinoneapps/home");


        }

        if (fragment != null) {
            setFragment();
            mWebView.setVisibility(View.GONE);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRewarded(RewardItem reward) {
        Toast.makeText(this, "Download to Earn" + reward.getType() + "  amount: " +
                reward.getAmount(), Toast.LENGTH_SHORT).show();

        // Reward the user.
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
      //  Toast.makeText(this, "onRewardedVideoAdLeftApplication",
          //      Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
       // Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
       // Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    @Override
    public void onRewardedVideoAdLoaded() {
      //  Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {
       // Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
      //  Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    public void homee(View view) {
        Intent intent=new Intent(MainActivity.this,Homescreen.class);
        startActivity(intent);
    }
}