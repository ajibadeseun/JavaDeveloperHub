package com.example.android.developerhub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extra = getIntent().getExtras();
        String userName = extra.getString("userName");
        String imageString = extra.getString("imageString");
        final String userLinkString = extra.getString("profileUri");

        Log.v("ProfileActivty", "Link is "+userLinkString);

        TextView username = (TextView) findViewById(R.id.username);
        username.setText(userName);

        TextView link = (TextView) findViewById(R.id.link);
        link.setText(userLinkString);

        CircleImageView imageView = (CircleImageView) findViewById(R.id.profile_image);
        Glide.with(this).load(imageString).into(imageView);
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri imageUri = Uri.parse(userLinkString);

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, imageUri);
                startActivity(websiteIntent);
            }
        });

        ImageView shareBtn = (ImageView) findViewById(R.id.share_btn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri shareUri = Uri.parse(userLinkString);

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, shareUri);
                startActivity(websiteIntent);
            }
        });

    }
}
