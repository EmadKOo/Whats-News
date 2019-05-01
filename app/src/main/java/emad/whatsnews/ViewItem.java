package emad.whatsnews;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import emad.whatsnews.Model.Article;

public class ViewItem extends AppCompatActivity {

    Article receivedArticle;
    ImageView mImageNews;
    ImageView mBack;
    TextView viewTitle;
    TextView viewDescription;
    Intent displayImageIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        receivedArticle = (Article) getIntent().getSerializableExtra("article");
        setupViews();
    }

    public void setupViews(){
        mImageNews = findViewById(R.id.mImageNews);
        viewTitle = findViewById(R.id.viewTitle);
        mBack = findViewById(R.id.mBack);
        viewDescription = findViewById(R.id.viewDescription);

        viewTitle.setText(receivedArticle.getTitle());
        viewDescription.setText(receivedArticle.getDescription());
        Picasso.get().load(receivedArticle.getUrlToImage()).into(mImageNews);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

        mImageNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayImageIntent = new Intent(getApplicationContext(), DisplayImageActivity.class);
                displayImageIntent.putExtra("imgUrl", receivedArticle.getUrlToImage());
                startActivity(displayImageIntent);
            }
        });

    }
}
