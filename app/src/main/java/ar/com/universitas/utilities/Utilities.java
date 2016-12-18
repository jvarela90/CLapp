package ar.com.universitas.utilities;

import java.util.ArrayList;
import java.util.List;
import ar.com.universitas.model.ProductModel;

/**
 * Created by Administrador on 11/1/2016.
 */

public class Utilities {

    /**
     * @see this Metodo compara la lista de productos modificada por el usuario contra la misma
     * lista original, de esta manera verificamos los cambios
     *
     * */
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
                // que ese producto tendra 0 cantidad por q ya no es requerido, entonces se le asigna un cantidad de cero -------------------------|
                //                                                                                                                                 |
                if (!modificada[i].getCheckBoxValue()){//                                                                                          |
                    productoModificado = new ProductModel(modificada[i].getNombre(),modificada[i].getCheckBoxValue(), modificada[i].getIdProduct(), 0, modificada[i].getOperation());
                }else {
                    productoModificado = new ProductModel(modificada[i].getNombre(),modificada[i].getCheckBoxValue(), modificada[i].getIdProduct(), modificada[i].getCantidad(),modificada[i].getOperation());
                }

                listaProductosModicados.add(productoModificado);

            }//fin if
        }//fin for

        return listaProductosModicados;
    }
}
