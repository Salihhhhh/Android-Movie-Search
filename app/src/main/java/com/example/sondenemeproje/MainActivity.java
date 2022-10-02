package com.example.sondenemeproje;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String JSON_URL="";
    private List<MovieModelClass> movieList;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        internetCheck();  // Uygulamayı başlattığımızda internetin olup olmadığını kontrol ediyor.
        // Eğer internete bağlı değilse uygulamayı kapatıyoruz..

        movieList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        searchView=findViewById(R.id.searchView);


        //Arama çubuğunda veri olup olmadığı dinleniyor. Veri var ise girilen veri
        // search(String gelen) metoduna parametre olarak gönderiliyor.

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                    search(query.toString());

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        }

    // Aramak istenilen film girildikten sonra arama ENTER tuşuna
    // tıklanınca search() metodu çalısacak .
    //Aracanak film başlığı ilgili arana girilince JSON_URL kısımındaki başlık ismi
    // doludurularak ilgili film sonucu apiden çekilecek.

    public void search(String gelen) {
        internetCheck(); // İnternete bağlı olup olmadığını tekrar kontrol ediyorum.

        movieList.clear();  // Yeni filmi aramadan önce var olan diziyi temizliyorum.
        String mName = gelen;
        progressBar.setVisibility(View.VISIBLE);
        JSON_URL = "https://www.omdbapi.com/?s=" + mName + "&plot=full&apikey=6d623c85";
        GetData getData = new GetData();
        getData.execute();

    }
    private void internetCheck(){
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =cm.getActiveNetworkInfo();
        if(networkInfo != null && ((NetworkInfo) networkInfo).isConnectedOrConnecting()){
        }
        else
        {
            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Hata");
            builder.setMessage("Lütfen internet bağlantınızı kontrol ediniz!");
            builder.setCancelable(true);
            builder.setPositiveButton("Çıkış", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    finish(); //Uygulamayı sonlandırıyoruz.
                   // progressBar.setVisibility(View.INVISIBLE);

                }
            });
            android.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }



    // İlgili URL'ye doInBackGround() metoduyla post isteği gönderiliyor.. Gelen post isteği sonucu return ile döndürülüyor.
    // Döndürülen post sonucu onPostExecute() metodu ile json veri tipine dönüştürülüyor ve array ile ekleniyor.
    // Array dizisi verilerini ayrıştırarak MovieModelClass sınıfına nesne oluşturulup Set() metoduyla gönderiliyor.


    public class GetData extends AsyncTask<String ,String,String>{

        @Override
        protected String doInBackground(String... strings) {

            String current="";

            try {
                URL url;
                HttpURLConnection urlConnection=null;

                try {
                    url=new URL(JSON_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream is=urlConnection.getInputStream();
                    InputStreamReader isr=new InputStreamReader(is);

                    int data=isr.read();
                    while(data!=-1) {
                        current+=(char) data;
                        data=isr.read();
                    }

                    return current;


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return current;
        }


        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject jsonObject=new JSONObject(s);
                String result = jsonObject.getString("Response");

            /// Gelen post isteği sonunda json verimizde bulunan "Responce" değerine bakıpyoruz.
            ///  Eğer sonuc True is veriler başarıyla çekilmiştir. False ise ya snuç gelmedi veya aranan film yoktur demektir.

                if(result.equals("True")) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Movie Found", Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Movies Not Found!", Toast.LENGTH_SHORT).show();
                }

                // Gelen json verileri diziye atıyoruz.


                JSONArray jsonArray=jsonObject.getJSONArray("Search");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

            // MovieModelClass sınıfına nesne oluşturulup gelen verileri gönderiyoruz.
            // Apiden gelen json verilerinden gerekli olan başlıkları şeçiyoruz. (Title,Year....)

                        MovieModelClass model = new MovieModelClass();
                        model.setTitle(jsonObject1.getString("Title"));
                        model.setYear((jsonObject1.getString("Year")));
                        model.setImg(jsonObject1.getString("Poster"));
                        model.setType(jsonObject1.getString("Type"));

                        movieList.add(model);
                    }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            PutDataIntoRecyclerView(movieList);
        }
    }

    ///Veriler ayrışmış bir şekilde dizi içerisinde ve bu verileri (movieList)
    // RecyclerView'e gönderiyoruz.

    private void PutDataIntoRecyclerView(List<MovieModelClass> movieList){

        Adaptery adaptery=new Adaptery(this,movieList);
       // recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)); // yatay hizalama için
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Dikey hizalamak için
        recyclerView.setAdapter(adaptery);
    }
}