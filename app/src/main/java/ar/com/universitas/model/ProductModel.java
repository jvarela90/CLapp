package ar.com.universitas.model;

/**
 * Created by Chino on 10/5/2016.
 * @see que esta clase sirva de guia para las buenas practicas.
 * @version esta clase matchea con la tabla PRODING,
 * @Question: deberiamos agregar un campo mas en la tabla para mantener el estado del check?
 *
 */

public class ProductModel {

    /* los atributos de una clase en general son como datos sensibles
    estos no podran ser accesados desde fuera de la clase */
    private String nombre;
    private boolean checkBoxValue;
    private int idProduct;

    public ProductModel(String nombre, boolean checkBoxValue, int idProduct) {
        this.nombre = nombre;
        this.checkBoxValue = checkBoxValue;
        this.idProduct = idProduct;
    }

    /**/
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean getCheckBoxValue() {
        return checkBoxValue;
    }

    public void setCheckBoxValue(boolean checkBoxValue) {
        this.checkBoxValue = checkBoxValue;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }
}
