package com.example.bloodgenix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnBoardingScreen extends AppCompatActivity {

    //Defining Layouts
    ViewPager sliderView;
    LinearLayout dots;
    private TextView[] mDots;
    private Button FinishButton;
    private int mCurrentPage;

    private SliderAdapter sliderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_on_boarding);
        sliderView =(ViewPager) findViewById(R.id.sliderView);
        dots = (LinearLayout) findViewById(R.id.dots);
        FinishButton =findViewById(R.id.FinishButton);

        sliderAdapter = new SliderAdapter(this);
        sliderView.setAdapter(sliderAdapter);
        addDotsIndicator(0);

        sliderView.addOnPageChangeListener(viewListener);
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        dots.removeAllViews();
        for(int i=0; i<mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(37);
            mDots[i].setTextColor(getResources().getColor(R.color.content_2));
            dots.addView(mDots[i]);

        }
        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.black));
        }
    }
    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            mCurrentPage=position;
            if (position == 2){
                dots.setEnabled(false);
                dots.setVisibility(View.INVISIBLE);
                FinishButton.setEnabled(true);
                FinishButton.setVisibility(View.VISIBLE);

                FinishButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent welcome = new Intent(OnBoardingScreen.this, Welcome.class);
                        startActivity(welcome);
                    }
                });
            }else{
                dots.setEnabled(true);
                dots.setVisibility(View.VISIBLE);
                FinishButton.setEnabled(false);
                FinishButton.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}