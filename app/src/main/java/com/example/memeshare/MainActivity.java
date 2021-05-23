package com.example.memeshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Intent.ACTION_SEND;
import static android.view.View.*;

public class MainActivity extends AppCompatActivity {


    String currentImageUrl = null;
    private Object JsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadMeme();
    }

    private void loadMeme(){
        // Instantiate the RequestQueue.
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(VISIBLE);

        //RequestQueue queue = Volley.newRequestQueue(this);
        MySingleton queue = MySingleton.getInstance(this.getApplicationContext());
        String url = "https://meme-api.herokuapp.com/gimme";

        // Request a string response from the provided URL.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String url=null;
                        try {
                            currentImageUrl = (String) response.getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ImageView imageView = findViewById(R.id.memeImageView);
                        Glide.with(imageView).load(currentImageUrl).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(imageView);


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //

                    }
                });

        // Add the request to the RequestQueue.
        //queue.add(jsonObjectRequest);
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void nextMeme(View view) {
        loadMeme();
    }

    public void shareMeme(View view) {
        Intent intent = new Intent(ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey,Checkout this cool meme I got from Reddit"+currentImageUrl);

        Intent shareIntent = Intent.createChooser(intent, "Share this meme using...");
        startActivity(shareIntent);


    }
}