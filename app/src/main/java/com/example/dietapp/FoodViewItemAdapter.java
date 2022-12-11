package com.example.dietapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class FoodViewItemAdapter extends BaseAdapter {
    private  Context context;
    private ArrayList<FoodViewItem> array = new ArrayList<>();

    public FoodViewItemAdapter(ArrayList<FoodViewItem> array, Context context){
        this.context = context;
        this.array = array;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_meal, parent, false);

        TextView name = view.findViewById(R.id.foodname);
        name.setText(array.get(position).getName());

        TextView cal = view.findViewById(R.id.showcal);
        cal.setText(array.get(position).getCal()+" kcal");

        TextView car = view.findViewById(R.id.showcar);
        car.setText(array.get(position).getCar()+" g");

        TextView pro = view.findViewById(R.id.showpr);
        pro.setText(array.get(position).getPro()+" g");

        TextView fat = view.findViewById(R.id.showfat);
        fat.setText(array.get(position).getFat()+" g");

        TextView review = view.findViewById(R.id.listFoodReview);
        String re = array.get(position).getReview();
        if(re == null){
            review.setVisibility(View.INVISIBLE);
        }
        else{
            review.setText(re);
        }

        TextView time = view.findViewById(R.id.listFoodTime);
        String t = array.get(position).getTime();
        if(t==null){
            time.setVisibility(View.INVISIBLE);
        }
        else{
            time.setText(t);
        }

        TextView place = view.findViewById(R.id.listFoodPlace);
        String p = array.get(position).getPlace();
        if(p != null && !p.contains("null")){
            String[] pl = p.split("\\(");
            place.setText(pl[0]);
        }
        else{
            place.setVisibility(View.INVISIBLE);
        }

        ImageView image = view.findViewById(R.id.imageView2);
        String imgDir = array.get(position).getImg();
        if(imgDir != null){
            File file = new File(imgDir);
            if(file.exists()){
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                image.setImageBitmap(bitmap);
            }
        }
        else{
            image.setVisibility(View.INVISIBLE);
        }

        return view;
    }
}
