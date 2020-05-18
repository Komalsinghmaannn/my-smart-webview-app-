package com.sempal.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.khizar1556.mkvideoplayer.MKPlayerActivity;
import com.sempal.myapplication.utils.StatusBarUtil;

import net.alhazmy13.mediapicker.Video.VideoPicker;

import java.util.List;


public class Main2Activity extends AppCompatActivity implements View.OnClickListener {


    public static boolean isLaunch = false;
    private AutoCompleteTextView etSearch;
    private RadioButton rbSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 0);
        initView();
        isLaunch = true;
    }

    private void initView() {
        findViewById(R.id.bt_deeplink).setOnClickListener(this);
        findViewById(R.id.bt_openUrl).setOnClickListener(this);
        findViewById(R.id.bt_baidu).setOnClickListener(this);
        findViewById(R.id.bt_movie).setOnClickListener(this);
        findViewById(R.id.bt_upload_photo).setOnClickListener(this);
        findViewById(R.id.bt_call).setOnClickListener(this);
        findViewById(R.id.bt_java_js).setOnClickListener(this);

        rbSystem = findViewById(R.id.rb_system);
        etSearch = findViewById(R.id.et_search);
        rbSystem.setChecked(true);
        TextView tvVersion = findViewById(R.id.tv_version);
        tvVersion.setText(String.format("❤版本：v%s", BuildConfig.VERSION_NAME));
        tvVersion.setOnClickListener(this);
        /** 处理键盘搜索键 */
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    openUrl();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_openUrl:
                openUrl();
                break;
            case R.id.bt_baidu:// 百度一下
                String baiDuUrl = "http://www.baidu.com";
                loadUrl(baiDuUrl, getString(R.string.text_baidu));
                break;
            case R.id.bt_movie:// 网络视频
                String movieUrl = "https://sv.baidu.com/videoui/page/videoland?context=%7B%22nid%22%3A%22sv_5861863042579737844%22%7D&pd=feedtab_h5";
                loadUrl(movieUrl, getString(R.string.text_movie));
                break;
            case R.id.bt_upload_photo:// 上传图片
                String uploadUrl = "file:///android_asset/upload_photo.html";
                loadUrl(uploadUrl, getString(R.string.text_movie));
                break;
            case R.id.bt_call:// 打电话、发短信、发邮件、JS
                String callUrl = "file:///android_asset/callsms.html";
                loadUrl(callUrl, getString(R.string.text_js));
                break;
            case R.id.bt_java_js://  js与android原生代码互调
                String javaJs = "file:///android_asset/java_js.html";
                loadUrl(javaJs, getString(R.string.js_android));
                break;
            case R.id.bt_deeplink:// DeepLink通过网页跳入App
                String deepLinkUrl = "file:///android_asset/deeplink.html";
                loadUrl(deepLinkUrl, getString(R.string.deeplink));
                break;
            case R.id.tv_version:
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("感谢");
                builder.setMessage("开源不易，给作者一个star好吗？😊");
                builder.setNegativeButton("已给", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Main2Activity.this, "感谢老铁~", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setPositiveButton("去star", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadUrl("https://github.com/youlookwhat/WebViewStudy", "WebViewStudy");
                    }
                });
                builder.show();
                break;
            default:
                break;
        }
    }

    /**
     * 打开网页
     */
    private void openUrl() {
        String url = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(url)) {
            // 空url
            url = "https://www.google.com";

        } else if (!url.startsWith("http") && url.contains("http")) {
            // 有http且不在头部
            url = url.substring(url.indexOf("http"), url.length());

        } else if (url.startsWith("www")) {
            // 以"www"开头
            url = "http://" + url;

        } else if (!url.startsWith("http") && (url.contains(".me") || url.contains(".com") || url.contains(".cn"))) {
            // 不以"http"开头且有后缀
            url = "http://www." + url;

        } else if (!url.startsWith("http") && !url.contains("www")) {
            // 输入纯文字 或 汉字的情况
            url = "https://www.google.com/search?q=" + url;
        }
        loadUrl(url, "详情");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionbar_update:
                loadUrl("http://d.6short.com/webviewstudy", "网页浏览器 - fir.im");
                break;
            case R.id.actionbar_about:
                loadUrl("https://github.com/youlookwhat/WebViewStudy", "WebViewStudy");
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadUrl(String mUrl, String mTitle) {
        if (rbSystem.isChecked()) {
            browser2.loadUrl(this, mUrl, mTitle);
        } else {
            browser2.loadUrl(this, mUrl, mTitle);
        }
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isLaunch = false;
    }

    public void google(View view) {
        Intent google = new Intent(Main2Activity.this, browser2.class);
        String baiDuUrl = "http://www.google.com";
        loadUrl(baiDuUrl, getString(R.string.google));
        startActivity(google);

    }

    public void whatsapp(View view) {
        Intent intent = new Intent(Main2Activity.this, whatsappweb.class);
        startActivity(intent);
    }

    public void calculators(View view) {
        Intent intent = new Intent(Main2Activity.this, calculators.class);
        startActivity(intent);
    }

    public void youtube(View view) {
        Intent google = new Intent(Main2Activity.this, browser2.class);
        String baiDuUrl = "http://www.youtube.com";
        loadUrl(baiDuUrl, getString(R.string.youtube));
        startActivity(google);
        //Uri uriUrl = Uri.parse("https://www.youtube");
        //  Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        // startActivity(launchBrowser);


    }

    public void facebook(View view) {
        Intent google = new Intent(Main2Activity.this, browser2.class);
        String baiDuUrl = "http://www.facebook.com";
        loadUrl(baiDuUrl, getString(R.string.facebook));
        startActivity(google);
    }

    public void video(View view) {
        new VideoPicker.Builder(Main2Activity.this)
                .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                .directory(VideoPicker.Directory.DEFAULT)
                .extension(VideoPicker.Extension.MP4)
                .enableDebuggingMode(true)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VideoPicker.VIDEO_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> mPaths = data.getStringArrayListExtra(VideoPicker.EXTRA_VIDEO_PATH);
            MKPlayerActivity.configPlayer(this).play(String.valueOf(mPaths.get(0)));
        }


    }

    public void audio(View view) {
        Intent intent = new Intent(Main2Activity.this, audioplayer.class);
        startActivity(intent);
    }

    public void deviceinfo(View view) {
        Intent intent = new Intent(Main2Activity.this, deviceinfo.class);
        startActivity(intent);
    }

    public void tiktok(View view) {
        Intent google = new Intent(Main2Activity.this, browser2.class);
        String baiDuUrl = "https://www.tiktok.com/trending/?lang=en";
        loadUrl(baiDuUrl, getString(R.string.tiktok));
        startActivity(google);
    }

    public void game(View view) {
        Intent google = new Intent(Main2Activity.this, browser2.class);
        String baiDuUrl = "https://m.silvergames.com/en/os/android";
        loadUrl(baiDuUrl, getString(R.string.game));
        startActivity(google);
    }

    public void filemanager(View view) {
        Intent intent = new Intent(Main2Activity.this, whatsappweb.class);
        startActivity(intent);
    }

}