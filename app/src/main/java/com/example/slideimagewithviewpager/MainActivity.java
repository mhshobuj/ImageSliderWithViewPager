package com.example.slideimagewithviewpager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;

import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    ArrayList<String> images = new ArrayList<>();
    ViewPagerAdapter adapter;
    WormDotsIndicator wormDotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        wormDotsIndicator = (WormDotsIndicator) findViewById(R.id.worm_dots_indicator);

        images.add("https://images.pexels.com/photos/6898857/pexels-photo-6898857.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        images.add("https://images.pexels.com/photos/6897769/pexels-photo-6897769.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        images.add("https://images.pexels.com/photos/6898855/pexels-photo-6898855.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        images.add("https://images.pexels.com/photos/6897682/pexels-photo-6897682.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        images.add("https://images.pexels.com/photos/7258495/pexels-photo-7258495.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");
        images.add("https://images.pexels.com/photos/5358943/pexels-photo-5358943.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1");

        adapter = new ViewPagerAdapter(this, images);
        viewPager.setPadding(100, 0, 100, 0);
        viewPager.setAdapter(adapter);

        wormDotsIndicator.attachTo(viewPager);

    }
}