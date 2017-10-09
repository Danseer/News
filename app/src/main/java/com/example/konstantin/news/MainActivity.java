package com.example.konstantin.news;

import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private RecyclerView StoriesRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();

    private List<SliderUtils> imgUrl = new ArrayList<>();

    private ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;

    LinearLayout sliderDotsPanel;
    private int dotsCount;
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(mActionBarToolbar);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        sliderDotsPanel = (LinearLayout) findViewById(R.id.sliderDots);


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    private class StoriesHolder extends RecyclerView.ViewHolder {
        private ImageView imageItemView;
        private TextView title;
        private TextView sourse;
        private TextView time;

        public StoriesHolder(View itemView) {
            super(itemView);
            imageItemView = (ImageView) itemView.findViewById(R.id.ivCover);
            title = (TextView) itemView.findViewById(R.id.tvCustomLayoutTitle);
            sourse = (TextView) itemView.findViewById(R.id.tvCustomLayoutSource);
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
            imgUrl = Fetcher.getSlImg();
            setUpAdapter();
            setUpSliderAdapter();

            dotsCount = viewPagerAdapter.getCount();
            dots = new ImageView[dotsCount];
            for (int i = 0; i < dotsCount; i++) {
                dots[i] = new ImageView(MainActivity.this);
                dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(8, 0, 8, 0);
                sliderDotsPanel.addView(dots[i], params);
            }
            dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active));

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new myTimerClass(), 2000, 4000);
        }
    }

    private void setUpAdapter() {
        StoriesRecyclerView.setAdapter(new StoriesAdapter(mItems));
    }

    private void setUpSliderAdapter() {
        viewPagerAdapter = new ViewPagerAdapter(MainActivity.this, imgUrl);
        viewPager.setAdapter(viewPagerAdapter);
    }


    public class myTimerClass extends TimerTask {

        @Override
        public void run() {

            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int current = viewPager.getCurrentItem();

                    if (current == imgUrl.size() - 1) viewPager.setCurrentItem(0);
                    else viewPager.setCurrentItem(current + 1);
                }
            });
        }
    }
}
