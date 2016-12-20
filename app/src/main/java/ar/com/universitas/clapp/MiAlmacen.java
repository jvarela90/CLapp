package ar.com.universitas.clapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ar.com.universitas.adapter.ProductoAdapter;
import ar.com.universitas.adapter.ProductoAlmacenadoAdapter;
import ar.com.universitas.model.ProductModel;
import ar.com.universitas.tasks.RefreshProducts;
import ar.com.universitas.utilities.Utilities;

/**
 * @author Chinuxs
 */
public class MiAlmacen extends Activity {

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS_STORED = "storing";
    private static final String TAG_ID = "producto";
    private static final String TAG_NOMBRE = "nombre";
    private static final String TAG_QTY = "cantidad";
    private static final String UPDATE_OPERATION = "U";
    private static String URL_MY_STORE_USERID = "http://clappuniv.esy.es/clappma/MiAlmacenByUserCompleted.php";

    private int sessionUserID;

    private ProgressDialog pDialog;
    JSONParseraml jParser = new JSONParseraml();
    private ProductModel[] productosList;
    private ProductModel[] originalList;

    // productStored JSONArray
    JSONArray products = null;
    JSONArray productStored = null;

    //Lista q contiene los productos en mi almacen.
    private ListView lista;

    public int getSessionUserID() {
        return sessionUserID;
    }

    public void setSessionUserID(int sessionUserID) {
        this.sessionUserID = sessionUserID;
    }

    public ProductModel[] getProductosList() {
        return productosList;
    }

    public void setProductosList(ProductModel[] productosList) {
        this.productosList = productosList;
    }

    public ProductModel[] getOriginalList() {
        return originalList;
    }

    public void setOriginalList(ProductModel[] originalList) {
        this.originalList = originalList;
    }

    public ListView getLista() {
        return lista;
    }

    public void setLista(ListView lista) {
        this.lista = lista;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mialm);

        //SharedPreferences
        SharedPreferences sp1 = getSharedPreferences("perfilusuario",MODE_PRIVATE);
        int idusuario = sp1.getInt("idusuario" , 0 );
        this.setSessionUserID(idusuario);

        new LoadMyStoreProducts(this).execute();
        setLista((ListView) findViewById(R.id.listaMiAlmacen));

    }//fin onCreate


    public void updateListOnMyStore(View view) {

        List<ProductModel> listaProductosModificados = Utilities.verifyChangeOnList(getOriginalList(), getProductosList());
        Log.i("Armilist", "saveListOnMyStore -  Lista de elementos seleccionados");
        for (ProductModel productModel : listaProductosModificados){
            Log.i("Producto Modificado - ",productModel.toString());
        }

        //Call to the PHP by Rest
        new RefreshProducts(listaProductosModificados, getSessionUserID(), this).execute();
    }


    /**
     * @classInner
     * Clase interna para ejecutar en background los procesos de datos de los Productos
     */
    class LoadMyStoreProducts extends AsyncTask<String, String, String> {

        private Context context;

        public LoadMyStoreProducts(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MiAlmacen.this);
            pDialog.setMessage("Cargando Productos en su Almacen. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * obteniendo todos los productos
         * */
        protected String doInBackground(String... args) {
            //Create a new Json for MyStoreOnDB
            JSONParser jParserMyStore = new JSONParser();

            String usuarioID = Integer.toString(getSessionUserID());

            // Building Parameters
            List paramsMyStore = new ArrayList();
            paramsMyStore.add(new BasicNameValuePair("userID", usuarioID));
            // getting JSON string from URL
            JSONObject jsonMyStore = jParserMyStore.makeHttpRequest(URL_MY_STORE_USERID, "POST", paramsMyStore);
            // Check your log cat for JSON reponse
            Log.d("My Store by UserdID: ", jsonMyStore.toString());

            try {
                // Checking for SUCCESS TAG for MyStoreOnDB
                int successMyStore = jsonMyStore.getInt(TAG_SUCCESS);
                boolean storedProducts = false;
                if ((successMyStore == 1)&&((jsonMyStore.getJSONArray(TAG_PRODUCTS_STORED).length() > 0))){
                    storedProducts = true;
                    //Obtengo los productos del Mi Almacen
                    productStored = jsonMyStore.getJSONArray(TAG_PRODUCTS_STORED);
                    Log.i("tracker1", "productStored.length" + productStored.length());

                    setProductosList(new ProductModel[productStored.length()]);
                    setOriginalList(new ProductModel[productStored.length()]);
                    //uso una sola referencia al objeto
                    ProductModel productModel;
                    ProductModel productModelOriginal;

                    for (int i = 0; i < productStored.length(); i++) {
                        JSONObject c = productStored.getJSONObject(i);
                        int id = c.getInt(TAG_ID);
                        String name = c.getString(TAG_NOMBRE);
                        int quantity = c.getInt(TAG_QTY);

                        //no tendremos en cuentas los checkbox en esta vista y todas las Operaciones seran Update
                        productModel = new ProductModel(name,Boolean.TRUE,id,quantity,UPDATE_OPERATION);
                        productModelOriginal = new ProductModel(name,Boolean.TRUE,id,quantity,UPDATE_OPERATION);
                        getProductosList()[i]=productModel;
                        getOriginalList()[i]=productModelOriginal;
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
                    ProductoAlmacenadoAdapter productoAlmacenadoAdapter = new ProductoAlmacenadoAdapter(getContext(), getProductosList());
                    // updating listview
                    getLista().setAdapter(productoAlmacenadoAdapter);
                }
            });
        }
    }/** end Inner class*/


}
