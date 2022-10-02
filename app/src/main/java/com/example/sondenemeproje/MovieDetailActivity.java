package com.example.sondenemeproje;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetailActivity extends AppCompatActivity {


   private ImageView imageView;
   private TextView title, plot,language,actor;
   private String mTitle;
   private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        // Adapterden gönderilen title bilgisini aldık.
         Bundle bundle=getIntent().getExtras();
         mTitle=bundle.getString("title");

        imageView=findViewById(R.id.poster_image);
        title=findViewById(R.id.titleView);
        plot=findViewById(R.id.plotView);
        language=findViewById(R.id.languageView);
        actor=findViewById(R.id.actorView);

        progressBar=findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);

        detayGet();
    }

    // detayGet() metodu ile apiye post istediği gönderildi. Gelen cevaba göre ekrana Toast mesajı yazdırıldı.
    // Eğer sonuç true geldiyse ekrana "Found" yazdırıldı. Sonuç False geldiyse ekrana "Not Found" yazdırıldı.
    // Bu metot ile sonuc true ise apiden gelen verileri ekrana bastırır.

    private void detayGet() {

        String url= "https://www.omdbapi.com/?t=" + mTitle +  "&plot=full&apikey=6d623c85";

        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest request= new StringRequest(Request.Method.GET, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject movie=new JSONObject(response);
                            String result = movie.getString("Response");
                            if(result.equals("True")) {

                                Toast.makeText(MovieDetailActivity.this, "Found", Toast.LENGTH_SHORT).show();
                                String title1=movie.getString("Title");
                                title.setText(title1);
                                String plot1=movie.getString("Plot");
                                plot.setText("Plot: " + plot1);
                                String lang1=movie.getString("Language");
                                language.setText("Laguage: "+lang1);
                                String actor1=movie.getString("Actors");
                                actor.setText("Actors: "+actor1);
                                String posterUrl=movie.getString("Poster");
                                if(posterUrl.equals("N/A")) {
                                    Toast.makeText(MovieDetailActivity.this, "Hatalı Resim Yüklenemedi!!!", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.INVISIBLE);
                                }else {

                                    Picasso.get().load(posterUrl).into(imageView);
                                    progressBar.setVisibility(View.INVISIBLE);
                                }
                            }else {
                                Toast.makeText(MovieDetailActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        );

        queue.add(request);

    }

    }
