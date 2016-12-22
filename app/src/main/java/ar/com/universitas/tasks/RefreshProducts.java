package ar.com.universitas.tasks;


import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

import ar.com.universitas.clapp.Armilist;
import ar.com.universitas.clapp.JSONParseraml;
import ar.com.universitas.clapp.Login;
import ar.com.universitas.clapp.MainActivity;
import ar.com.universitas.clapp.Register;
import ar.com.universitas.model.ProductModel;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrador on 12/17/2016.
 */

public class RefreshProducts extends AsyncTask<String, String, String> {

    // Progress Dialog
    private ProgressDialog pDialog;

    private Context context;

    // url to get all productStored list
    private static String URL_MY_REFRESH_STORE = "http://clapp.esy.es/clappma/ActualizarMiAlmacen.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private List<ProductModel> lista;
    private int usuario;
    int successMyStore =0;

    public RefreshProducts(List<ProductModel> listProducts, int usuarioID, Context myContext){
        this.setLista(listProducts);
        this.setUsuario(usuarioID);
        this.setContext(myContext);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<ProductModel> getLista() {
        return lista;
    }

    public void setLista(List<ProductModel> lista) {
        this.lista = lista;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Actualizando su Almacen. Por favor espere...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    //
    protected String doInBackground(String... args) {
        //TODO - #JSON meter los productos en un JSON
        /**
         *      {Products}
         *          {id:}
         *          {usuarioID:}
         *          {cantidad:}
         *          {operacion: U or I}
         * */
        try {
            //array de JSON'sssss
            JSONArray arrayProducts = new JSONArray();

            for (ProductModel productModel: getLista()){
                //Creo cada instancia de JSON con lo q trae la lista de productosSeleccionadas
                JSONObject jsonProduct = new JSONObject();
                jsonProduct.put("id", productModel.getIdProduct());
                jsonProduct.put("usuarioID", this.getUsuario());
                jsonProduct.put("cantidad", productModel.getCantidad());
                jsonProduct.put("operacion", productModel.getOperation());
                arrayProducts.put(jsonProduct);
            }

            //Creo la cabecera de JSON
            JSONObject jsonProductsRefresh = new JSONObject();
            jsonProductsRefresh.put("Products", arrayProducts);

            //TODO - Crear una metodo nuevo en el JSONParsearaml.java:- override del metodo: makeHttpRequest
            //TODO  # Para permitir pasar un JSON completo con todos los productos
            //Create a new Json to refreshDB
            JSONParseraml jParserRefresh = new JSONParseraml();
            JSONObject jsonRefreshDB = jParserRefresh.makeHttpRequest(URL_MY_REFRESH_STORE, "POST", jsonProductsRefresh);
            Log.d("jsonRefreshDB: ", jsonRefreshDB.toString());

            int success = jsonRefreshDB.getInt(TAG_SUCCESS);

            if (success == 1) {
                Log.d("Update Successful!", String.valueOf(success));

                Intent i = new Intent(getContext(), MainActivity.class);
                Activity activity = (Activity) getContext();
                activity.finish();
                getContext().startActivity(i);
                return jsonRefreshDB.getString(TAG_MESSAGE);
            } else {
                Log.d("Update Failure!", jsonRefreshDB.getString(TAG_MESSAGE));
                return jsonRefreshDB.getString(TAG_MESSAGE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(String file_url) {
        pDialog.dismiss();
        if (file_url != null){
            Toast.makeText(getContext(), file_url, Toast.LENGTH_LONG).show();
        }
    }

}