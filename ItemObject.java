package ar.com.nbcargo.nbcargo_choferes;

import com.google.gson.annotations.SerializedName;

public class ItemObject {

    @SerializedName("turno")
    private String turno;
    @SerializedName("hora")
    private String hora;
    @SerializedName("chofer1")
    private String chofer1;
    @SerializedName("unidad1")
    private String unidad1;
    @SerializedName("carril1")
    private String carril1;
    @SerializedName("estado1")
    private String estado1;
    @SerializedName("chofer2")
    private String chofer2;
    @SerializedName("unidad2")
    private String unidad2;
    @SerializedName("carril2")
    private String carril2;
    @SerializedName("estado2")
    private String estado2;


    public ItemObject(String turno, String hora, String chofer1, String unidad1, String carril1, String estado1, String chofer2, String unidad2, String carril2, String estado2) {
        this.turno = turno;
        this.hora = hora;
        this.chofer1 = chofer1;
        this.unidad1 = unidad1;
        this.carril1 = carril1;
        this.estado1 = estado1;
        this.chofer2 = chofer2;
        this.unidad2 = unidad2;
        this.carril2 = carril2;
        this.estado2 = estado2;

    }

    public String getTurno() {
        return turno;
    }

    public String getHora() {
        return hora;
    }

    public String getChofer1() {
        return chofer1;
    }

    public String getunidad1() {
        return unidad1;
    }

    public String getCarril1() {
        return carril1;
    }

    public String getEstado1() {
        return estado1;
    }

    public String getChofer2() {
        return chofer2;
    }

    public String getunidad2() {
        return unidad2;
    }

    public String getCarril2() {
        return carril2;
    }

    public String getEstado2() {
        return estado2;
    }
}

