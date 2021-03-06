package com.blueshak.app.blueshak.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.blueshak.app.blueshak.util.FullScreenImageAdapter;
import com.blueshak.app.blueshak.util.FullScreenImageAdapterNextSplash;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.blueshak.app.blueshak.MainActivity;
import com.blueshak.blueshak.R;
import com.blueshak.app.blueshak.global.GlobalFunctions;
import com.blueshak.app.blueshak.global.GlobalVariables;
import com.blueshak.app.blueshak.root.RootActivity;
import com.blueshak.app.blueshak.view.CustomSliderTextView;

import me.relex.circleindicator.CircleIndicator;

public class NextSplashScreen  extends RootActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{


	SliderLayout mProductSlider;
	PagerIndicator mPagerIndicator;
	Button skipText;
	Context context;
	static Activity activity;
	Button go_to_main_page;
 ViewPager viewPager;
	FullScreenImageAdapterNextSplash myPagerAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		try{
			activity = this;
			context = this;
			setContentView(R.layout.next_splash_screen);
			skipText = (Button) findViewById(R.id.tutorial_skip_text);
			//mProductSlider = (SliderLayout) findViewById(R.id.splash_slider);
			go_to_main_page=(Button)findViewById(R.id.go_to_main_page);
			//mPagerIndicator = (PagerIndicator) findViewById(R.id.splash_custom_indicator);
			//setImageOnView(this);
			go_to_main_page.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					nextScreen();
				}
			});
			skipText.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					nextScreen();
				}
			});
		}catch (Exception e){
			e.printStackTrace();

		}


		viewPager = (ViewPager)findViewById(R.id.pager);

		//viewPager.setCurrentItem(1, false);
		CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
		myPagerAdapter = new FullScreenImageAdapterNextSplash(this);
		//  myPagerAdapter.set
		viewPager.setAdapter(myPagerAdapter);
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int i) {
				if(i==2){
					go_to_main_page.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		indicator.setViewPager(viewPager);

		myPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
	}

	public void setImageOnView(Context context){
		mProductSlider.removeAllSliders();
        int[] displaypictures = {R.drawable.splash_1,R.drawable.splash_2, R.drawable.splash_3};
        for(int i=0;i<displaypictures.length;i++){
            CustomSliderTextView textSliderView = new CustomSliderTextView(context);
            // initialize a SliderLayout
            textSliderView
                    .description(1 + "")
                    .image(displaypictures[i])
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);
            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", 1 + "");

            mProductSlider.addSlider(textSliderView);
        }

        mProductSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOutSlide);
        mProductSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mProductSlider.stopAutoCycle();
        if(mPagerIndicator!=null){mProductSlider.setCustomIndicator(mPagerIndicator);mProductSlider.setCurrentPosition(0);}
        mProductSlider.setCustomAnimation(new DescriptionAnimation());
        mProductSlider.setDuration(4000);
        mProductSlider.addOnPageChangeListener(this);
    }


    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*mProductSlider.startAutoCycle();*/

	}



	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
       // mProductSlider.stopAutoCycle();
		super.onStop();
	}

	
	private void nextScreen(){
		GlobalFunctions.setSharedPreferenceInt(context,GlobalVariables.SHARED_PREFERENCE_FIRST_TIME,1);
		Intent i = new Intent(NextSplashScreen.this,MainActivity.class);
		startActivity(i);
		closeThisActivity();
	}

	@Override
	public void onPageScrolled(int i, float v, int i1) {
	}

	@Override
	public void onPageSelected(int i) {
		if(i==2){
			go_to_main_page.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onPageScrollStateChanged(int i)
	{
	}

	@Override
	public void onSliderClick(BaseSliderView baseSliderView) {

	}

	public static void closeThisActivity(){
		if(activity!=null){activity.finish();}
	}
}
