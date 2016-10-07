package ar.com.universitas.clapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ar.com.universitas.adapter.ProductoAdapter;
import ar.com.universitas.model.ProductModel;

public class Armilist extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParseraml jParser = new JSONParseraml();

    //ArrayList<HashMap<String, String>> empresaList;

    // Creo un arreglo con nuestro objeto producto
    ProductModel[] empresaList;

    // url to get all products list
    private static String url_all_empresas = "http://clappuniv.esy.es/clappaml/get_all_empresas.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "proding";
    private static final String TAG_ID = "id";
    private static final String TAG_NOMBRE = "nombre";
    // products JSONArray
    JSONArray products = null;

    ListView lista;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.armilist);

        // Hashmap para el ListView
        //empresaList = new ArrayList<HashMap<String, String>>();

        // Cargar los productos en el Background Thread
        new LoadAllProducts(this).execute();
        lista = (ListView) findViewById(R.id.listAllProducts);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }//fin onCreate


    class LoadAllProducts extends AsyncTask<String, String, String> {

        private Context context;

        public LoadAllProducts(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Armilist.this);
            pDialog.setMessage("Cargando comercios. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * obteniendo todos los productos
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List params = new ArrayList();
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_empresas, "GET", params);
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());

            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    products = json.getJSONArray(TAG_PRODUCTS);

                    // looping through All Products
                    Log.i("tracker1", "produtos.length" + products.length());

                    //Valido q el Json no venga vacio ni nuleado
                    if ((products != null) && (products.length()>0)){
                        //Inicializo el array de productos parseado a objetos del Modelo.
                        empresaList = new ProductModel[products.length()];
                        //uso una sola referencia al objeto
                        ProductModel productModel;

                        for (int i = 0; i < products.length(); i++) {
                            JSONObject c = products.getJSONObject(i);

                            // Storing each json item in variable
                            int id = c.getInt(TAG_ID);
                            String name = c.getString(TAG_NOMBRE);

                            // Instancio mi clase modelo con la informacion obtenida
                            productModel = new ProductModel(name,1,id);
                            //lo meto a la bolsa
                            empresaList[i]=productModel;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ProductoAdapter productoAdapter = new ProductoAdapter(getContext(),empresaList);
                    // updating listview
                    //setListAdapter(adapter);
                    lista.setAdapter(productoAdapter);
                }
            });
        }
    }
}