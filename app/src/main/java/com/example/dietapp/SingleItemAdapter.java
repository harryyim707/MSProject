package com.example.dietapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SingleItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SingleItem> array_singleItem = new ArrayList<>();

    public SingleItemAdapter(ArrayList<SingleItem> array_singleItem, Context context){
        this.context=context;
        this.array_singleItem=array_singleItem;
    }
    @Override
    public int getCount() {
        return array_singleItem.size();
    }

    @Override
    public Object getItem(int position) {
        return array_singleItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_item, parent, false);

        TextView name = view.findViewById(R.id.listFoodName);
        name.setText(array_singleItem.get(position).getName());

        TextView brand = view.findViewById(R.id.listFoodBrand);
        brand.setText(array_singleItem.get(position).getBrand());

        TextView weight = view.findViewById(R.id.listFoodWeight);
        weight.setText(array_singleItem.get(position).getWeight().toString());

        TextView standard = view.findViewById(R.id.listFoodStandard);
        standard.setText(array_singleItem.get(position).getStandard().toString());

        TextView cal = view.findViewById(R.id.showcalval);
        cal.setText(array_singleItem.get(position).getCalVal().toString());

        TextView car = view.findViewById(R.id.showcarval);
        car.setText(array_singleItem.get(position).getCarVal().toString());

        TextView pro = view.findViewById(R.id.showproval);
        pro.setText(array_singleItem.get(position).getProVal().toString());

        TextView fat = view.findViewById(R.id.showfatval);
        fat.setText(array_singleItem.get(position).getFatVal().toString());

        return view;
    }
}
