package ar.com.universitas.clapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.view.View;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import ar.com.universitas.adapter.ProductoAdapter;
import ar.com.universitas.model.ProductModel;
import ar.com.universitas.utilities.Utilities;

public class Armilist extends AppCompatActivity {

    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParseraml jParser = new JSONParseraml();

    // Creo un arreglo con nuestro objeto producto
    ProductModel[] productosList, originalList;

    // url to get all products list
    private static String url_all_empresas = "http://clappuniv.esy.es/clappaml/get_all_empresas.php/prod1";

    // url to get all products list
    private static String URL_MY_STORE_USERID = "http://clappuniv.esy.es/clappma/MiAlmacenbyUsuario.php";


    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "proding";
    private static final String TAG_PRODUCTS_STORED = "storing";
    private static final String TAG_ID = "id";
    private static final String TAG_NOMBRE = "nombre";
    // products JSONArray
    JSONArray products = null;
    JSONArray productStored = null;
    //Lista q contiene los productos seleccionables.
    ListView lista;

    @Override
    public void onCreate(Bundle savedInstanceState) { //Metodo que se llama previo a que se cargue la vista: armilist.xml
        super.onCreate(savedInstanceState);
        setContentView(R.layout.armilist);

        // Cargar los productos en el Background Thread
        new LoadAllProducts(this).execute();
        lista = (ListView) findViewById(R.id.listAllProducts);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }//fin onCreate

    /**
    * @see
    * @description: Este método se ejecutará cuando se presione el botón Guardar en mi Almancen
    */
    public void saveListOnMyStore(View view) {

        /**TODO - llamar a un metodo para verificar los cambios en las listas.
         * El metodo podria devolver un JSON con los datos a insertar de la lista
        */
        List<ProductModel> listaProductosModificados = Utilities.verifyChangeOnList(originalList,productosList);
        Log.i("Armilist", "saveListOnMyStore -  Lista de elementos seleccionados");
        for (ProductModel productModel : listaProductosModificados){
            Log.i("Producto Modificado - ",productModel.toString());
        }
    }

    /**
     * @classInner
     * Clase interna para ejecutar en background los procesos de datos de los Productos
     */
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
            pDialog.setMessage("Cargando Productos. Por favor espere...");
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


            //Create a new Json for MyStoreOnDB
            JSONParser jParserMyStore = new JSONParser();
            // Building Parameters
            List paramsMyStore = new ArrayList();
            paramsMyStore.add(new BasicNameValuePair("userID", "8129")); //Harcodeamos este valor mientras lo obtenemos de la session.
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
                    productStored = jsonMyStore.getJSONArray(TAG_PRODUCTS_STORED);
                }

                // Checking for SUCCESS TAG for products
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
                        productosList = new ProductModel[products.length()];
                        //preparo el puntero pero tener una copia de los estados originales de la lista.
                        originalList = new ProductModel[products.length()];
                        //uso una sola referencia al objeto
                        ProductModel productModel;
                        ProductModel productModelOriginal;
                        for (int i = 0; i < products.length(); i++) {
                            JSONObject c = products.getJSONObject(i);

                            // Storing each json item in variable
                            int id = c.getInt(TAG_ID);
                            String name = c.getString(TAG_NOMBRE);
                            int quantity = 0;
                            Boolean checked = false;

                            // TODO verificar q no existe en la lista de los productos del almacen
                            if(storedProducts){
                                //TODO no esta pasando la variable por referencia del checked
                                quantity = getQuantityfromMyStore(checked,id);
                                checked = (quantity > 0) ? true : false;
                            }

                            // Instancio mi clase modelo con la informacion obtenida
                            productModel = new ProductModel(name,checked.booleanValue(),id,quantity);
                            productModelOriginal = new ProductModel(name,checked.booleanValue(),id,quantity);
                            //lo meto a la bolsa
                            productosList[i]=productModel;
                            originalList[i]=productModelOriginal;
                        }
                    }// fin del IF
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private int getQuantityfromMyStore(Boolean checado, int productoID){

            int i = 0;
            int quantity = 0;
            try {
                 while ((!checado.booleanValue())&&(i < productStored.length()))  {
                     JSONObject jsonObject = productStored.getJSONObject(i);
                     if (jsonObject.getInt("producto") == productoID){
                         checado = true;
                         quantity = jsonObject.getInt("cantidad");
                     }else{
                         i++;
                     }
                 }
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }
            return quantity;
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
                    ProductoAdapter productoAdapter = new ProductoAdapter(getContext(), productosList);
                    // updating listview
                    lista.setAdapter(productoAdapter);
                }
            });
        }
    }/** end Inner class*/
}