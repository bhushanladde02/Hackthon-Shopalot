package com.shopalot.main;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ViewPagerAdapter extends PagerAdapter {
	// Declare Variables
	Context context;
	String[] rank;
	int[] color;
	LayoutInflater inflater;
	int[] flag;

	public ViewPagerAdapter(Context context, String[] rank, int[] flag, int[] color) {
		this.context = context;
		this.rank = rank;
		this.flag = flag;
		this.color=color;
	}

	@Override
	public int getCount() {
		return 8;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		// Declare Variables
		ImageView imgflag;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.viewpager_item, container,
				false);

		// Locate the TextViews in viewpager_item.xml
/*		txtrank = (TextView) itemView.findViewById(R.id.rank);
		txtcountry = (TextView) itemView.findViewById(R.id.country);
		txtpopulation = (TextView) itemView.findViewById(R.id.population);
*/		RelativeLayout parentLayout=(RelativeLayout)itemView.findViewById(R.id.layout_bg);
		parentLayout.setBackgroundColor(color[position]);

		// Capture position and set to the TextViews
/*		txtrank.setText(rank[position]);
		txtcountry.setText(country[position]);
		txtpopulation.setText(population[position]);
*/
		// Locate the ImageView in viewpager_item.xml
		imgflag = (ImageView) itemView.findViewById(R.id.flag);
		// Capture position and set to the ImageView
		imgflag.setImageResource(flag[position]);
		imgflag.setBackgroundColor(color[position]);
		

		// Add viewpager_item.xml to ViewPager
		((ViewPager) container).addView(itemView);

		return itemView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Remove viewpager_item.xml from ViewPager
		((ViewPager) container).removeView((RelativeLayout) object);

	}
}
