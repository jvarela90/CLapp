package ar.com.universitas.clapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Julian on 14/12/2016.
 */

public class DescripcionRecetas extends AppCompatActivity {


    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> recetasList;

    private static String URL_LIST_STORE = "http://clappuniv.esy.es/clapprr/descripcionreceta.php";

    // JSON Node names
    private static final String TAG_SUCCESSS = "success";
    private static final String TAG_PRODUCTS_STOREDS = "descreceta";
    private static final String TAG_NOMBRE_RECETA = "tituloreceta";
    private static final String TAG_DESC_RECETA = "descripcionreceta";

    // productStored JSONArray
    JSONArray recetaStored = null;

    ListView lista;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listadescripcion_recetas);


        // Hashmap para el ListView
        recetasList = new ArrayList<HashMap<String, String>>();


        // Cargar los productos en el Background Thread
        new LoadAllProducts().execute();
        lista = (ListView) findViewById(R.id.list_descripreceta);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }//fin onCreate


    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DescripcionRecetas.this);
            pDialog.setMessage("Cargando Descripcion de su Receta. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * obteniendo todos los productos0
         * */
        protected String doInBackground(String... args) {
            //Create a new Json for MyStoreOnDB
            JSONParser jParserMyStores = new JSONParser();

            //SharedPreferences
            SharedPreferences sp1 = getSharedPreferences("idreceta",MODE_PRIVATE);
            String idrec = sp1.getString("idrec" , "0" );

            // Building Parameters
            List paramsListStore = new ArrayList();
            paramsListStore.add(new BasicNameValuePair("idreceta", idrec));

            // getting JSON string from URL
            JSONObject jsonMyStores = jParserMyStores.makeHttpRequest(URL_LIST_STORE, "POST", paramsListStore);
            // Check your log cat for JSON
            Log.d("My Store by UserdID: ", jsonMyStores.toString());

            try {
                // Checking for SUCCESS TAG forMyStoreOnDB
                int successMyStore = jsonMyStores.getInt(TAG_SUCCESSS);
                boolean storedProducts = false;
                if ((successMyStore == 1)&&((jsonMyStores.getJSONArray(TAG_PRODUCTS_STOREDS).length() > 0))){
                    storedProducts = true;
                    recetaStored = jsonMyStores.getJSONArray(TAG_PRODUCTS_STOREDS);

                    for (int i = 0; i < recetaStored.length(); i++) {
                        JSONObject c = recetaStored.getJSONObject(i);

                        // Storing each json item in variable
                        String tituloreceta = c.getString(TAG_NOMBRE_RECETA);
                        String descripcion = c.getString(TAG_DESC_RECETA);
                        // Si la cantidad de producto es cero lo mando dentro de la lista de faltantes.

                        HashMap map = new HashMap();
                        map.put(TAG_DESC_RECETA, descripcion);
                        map.put(TAG_NOMBRE_RECETA,tituloreceta);

                        recetasList.add(map);

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
            // dismiss the dialog after getting all productStored
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            DescripcionRecetas.this,
                            recetasList,
                            R.layout.listanegocios,
                            new String[] {
                                    TAG_NOMBRE_RECETA,
                                    TAG_DESC_RECETA,
                            },
                            new int[] {
                                    R.id.RecetaNombre,
                                    R.id.RecetaDescripcion,
                            });
                    // updating listview
                    //setListAdapter(adapter);
                    lista.setAdapter(adapter);

                }
            });
        }
    }
}