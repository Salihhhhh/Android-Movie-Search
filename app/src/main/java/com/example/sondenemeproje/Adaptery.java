package com.example.sondenemeproje;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Adaptery extends RecyclerView.Adapter<Adaptery.MyViewHolder> {


    private Context context;
    private List<MovieModelClass> mData;

    public Adaptery(Context context, List<MovieModelClass> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v;
        LayoutInflater inflater=LayoutInflater.from(context);
        v=inflater.inflate(R.layout.movie_item, parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.title.setText(mData.get(position).getTitle());
        holder.year.setText("Year: "+mData.get(position).getYear());
        holder.type.setText("Type: "+mData.get(position).getType());

        // Glide ile resim gösterimi
        Glide.with(context)
                .load(mData.get(position).getImg())
                .into(holder.img);


        ///Tıklanma olduğunda detay sayfasına gidecek.
        // İstenilen film tıklandığında ilgili pozisyondaki filmin "Title" kısmı MovieDetailActivity
        // sayfasına gönderiliyor.

        // Bütün veirleri gönderip detay sayfasında kullanabiliriz fakat ben detay sayfası için
        // farklı bir url kullanacağım için detay sayfasına sadece filmin başlığı gönderdim.


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context,MovieDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("title", mData.get(position).getTitle());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView year;
        TextView type;
        ImageView img;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            title=itemView.findViewById(R.id.titleView);
            type=itemView.findViewById(R.id.typeView);
            year=itemView.findViewById(R.id.yearView);
            img=itemView.findViewById(R.id.imageView);
        }
    }


}
