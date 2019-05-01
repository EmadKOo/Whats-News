package emad.whatsnews;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

public class DisplayImageActivity extends AppCompatActivity {

    Toolbar imgToolbar;
    ImageView displayImageNews;
    ImageView displayImageBack;
    ImageView displayImageMore;
    String imgUrl = "";
    BottomSheetDialog bottomSheetDialog;
    LinearLayout saveLayout;
    LinearLayout shareLayout;
    LinearLayout cancelLayout;
    int numOfClicks = 0;

    private static final String TAG = "DisplayImageActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image);
        imgUrl = getIntent().getStringExtra("imgUrl");
        initViews();
        handleViews();
    }

    public void initViews(){
        imgToolbar = findViewById(R.id.imgToolbar);
        setSupportActionBar(imgToolbar);
        displayImageNews = findViewById(R.id.displayImageNews);
        displayImageBack= findViewById(R.id.displayImageBack);
        displayImageMore = findViewById(R.id.displayImageMore);
    }

    public void handleViews(){
        Picasso.get().load(imgUrl).into(displayImageNews);

        displayImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        displayImageMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show bottom sheet // save share
                showBottomSheet();
            }
        });

        displayImageNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numOfClicks +=1;
                Log.d(TAG, "onClick: " + numOfClicks);
                if (numOfClicks%2 ==0){
                    getSupportActionBar().show();
                    Log.d(TAG, "onClick: show");
                }else {
                    getSupportActionBar().hide();
                    Log.d(TAG, "onClick: hide");
                }

            }
        });

        displayImageNews.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // display bottom sheet
                showBottomSheet();
                return true;
            }
        });
    }


    public void showBottomSheet(){
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.display_image_dialog);
        saveLayout = bottomSheetDialog.findViewById(R.id.saveLayout);
        shareLayout = bottomSheetDialog.findViewById(R.id.shareLayout);
        cancelLayout = bottomSheetDialog.findViewById(R.id.cancelLayout);
        bottomSheetDialog.show();

        saveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save image to phone
                file_download(imgUrl);
                 bottomSheetDialog.dismiss();
            }
        });

        shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // share image
                shareImage();
                bottomSheetDialog.dismiss();
            }
        });

        cancelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
    }

    public void file_download(String uRl) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/whatsNews");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .setDescription("Something useful. No, rally.")
                .setDestinationInExternalPublicDir("/whatsNews", String.valueOf(Math.random()*50+1)+".jpg");

        mgr.enqueue(request);
        Toast.makeText(this, "Image Saved", Toast.LENGTH_SHORT).show();
    }

    public void shareImage(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Image");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, imgUrl);
        startActivity(Intent.createChooser(sharingIntent, "Share Image using "));

    }
}
