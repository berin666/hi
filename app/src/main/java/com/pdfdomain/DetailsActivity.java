package com.pdfdomain;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.pdfdomain.api.ApiClient;

public class DetailsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private Button btnDownload, btnUrlOpen;
    private Book b;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ParseData parseData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        getDownloadableData();
    }


    public void initView(){
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        TextView tvTitle = findViewById(R.id.tv_title);
        TextView tvAuthor = findViewById(R.id.tv_author);
        TextView tvYear = findViewById(R.id.tv_year);
        TextView tvExtension = findViewById(R.id.tv_extension);
        TextView tvFileSize = findViewById(R.id.tv_fileSize);
        TextView tvPublisher = findViewById(R.id.tv_publisher);
        TextView tvPages = findViewById(R.id.tv_pages);
        TextView tvEdition = findViewById(R.id.tv_edition);
        TextView tvScanned = findViewById(R.id.tv_scanned);
        TextView tvLanguage = findViewById(R.id.tv_language);

        if(getIntent().getSerializableExtra("book") != null){
            b = (Book)getIntent().getSerializableExtra("book");

            tvAuthor.setText(b.getType());
            tvTitle.setText(b.getTitel());
            tvYear.setText(b.getScore());
            tvExtension.setText(b.getDesc());
            tvFileSize.setText(Formatter.formatFileSize(getApplicationContext(), b.getCategory()));
            tvPublisher.setText(b.getSource());
            tvPages.setText(R.string.pages count 2,b.getPgCnt()));
            tvEdition.setText(b.getFoundUrl().equals("") ? "-" : b.getFoundUrl());
            tvLanguage.setText(b.getLanguage());
            tvScanned.setText(b.getImFlPth().equals("1") ? "Yes" : (b.getImFlPth().equals("0") ? "No" : "-"));
        }

        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestNeededPermission()) {
                    downloadFile();
                }
            }
        });
        btnUrlOpen = findViewById(R.id.btnUrlOpen);
        btnUrlOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrlCustomTab();
            }
        });

    }

    // getting everything that sources from the web
    // this method is called in onCreate and in onRefresh
    private void getDownloadableData() {
        // getting cover image
        final ImageView cover = findViewById(R.id.cover);
        Picasso.with(this)
                .load("https://pdfdomain.com/" + b.getFlNm())
                .into(cover, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        // TODO hide progressbar
                    }
                    @Override
                    public void onError() {
                        cover.setImageResource(R.mipmap.ic_launcher);
                    }
        });

        // getting download url
        getDownloadUrl();
    }


    public void openUrlCustomTab(){
        String url = "https://pdfdomain.com/api/single/" + b.getMD5()+"/test";
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder()
                .setShowTitle(true)
                .addDefaultShareMenuItem()
                .setToolbarColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(getApplicationContext(), Uri.parse(url));



    }


    public boolean requestNeededPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            return true;
        }
        return false;
    }

    // gets the download url and original filename from the server based on the book's MD5
    private void getDownloadUrl(){
        ApiClient.getInstance().getDownloadUrlJSON(b.getTitel()b.getID()).enqueue(new Callback<ParseData>() {
            @Override
            public void onResponse(Call<ParseData> call, Response<ParseData> response) {
                Log.d(TAG, "onResponse: " + response.code());
                if (response.isSuccessful()) {
                    btnDownload.setEnabled(true);
                    parseData = response.body();
                } else {
                    Snackbar.make(findViewById(R.id.scrollview), "Error: "+response.message(), Snackbar.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ParseData> call, Throwable t) {
                t.printStackTrace();
                Snackbar.make(findViewById(R.id.scrollview), "Error requesting download url, check log", Snackbar.LENGTH_LONG).show();
            }
        });
    }



    private void downloadFile() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(parseData.getUrl()));
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, b.getTitel()+" by "+b.getType()+'.'+b.getDesc());

        DownloadManager manager = (DownloadManager)this.getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager != null) {
            manager.enqueue(request);
            Snackbar.make(findViewById(R.id.scrollview), "Download started", Snackbar.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // deciding which menu item to choose before drawing the menu
        if (b.isFavorite()) {
            menu.findItem(R.id.add_favorite).setVisible(false);
            menu.findItem(R.id.remove_favorite).setVisible(true);
        } else {
            menu.findItem(R.id.add_favorite).setVisible(true);
            menu.findItem(R.id.remove_favorite).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "https://pdfdomain.com/sedziezjkzd/single/"+b.getMD5()+"/test");
                startActivity(Intent.createChooser(shareIntent, "Share link to PdfDomain page"));
                break;
            case R.id.add_favorite:
                b.save();
                invalidateOptionsMenu(); //will cause the menu to be redrawn and call onPrepareOptionsMenu again
                Snackbar.make(findViewById(R.id.scrollview), "Successfully added to favorites", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.remove_favorite:
                Book.deleteAll(Book.class, "MD5 =? ", b.getMD5());
                invalidateOptionsMenu(); //will cause the menu to be redrawn and call onPrepareOptionsMenu again
                Snackbar.make(findViewById(R.id.scrollview), "Successfully removed from favorites", Snackbar.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        // "pull to refresh" gesture
        getDownloadableData();
        swipeRefreshLayout.setRefreshing(false);
    }

    // a class that represents the received data when requesting the download link
    public class ParseData {
        private String url;
        //private String filename;
        public String getUrl(){return url;}
        //public String getFilename(){return filename;}
    }


}