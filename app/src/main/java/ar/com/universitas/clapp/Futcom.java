package ar.com.universitas.clapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class Futcom extends AppCompatActivity {


    Button btnIrListaNegocios;


    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsMissingList;

    private static String URL_MY_STORE_USERID = "http://clappuniv.esy.es/clappfc/almacenusuarioordenado.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS_STORED = "storing";
    private static final String TAG_PRODUCT_ID = "producto";
    private static final String TAG_QTY = "cantidad";
    private static final String TAG_NAME = "nombre";

    // productStored JSONArray
    JSONArray productStored = null;

    ListView lista;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.futcom);
        btnIrListaNegocios = (Button) findViewById(R.id.productMissingName);


        // Hashmap para el ListView
        productsMissingList = new ArrayList<HashMap<String, String>>();


        // Cargar los productos en el Background Thread
        new LoadAllProducts().execute();
        lista = (ListView) findViewById(R.id.listProductsMisssing);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }//fin onCreate

    public void onClickIrListaNegocios (View view){
        Intent i = new Intent(this,FuturaCompra_ListNegocios.class);
        startActivity(i);}


    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Antes de empezar el background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Futcom.this);
            pDialog.setMessage("Cargando Productos Faltantes. Por favor espere...");
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

                    for (int i = 0; i < productStored.length(); i++) {
                        JSONObject c = productStored.getJSONObject(i);

                        if ((c.getString(TAG_NAME) != null)&&(!c.getString(TAG_NAME).equals(""))
                                &&(c.getString(TAG_PRODUCT_ID)!= null)
                                &&(!c.getString(TAG_PRODUCT_ID).equals("")))
                        {
                            HashMap map = new HashMap();
                            map.put(TAG_NAME, c.getString(TAG_NAME));
                            map.put(TAG_QTY, c.getInt(TAG_QTY));
                            map.put(TAG_PRODUCT_ID, c.getString(TAG_PRODUCT_ID));
                            productsMissingList.add(map);
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
                    ListAdapter adapter = new SimpleAdapter(
                            Futcom.this,
                            productsMissingList,
                            R.layout.listview_futuracompra,
                            new String[] {
                                    TAG_NAME,
                                    TAG_PRODUCT_ID,
                                    TAG_QTY,
                            },
                            new int[] {
                                    R.id.productMissingName,
                                    R.id.productMissingNameID,
                                    R.id.productMissingQTY,
                            });
                    // updating listview
                    //setListAdapter(adapter);
                    Log.i("onPostExecute: ", "se setea adapter ");
                    lista.setAdapter(adapter);

                }
            });
        }
    }
}