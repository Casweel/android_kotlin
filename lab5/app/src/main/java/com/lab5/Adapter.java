package com.lab5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    private ArrayList<Feed> medias;
    LayoutInflater lInflater;
    private Context context;
    private static ArrayDeque<String> starred = new ArrayDeque<String>(10);

    Adapter(ArrayList<Feed> medias, Context context) {
        this.medias = medias;
        this.context = context;
        lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return medias.size();
    }

    @Override
    public Object getItem(int position) {
        return medias.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.post, parent, false);
        }
        Feed feed = getFeed(position);

        ImageView img = view.findViewById(R.id.loadMedia);
        ImageView like = view.findViewById(R.id.likeImg);
        ImageView dislike = view.findViewById(R.id.dislikeImg);

        if (feed.getLike() == 0) {
            Glide.with(context)
                    .load(R.drawable.emptylike)
                    .into(like);
            Glide.with(context)
                    .load(R.drawable.emptydislike)
                    .into(dislike);
        } else if (feed.getLike() == 1) {
            Glide.with(context)
                    .load(R.drawable.like)
                    .into(like);
            Glide.with(context)
                    .load(R.drawable.emptydislike)
                    .into(dislike);
        } else {
            Glide.with(context)
                    .load(R.drawable.emptylike)
                    .into(like);
            Glide.with(context)
                    .load(R.drawable.dislike)
                    .into(dislike);
        }

        Glide.with(context)
                .load(feed.getMedia())
                .into(img);

        like.setClickable(true);
        dislike.setClickable(true);

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feed.getLike() == 0) {
                    feed.setLike(1);

                    if (starred.size() < 10)
                        addStarred(feed);
                    else
                        addRemoveStarred(feed);

                    Glide.with(context)
                            .load(R.drawable.like)
                            .into(like);
                } else if (feed.getLike() == 1) {
                    feed.setLike(0);

                    removeStarred(feed);

                    Glide.with(context)
                            .load(R.drawable.emptylike)
                            .into(like);
                } else {
                    feed.setLike(1);

                    if (starred.size() < 10)
                        addStarred(feed);
                    else
                        addRemoveStarred(feed);

                    Glide.with(context)
                            .load(R.drawable.like)
                            .into(like);
                    Glide.with(context)
                            .load(R.drawable.emptydislike)
                            .into(dislike);
                }
            }
        });
        dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (feed.getLike() == 0) {
                    feed.setLike(-1);
                    Glide.with(context)
                            .load(R.drawable.dislike)
                            .into(dislike);
                } else if (feed.getLike() == 1) {
                    feed.setLike(-1);

                    removeStarred(feed);

                    Glide.with(context)
                            .load(R.drawable.dislike)
                            .into(dislike);
                    Glide.with(context)
                            .load(R.drawable.emptylike)
                            .into(like);
                } else {
                    feed.setLike(0);
                    Glide.with(context)
                            .load(R.drawable.emptydislike)
                            .into(dislike);
                }
            }
        });
        return view;
    }

    public Feed getFeed(int position) {
        return ((Feed) getItem(position));
    }

    public static ArrayDeque<String> getStarred() {
        return starred;
    }

    private void addStarred(Feed feed) {
        starred.addLast(feed.getMedia());
    }

    private void addRemoveStarred(Feed feed) {
        starred.pollFirst();
        addStarred(feed);
    }

    private void removeStarred(Feed feed) {
        starred.removeFirstOccurrence(feed.getMedia());
    }
}