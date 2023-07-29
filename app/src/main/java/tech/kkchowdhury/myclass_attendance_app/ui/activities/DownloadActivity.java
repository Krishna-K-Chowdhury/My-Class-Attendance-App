package tech.kkchowdhury.myclass_attendance_app.ui.activities;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import tech.kkchowdhury.myclass_attendance_app.R;
import tech.kkchowdhury.myclass_attendance_app.backend_api.ApiUrls;

public class DownloadActivity extends AppCompatActivity {

    ImageView imageView;
    WebView webView;
    DownloadManager downloadManager;
    long downloadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_download);
        webView = findViewById(R.id.web);
        String startDate = getIntent().getStringExtra("start_date");
        String endDate = getIntent().getStringExtra("end_date");
        String showCourse = getIntent().getStringExtra("show_course");
        webView.loadUrl(ApiUrls.DOWNLOAD_REPORT_API+"?t1="+startDate+"&t2="+endDate+"&t3="+showCourse);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        imageView = findViewById(R.id.gif_icon);
        Glide.with(this).asGif().load(R.raw.image_gif).into(imageView);

        // Define a Handler and a Runnable
        Handler handler = new Handler();
        Runnable changeGifRunnable = new Runnable() {
            @Override
            public void run() {
                // Load the second GIF after 3 seconds
                Glide.with(DownloadActivity.this).asGif().load(R.raw.report_downloaded).into(imageView);
            }
        };

        handler.postDelayed(changeGifRunnable, 3000);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_search);

        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(
                        Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                        "download.pdf");
                downloadId = downloadManager.enqueue(request);
            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_search:
                    return true;
                case R.id.bottom_settings:
                    startActivity(new Intent(getApplicationContext(), StudentsProfileView.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_profile:
                    startActivity(new Intent(getApplicationContext(), FacultyPersonalProfile.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

    }

    @Override
    public void onBackPressed() {
        if(webView.isFocused() && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            openDownloadedFile();
        }
    };

    protected void onResume() {
        super.onResume();
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(onComplete);
    }

    private void openDownloadedFile() {
        Uri fileUri = downloadManager.getUriForDownloadedFile(downloadId);
        if (fileUri != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        }
    }

}
