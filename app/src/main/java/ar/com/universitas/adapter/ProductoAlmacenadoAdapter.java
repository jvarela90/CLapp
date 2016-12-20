package ar.com.universitas.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import java.io.StringBufferInputStream;

import ar.com.universitas.clapp.R;
import ar.com.universitas.model.ProductModel;

/**
 * Created by Chino on 10/5/2016.
 */

public class ProductoAlmacenadoAdapter extends ArrayAdapter<ProductModel>{

    private ProductModel[] modelItems = null;
    private Context contxt;

    public ProductoAlmacenadoAdapter(Context context, ProductModel[] resource) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) contxt).getLayoutInflater();
        convertView = inflater.inflate(R.layout.listview_mialmacen, parent, false);

        TextView id = (TextView) convertView.findViewById(R.id.idProducto);
        TextView name = (TextView) convertView.findViewById(R.id.nombre);
        TextView cb = (TextView) convertView.findViewById(R.id.box);
        EditText cantidad = (EditText) convertView.findViewById(R.id.cantidad);
        TextView operacion = (TextView) convertView.findViewById(R.id.operationType);

        try {
            name.setText(getModelItems()[position].getNombre());
            String idProduct = String.valueOf(getModelItems()[position].getIdProduct());
            id.setText(idProduct);
            cb.setText(String.valueOf(getModelItems()[position].getCheckBoxValue()));
            operacion.setText(getModelItems()[position].getOperation());

            cantidad.setText(String.valueOf(getModelItems()[position].getCantidad()));
            cantidad.setTag(position);
            cantidad.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.i("onTextChanged", s.toString());
                    if ((s != null)&&(!s.toString().equals(""))){
                        getModelItems()[position].setCantidad(Integer.valueOf(s.toString()));
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    Log.d("beforeTextChanged: ", "");
                }
                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                }
            });


        }catch (Exception e){
            // Logea la exception..
            Log.i("tracker2", "PrductoAdapter.getView Exception" + e.getMessage());
        }
        return convertView;
    }
}
