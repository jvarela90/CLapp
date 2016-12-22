package ar.com.universitas.model;

/**
 * Created by Chino on 12/22/2016.
 */

public class RecetasModel {

    private String tituloReceta;
    private String descripcionReceta;
    private int recetaID;

    public RecetasModel(int recetaID, String descripcionReceta, String tituloReceta) {
        this.recetaID = recetaID;
        this.descripcionReceta = descripcionReceta;
        this.tituloReceta = tituloReceta;
    }


    public String getTituloReceta() {
        return tituloReceta;
    }

    public void setTituloReceta(String tituloReceta) {
        this.tituloReceta = tituloReceta;
    }

    public String getDescripcionReceta() {
        return descripcionReceta;
    }

    public void setDescripcionReceta(String descripcionReceta) {
        this.descripcionReceta = descripcionReceta;
    }

    public int getRecetaID() {
        return recetaID;
    }

    public void setRecetaID(int recetaID) {
        this.recetaID = recetaID;
    }

    @Override
    public String toString() {
        return "RecetasModel{" +
                "tituloReceta='" + tituloReceta + '\'' +
                ", descripcionReceta='" + descripcionReceta + '\'' +
                ", recetaID=" + recetaID +
                '}';
    }
}
