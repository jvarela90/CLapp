package ar.com.universitas.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ar.com.universitas.clapp.DescripcionRecetas;
import ar.com.universitas.clapp.R;
import ar.com.universitas.model.ProductModel;

/**
 * Created by Julian on 21/12/2016.
 */

public class RapiRecetasAdapter extends ArrayAdapter<ProductModel>{


    private ProductModel[] modelItems = null;
    private Context contxt;

    public RapiRecetasAdapter (Context context, ProductModel[] resource) {
        super(context, R.layout.list_rapirecetas,resource);
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
        convertView = inflater.inflate(R.layout.list_rapirecetas, parent, false);

        TextView descripcion = (TextView) convertView.findViewById(R.id.recetasMissingDescripcion);
        EditText idReceta = (EditText) convertView.findViewById(R.id.recetasMissingNameID);
        Button buttonName = (Button) convertView.findViewById(R.id.recetasMissingName);


        try {
            descripcion.setText(String.valueOf(getModelItems()[position].getCantidad()));
            String idRecet = String.valueOf(getModelItems()[position].getIdProduct());
            idReceta.setText(idRecet);
            buttonName.setText(getModelItems()[position].getNombre());
            buttonName.setTag(position);
            //TODO meter el Listener para el onclick y atrapar el valor del boton clickeado.
            //TODO obtenido eso meterlo en el sharepreferences y reenviarlo a futuraCompraListaNegocio.java
            buttonName.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.i("Edit Button Clicked", "**********");
                    int id = getModelItems()[position].getIdProduct();
                    Toast.makeText(contxt, "Edit button Clicked - ID: "+id,
                            Toast.LENGTH_LONG).show();

                    //Shareo el ID
                    // save user data
                    SharedPreferences sp1 = getContext().getSharedPreferences("idreceta",contxt.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp1.edit();
                    editor.putString("idrec", String.valueOf(id));
                    editor.commit();
                    ((Activity) contxt).finish();

                    Intent i = new Intent(contxt, DescripcionRecetas.class);
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
