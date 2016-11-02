package ar.com.universitas.utilities;

import java.util.ArrayList;
import java.util.List;
import ar.com.universitas.model.ProductModel;

/**
 * Created by Administrador on 11/1/2016.
 */

public class Utilities {

    public static List verifyChangeOnList (ProductModel[] original, ProductModel[] modificada){

        //Lista Productos de salida;
        List<ProductModel> listaProductosModicados = new ArrayList<>();

        //Producto Modificado listo para insertar en el almacen
        ProductModel productoModificado;

        //comparo posicion a posicion y voy almacenando en otro list.
        for (int i = 0; i < original.length; i++) {

            if (!((original[i].getIdProduct() == modificada[i].getIdProduct()) &&
                    (original[i].getNombre().equals(modificada[i].getNombre()) &&
                            (original[i].getCheckBoxValue() == modificada[i].getCheckBoxValue())))){

                productoModificado = new ProductModel(modificada[i].getNombre(),modificada[i].getCheckBoxValue(), modificada[i].getIdProduct());

                listaProductosModicados.add(productoModificado);

            }//fin if
        }//fin for

        return listaProductosModicados;
    }
}
