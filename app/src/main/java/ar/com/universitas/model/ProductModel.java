package ar.com.universitas.model;

/**
 * Created by Chino on 10/5/2016.
 * @see  this class sirva de guia para las buenas practicas.
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
    private int cantidad;
    private String operation;


    public ProductModel(String nombre, boolean checkBoxValue, int idProduct, int cantidad, String tipoOperacion) {
        this.setNombre(nombre);
        this.setCheckBoxValue(checkBoxValue);
        this.setIdProduct(idProduct);
        this.setCantidad(cantidad);
        this.setOperation(tipoOperacion);
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {

        String s = "Nombre: "+ this.getNombre()
                + " Checked: " + this.getCheckBoxValue()
                + " ID: "+ String.valueOf(this.getIdProduct())
                + " cantidad: "+ String.valueOf(this.getCantidad())
                + " operacion: " + this.getOperation();
        return s;
    }



}
