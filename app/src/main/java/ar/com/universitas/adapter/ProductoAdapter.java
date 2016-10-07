package ar.com.universitas.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import ar.com.universitas.clapp.R;
import ar.com.universitas.model.ProductModel;
import android.app.Activity;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by Chino on 10/5/2016.
 */

public class ProductoAdapter extends ArrayAdapter<ProductModel>{

    private ProductModel[] modelItems = null;
    private Context context;

    public ProductoAdapter(Context context, ProductModel[] resource) {
        super(context, R.layout.single_post,resource);
        this.setContext(context);
        this.setModelItems(resource);
    }

    public ProductModel[] getModelItems() {
        return modelItems;
    }

    public void setModelItems(ProductModel[] modelItems) {
        this.modelItems = modelItems;
    }
    @NonNull
    @Override
    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.single_post, parent, false);

        TextView id = (TextView) convertView.findViewById(R.id.single_post_tv_id);
        TextView name = (TextView) convertView.findViewById(R.id.single_post_tv_nombre);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);

        try {
            name.setText(getModelItems()[position].getNombre());
            //TODO : Ojo con este valor si se parsea y viene null.
            String idProduct = String.valueOf(getModelItems()[position].getIdProduct());
            id.setText(idProduct);

            if(getModelItems()[position].getCheckBoxValue() == 1){
                cb.setChecked(true);
            }else {
                cb.setChecked(false);
            }

        }catch (Exception e){
            // Logea la exception..
            Log.i("tracker2", "PrductoAdapter.getView Exception" + e.getMessage());
        }
        return convertView;
    }
}
