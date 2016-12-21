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

public class FuturaCompra_ListNegocios extends AppCompatActivity {


    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> storeMissingList;

    private static String URL_LIST_STORE = "http://clappuniv.esy.es/clappfc/listanegocios.php";

    // JSON Node names
    private static final String TAG_SUCCESSS = "success";
    private static final String TAG_PRODUCTS_STOREDS = "store";
    private static final String TAG_PRODUCT_IDS = "nombretienda";
    private static final String TAG_QTYS = "direccion";
    private static final String TAG_NAMES = "horario";

    // productStored JSONArray
    JSONArray localStored = null;

    ListView lista;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.futuracompra_listanegocios);


        // Hashmap para el ListView
        storeMissingList = new ArrayList<HashMap<String, String>>();


        // Cargar los productos en el Background Thread
        new LoadAllProducts().execute();
        lista = (ListView) findViewById(R.id.listStoreMisssing);

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
            pDialog = new ProgressDialog(FuturaCompra_ListNegocios.this);
            pDialog.setMessage("Cargando Negocios. Por favor espere...");
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
            SharedPreferences sp1 = getSharedPreferences("idproducto",MODE_PRIVATE);
            String idprod = sp1.getString("idprod" , "0" );

            // Building Parameters
            List paramsListStore = new ArrayList();
            paramsListStore.add(new BasicNameValuePair("idproducto", idprod));

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
                    localStored = jsonMyStores.getJSONArray(TAG_PRODUCTS_STOREDS);

                    for (int i = 0; i < localStored.length(); i++) {
                        JSONObject c = localStored.getJSONObject(i);

                        // Storing each json item in variable
                        String nombrelocal = c.getString(TAG_PRODUCT_IDS);
                        String direccion = c.getString(TAG_QTYS);
                        String horario = c.getString(TAG_NAMES);
                        // Si la cantidad de producto es cero lo mando dentro de la lista de faltantes.

                        HashMap map = new HashMap();
                        map.put(TAG_NAMES, horario);
                        map.put(TAG_QTYS, direccion);
                        map.put(TAG_PRODUCT_IDS,nombrelocal);

                        storeMissingList.add(map);

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
                            FuturaCompra_ListNegocios.this,
                            storeMissingList,
                            R.layout.listanegocios,
                            new String[] {
                                    TAG_NAMES,
                                    TAG_PRODUCT_IDS,
                                    TAG_QTYS,
                            },
                            new int[] {
                                    R.id.storeMissingName,
                                    R.id.storeMissingNameID,
                                    R.id.storeMissingQTY,
                            });
                    // updating listview
                    //setListAdapter(adapter);
                    lista.setAdapter(adapter);

                }
            });
        }
    }
}