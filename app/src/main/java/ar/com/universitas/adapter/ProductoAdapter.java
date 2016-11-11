package ar.com.universitas.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import ar.com.universitas.clapp.R;
import ar.com.universitas.model.ProductModel;

/**
 * Created by Chino on 10/5/2016.
 */

public class ProductoAdapter extends ArrayAdapter<ProductModel>{

    private ProductModel[] modelItems = null;
    private Context contxt;

    public ProductoAdapter(Context context, ProductModel[] resource) {
        super(context, R.layout.single_post,resource);
        this.contxt=context;
        this.setModelItems(resource);
    }

    public ProductModel[] getModelItems() {
        return modelItems;
    }
    public void setModelItems(ProductModel[] modelItems) {
        this.modelItems = modelItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) contxt).getLayoutInflater();
        convertView = inflater.inflate(R.layout.single_post, parent, false);

        TextView id = (TextView) convertView.findViewById(R.id.single_post_tv_id);
        TextView name = (TextView) convertView.findViewById(R.id.single_post_tv_nombre);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);

        try {
            name.setText(getModelItems()[position].getNombre());
            //TODO : Ojo con este valor si se parsea y viene null.
            String idProduct = String.valueOf(getModelItems()[position].getIdProduct());
            id.setText(idProduct);

            //le agrego un listener para q escuche cuando checkeo un checkbox.
            cb.setOnCheckedChangeListener(myCheckChangList);
            cb.setTag(position);
            cb.setChecked(getModelItems()[position].getCheckBoxValue());

        }catch (Exception e){
            // Logea la exception..
            Log.i("tracker2", "PrductoAdapter.getView Exception" + e.getMessage());
        }
        return convertView;
    }

    //Agrego un listener para checar los checkbox
    OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
                //logeo la entrada
                Log.i("OnCheckedChangeListener", "OnCheckedChangeListener.onCheckedChanged" + "buttonView");
                Log.i("OnCheckedChangeListener", (buttonView== null)?"Null":buttonView.toString());
                Log.i("OnCheckedChangeListener", "OnCheckedChangeListener.onCheckedChanged" + "isChecked:"+ isChecked);

                if (buttonView.getTag()!= null)
                        getModelItems()[(Integer) buttonView.getTag()].setCheckBoxValue(isChecked);
                }
    };
}
