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
import ar.com.universitas.model.RecetasModel;

/**
 * Created by Chino on 21/12/2016.
 */

public class RapiRecetasAdapter extends ArrayAdapter<RecetasModel>{


    private RecetasModel[] modelItems = null;
    private Context contxt;

    public RapiRecetasAdapter (Context context, RecetasModel[] resource) {
        super(context, R.layout.list_rapirecetas,resource);
        this.contxt=context;
        this.setModelItems(resource);
    }

    public RecetasModel[] getModelItems() {
        return modelItems;
    }
    public void setModelItems(RecetasModel[] modelItems) {
        this.modelItems = modelItems;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) contxt).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_rapirecetas, parent, false);

        TextView descripcion = (TextView) convertView.findViewById(R.id.recetasDescripcion);
        EditText idReceta = (EditText) convertView.findViewById(R.id.recetasID);
        Button buttonName = (Button) convertView.findViewById(R.id.recetasName);


        try {
            descripcion.setText(getModelItems()[position].getDescripcionReceta());
            String idRecet = String.valueOf(getModelItems()[position].getRecetaID());
            idReceta.setText(idRecet);
            buttonName.setText(getModelItems()[position].getTituloReceta());
            buttonName.setTag(position);
            buttonName.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i("Edit Button Clicked", "**********");
                    int id = getModelItems()[position].getRecetaID();
                    String description=getModelItems()[position].getDescripcionReceta();
                    Toast.makeText(contxt, "Edit button Clicked - ID: "+id +"description: "+description, Toast.LENGTH_LONG).show();

                    SharedPreferences sp1 = getContext().getSharedPreferences("DescripcionReceta",contxt.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp1.edit();
                    editor.putString("descripcion", description);
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
