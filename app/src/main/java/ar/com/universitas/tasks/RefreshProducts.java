package ar.com.universitas.tasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ar.com.universitas.clapp.JSONParseraml;
import ar.com.universitas.model.ProductModel;

/**
 * Created by Administrador on 12/17/2016.
 */

public class RefreshProducts extends AsyncTask<String, String, String> {

    // url to get all productStored list
    private static String URL_MY_REFRESH_STORE = "http://clappuniv.esy.es/clappma/ActualizarMiAlmacen.php";

    List<ProductModel> lista;

    public RefreshProducts(List<ProductModel> listProducts){
        this.lista = listProducts;
    }

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
            JSONObject jsonProduct = new JSONObject();
            jsonProduct.put("id", "1");
            jsonProduct.put("usuarioID", "8129");
            jsonProduct.put("cantidad", "1");
            jsonProduct.put("operacion", "U");

            JSONObject jsonProduct01 = new JSONObject();
            jsonProduct01.put("id", "331");
            jsonProduct01.put("usuarioID", "8129");
            jsonProduct01.put("cantidad", "2");
            jsonProduct01.put("operacion", "I");

            JSONArray arrayProducts = new JSONArray();
            arrayProducts.put(jsonProduct);
            arrayProducts.put(jsonProduct01);

            JSONObject jsonProductsRefresh = new JSONObject();
            jsonProductsRefresh.put("Products", arrayProducts);

            //TODO - Crear una metodo nuevo en el JSONParsearaml.java:- override del metodo: makeHttpRequest
            //TODO  # Para permitir pasar un JSON completo con todos los productos
            //Create a new Json to refreshDB
            JSONParseraml jParserRefresh = new JSONParseraml();
            JSONObject jsonRefreshDB = jParserRefresh.makeHttpRequest(URL_MY_REFRESH_STORE, "POST", jsonProductsRefresh);
            Log.d("jsonRefreshDB: ", jsonRefreshDB.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(int code) {
        // TODO: check this.exception
        // retrieve your 'code' here
    }
}