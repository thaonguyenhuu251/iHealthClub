package com.htnguyen.ihealthclub.view.mainscreen.storyviewext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.htnguyen.ihealthclub.R;
import com.htnguyen.ihealthclub.model.EmojiHome;
import com.htnguyen.ihealthclub.model.ObjectStory;
import com.htnguyen.ihealthclub.view.adapter.EmojiGeneralAdapter;


import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter{

    private ArrayList<ObjectStory> images;
    private ArrayList<EmojiHome> listEmoji ;
    private RecyclerView rvEmoji;
    private EmojiGeneralAdapter rvEmojiAdapter ;
    private Context context;
    private StoryCallbacks storyCallbacks;
    private boolean storiesStarted = false;

    public ViewPagerAdapter(ArrayList<ObjectStory> images, Context context, StoryCallbacks storyCallbacks) {
        this.images = images;
        this.context = context;
        this.storyCallbacks = storyCallbacks;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, final int position) {

        LayoutInflater inflater = LayoutInflater.from(context);

        ObjectStory currentStory = images.get(position);

        final View view = inflater.inflate(R.layout.layout_story_item, collection, false);

        final ImageView mImageView = view.findViewById(R.id.mImageView);

        final RecyclerView rvEmoji = view.findViewById(R.id.rv_comment_status);

        listEmoji = new ArrayList<>();
        listEmoji();
        rvEmojiAdapter = new EmojiGeneralAdapter(this.context, listEmoji,emojiHome -> {
            return null;
        });
        rvEmoji.setAdapter(rvEmojiAdapter);
        rvEmoji.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false));

//  descriptionTextView Button ImageView Bottom
//        if (!TextUtils.isEmpty(currentStory.getCreateBy())) {
//            TextView textView = view.findViewById(R.id.descriptionTextView);
//            textView.setVisibility(View.VISIBLE);
//            textView.setText(currentStory.getCreateBy());
//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    storyCallbacks.onDescriptionClickListener(position);
//                }
//            });
//        }

        Glide.with(context)
                .load(currentStory.getListFile().get(0).getUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        storyCallbacks.nextStory();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource != null) {
                            PaletteExtraction pe = new PaletteExtraction(view.findViewById(R.id.relativeLayout),
                                    ((BitmapDrawable) resource).getBitmap());
                            pe.execute();
                        }
                        if (!storiesStarted) {
                            storiesStarted = true;
                            storyCallbacks.startStories();
                        }
                        return false;
                    }
                })
                .into(mImageView);

        collection.addView(view);

        return view;
    }

    private void listEmoji(){
        listEmoji.add(new EmojiHome("1","1"));
        listEmoji.add(new EmojiHome("","\uD83D\uDC4D"));
        listEmoji.add(new EmojiHome("","\uD83D\uDC95"));
        listEmoji.add(new EmojiHome("","\uD83D\uDE02"));
        listEmoji.add(new EmojiHome("","\uD83D\uDE0A"));
        listEmoji.add(new EmojiHome("","\uD83D\uDE2E"));
        listEmoji.add(new EmojiHome("","\uD83D\uDE25"));
        listEmoji.add(new EmojiHome("","\uD83D\uDE21"));
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        (container).removeView((View) object);
    }
}
