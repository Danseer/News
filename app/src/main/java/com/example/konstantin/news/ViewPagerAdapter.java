package com.example.konstantin.news;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Konstantin on 07.10.2017.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<SliderUtils> sliderImgUrl;

    public ViewPagerAdapter(Context context,List<SliderUtils> sliderImgUrl) {
        this.context = context;
        this.sliderImgUrl=sliderImgUrl;
    }

    @Override
    public int getCount() {
        return sliderImgUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.custom_layout,null);

        SliderUtils utils=sliderImgUrl.get(position);


        ImageView imageView=view.findViewById(R.id.ivCustomLayout);
        Picasso.with(context).load(utils.getSliderImageUrl()).into(imageView);
        ViewPager viewPager=(ViewPager) container;
        viewPager.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager=(ViewPager)container;
        View view=(View)object;
        viewPager.removeView(view);
    }
}
