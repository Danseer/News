package com.example.konstantin.news;

import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView StoriesRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager=(ViewPager)findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.stories);
        tabSpec.setIndicator("stories");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.video);
        tabSpec.setIndicator("video");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.favourites);
        tabSpec.setIndicator("favourites");
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);

        StoriesRecyclerView = (RecyclerView) findViewById(R.id.rvStories);
        StoriesRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        setUpAdapter();

        new FetchItemTask().execute();
    }

    private class StoriesHolder extends RecyclerView.ViewHolder {
        private ImageView imageItemView;
        private TextView title;
        private TextView sourse;
        private TextView time;

        public StoriesHolder(View itemView) {
            super(itemView);
            imageItemView = (ImageView) itemView.findViewById(R.id.ivCover);
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            sourse = (TextView) itemView.findViewById(R.id.tvSource);
            time = (TextView) itemView.findViewById(R.id.tvTime);

        }
    }

    private class StoriesAdapter extends RecyclerView.Adapter<StoriesHolder> {

        private List<GalleryItem> mGalleryItems;

        public StoriesAdapter(List<GalleryItem> items) {
            mGalleryItems = items;
        }

        @Override
        public StoriesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View v = inflater.inflate(R.layout.gallery_item, parent, false);
            return new StoriesHolder(v);
        }

        @Override
        public void onBindViewHolder(StoriesHolder holder, final int position) {
            GalleryItem GalleryItem = mGalleryItems.get(position);
            Picasso.with(MainActivity.this).load(GalleryItem.getUrl()).into(holder.imageItemView);

            holder.title.setText(mGalleryItems.get(position).getTitle());
            holder.sourse.setText(mGalleryItems.get(position).getSource());
            holder.time.setText(mGalleryItems.get(position).getTime());

        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }

    private class FetchItemTask extends AsyncTask<Void, Void, List<GalleryItem>> {


        @Override
        protected List<GalleryItem> doInBackground(Void... voids) {
            return new Fetcher().fetchItems();
        }

        @Override
        protected void onPostExecute(List<GalleryItem> gallery_items) {
            mItems = gallery_items;
            setUpAdapter();
        }
    }

    private void setUpAdapter() {
        StoriesRecyclerView.setAdapter(new StoriesAdapter(mItems));
    }
}
