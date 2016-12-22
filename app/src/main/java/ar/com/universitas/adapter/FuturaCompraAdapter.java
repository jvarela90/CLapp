package ar.com.universitas.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ar.com.universitas.clapp.FuturaCompra_ListNegocios;
import ar.com.universitas.clapp.R;
import ar.com.universitas.model.ProductModel;

/**
 * Created by Chino on 10/5/2016.
 */

public class FuturaCompraAdapter extends ArrayAdapter<ProductModel>{

    private ProductModel[] modelItems = null;
    private Context contxt;

    public FuturaCompraAdapter(Context context, ProductModel[] resource) {
        super(context, R.layout.listview_futuracompra,resource);
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
        convertView = inflater.inflate(R.layout.listview_futuracompra, parent, false);

        TextView quantity = (TextView) convertView.findViewById(R.id.productMissingQTY);
        EditText idProduct = (EditText) convertView.findViewById(R.id.productMissingNameID);
        Button buttonName = (Button) convertView.findViewById(R.id.productMissingName);


        try {
            quantity.setText(String.valueOf(getModelItems()[position].getCantidad()));
            String idProducto = String.valueOf(getModelItems()[position].getIdProduct());
            idProduct.setText(idProducto);
            buttonName.setText(getModelItems()[position].getNombre());
            buttonName.setTag(position);
            buttonName.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i("Edit Button Clicked", "**********");
                    int id = getModelItems()[position].getIdProduct();
                    Toast.makeText(contxt, "Edit button Clicked - ID: "+id,
                            Toast.LENGTH_LONG).show();

                    //Shareo el ID
                    // save user data
                    SharedPreferences sp1 = getContext().getSharedPreferences("idproducto",contxt.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp1.edit();
                    editor.putString("idprod", String.valueOf(id));
                    editor.commit();
                    ((Activity) contxt).finish();

                    Intent i = new Intent(contxt, FuturaCompra_ListNegocios.class);
                    getContext().startActivity(i);

                }
            });


        }catch (Exception e){
            // Logea la exception..
            Log.i("tracker2", "PrductoAdapter.getView Exception" + e.getMessage());
        }
        return convertView;
    }
}
