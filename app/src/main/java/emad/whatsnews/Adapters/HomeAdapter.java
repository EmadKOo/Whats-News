package emad.whatsnews.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.comix.rounded.RoundedCornerImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import emad.whatsnews.Model.Article;
import emad.whatsnews.Model.NewsResponse;
import emad.whatsnews.R;
import emad.whatsnews.ViewItem;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{

    ArrayList<Article> articles;
    Context context;

    public HomeAdapter(ArrayList<Article> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_layout, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        Picasso.get().load(articles.get(i).getUrlToImage()).into(holder.imageItem);
        holder.itemTitle.setText(articles.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RoundedCornerImageView imageItem;
        TextView itemTitle;
        Intent intent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageItem = itemView.findViewById(R.id.imageItem);
            itemTitle = itemView.findViewById(R.id.itemTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(context, ViewItem.class);
                    intent.putExtra("article", articles.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
