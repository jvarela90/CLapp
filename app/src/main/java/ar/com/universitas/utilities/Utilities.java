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
                    (original[i].getNombre().equals(modificada[i].getNombre())) &&
                            (original[i].getCheckBoxValue() == modificada[i].getCheckBoxValue()) &&
                                    (original[i].getCantidad() == modificada[i].getCantidad()))){

                //Si entra a este if es por q el check cambio o cambio la cantidad...entonces
                //Si el checked es false en la modificada indica q se cambio respecto a la orginal, quiere decir
                if (!modificada[i].getCheckBoxValue()){
                    productoModificado = new ProductModel(modificada[i].getNombre(),modificada[i].getCheckBoxValue(), modificada[i].getIdProduct(), 0);
                }else {
                    productoModificado = new ProductModel(modificada[i].getNombre(),modificada[i].getCheckBoxValue(), modificada[i].getIdProduct(), modificada[i].getCantidad());
                }

                listaProductosModicados.add(productoModificado);

            }//fin if
        }//fin for

        return listaProductosModicados;
    }
}
