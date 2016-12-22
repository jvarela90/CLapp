package ar.com.universitas.clapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ar.com.universitas.adapter.RapiRecetasAdapter;
import ar.com.universitas.model.ProductModel;

public class Raprec extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;
    Button btnIrListaRecetas;

    // Creating JSON object
    JSONParser jParser = new JSONParser();

    ProductModel[] recetasList;

    private static String URL_MY_STORE_USERID = "http://clappuniv.esy.es/clapprr/rapirecetas.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS_STORED = "receta";
    private static final String TAG_PRODUCT_ID = "pruebarecetaid";
    private static final String TAG_DESC_RECETA = "descripcionreceta";
    private static final String TAG_NAME = "tituloreceta";

    // productStored JSONArray
    JSONArray productStored = null;

    ListView lista;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.raprec);
        btnIrListaRecetas = (Button) findViewById(R.id.recetasMissingName);

        // Cargar los productos en el Background Thread
        new LoadRecetas(this).execute();
        lista = (ListView) findViewById(R.id.listRecetasMisssing);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }//fin onCreate


    class LoadRecetas extends AsyncTask<String, String, String> {

        private Context context;

        public LoadRecetas(Context context) {
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
            pDialog = new ProgressDialog(Raprec.this);
            pDialog.setMessage("Cargando Recetas. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * obteniendo todos los productos
         * */
        protected String doInBackground(String... args) {
            //Create a new Json for MyStoreOnDB
            JSONParserFuturaCompra jParserMyStore = new JSONParserFuturaCompra();

            //SharedPreferences
            SharedPreferences sp1 = getSharedPreferences("perfilusuario",MODE_PRIVATE);
            int idusuario = sp1.getInt("idusuario" , 0 );
            String cadena = Integer.toString(idusuario);

            Log.d("My UserdID: ", cadena);

            // Building Parameters
            List paramsMyStore = new ArrayList();
            paramsMyStore.add(new BasicNameValuePair("userID", cadena));

            // getting JSON string from URL
            JSONObject jsonMyStore = jParserMyStore.makeHttpRequest(URL_MY_STORE_USERID, "POST", paramsMyStore);
            // Check your log cat for JSON reponse
            Log.d("My Store by UserdID: ", jsonMyStore.toString());

            try {
                // Checking for SUCCESS TAG for MyStoreOnDB
                int successMyStore = jsonMyStore.getInt(TAG_SUCCESS);
                Log.d("successMyStore: ", String.valueOf(successMyStore));

                boolean storedProducts = false;
                if ((successMyStore == 1)&&((jsonMyStore.getJSONArray(TAG_PRODUCTS_STORED).length() > 0))){
                    storedProducts = true;
                    productStored = jsonMyStore.getJSONArray(TAG_PRODUCTS_STORED);

                    Log.d("jsonMyStore -length: ", String.valueOf(productStored.length()));

                    recetasList = new ProductModel[productStored.length()];
                    ProductModel productModel;

                    for (int i = 0; i < productStored.length(); i++) {
                        JSONObject c = productStored.getJSONObject(i);

                        if ((c.getString(TAG_NAME) != null)&&(!c.getString(TAG_NAME).equals(""))
                                &&(c.getString(TAG_PRODUCT_ID)!= null)
                                &&(!c.getString(TAG_PRODUCT_ID).equals("")))
                        {
                            productModel = new ProductModel(c.getString(TAG_NAME),Boolean.TRUE,c.getInt(TAG_PRODUCT_ID),c.getInt(TAG_DESC_RECETA),"U");
                            recetasList[i]=productModel;
                        }

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("JSONException: ", e.getMessage());

            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all productStored
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    RapiRecetasAdapter rapiRecetasAdapter = new RapiRecetasAdapter(getContext(), recetasList);
                    // updating listview
                    lista.setAdapter(rapiRecetasAdapter);

                }
            });
        }
    }
}