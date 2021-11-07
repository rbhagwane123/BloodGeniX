package com.example.bloodgenix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ImageView sliderImage;
    TextView sliderTitle, sliderDesc;


    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int [] slider_images={
      R.drawable.ic_drawer_1, R.drawable.ic_drawer_2, R.drawable.ic_drawer_3
    };

    public String [] title={
            "Donate Blood",
            "Search Blood Donor",
            "Explore update near you"
    };

    public int[] desc={R.string.blood_donation,
            R.string.Search_blood_donor,
            R.string.Explore_update
    };

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public boolean isViewFromObject( View view,  Object object) {
        return view == (ConstraintLayout)object;
    }

    @Override
    public Object instantiateItem( ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout_dashboard, container,false);

        sliderImage = (ImageView) view.findViewById(R.id.sliderImage);
        sliderTitle = (TextView) view.findViewById(R.id.sliderTitle);
        sliderDesc = (TextView) view.findViewById(R.id.sliderDesc);

        sliderImage.setImageResource(slider_images[position]);
        sliderTitle.setText(title[position]);
        sliderDesc.setText(desc[position]);
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
