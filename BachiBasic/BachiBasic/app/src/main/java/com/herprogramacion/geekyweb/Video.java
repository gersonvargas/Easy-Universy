package com.herprogramacion.geekyweb;

public class Video {
    private String Nombre;
    private String direccion;
    private int iconID;

    public Video(String nombre, String direccion, int iconID) {
        super();
        this.Nombre=nombre;
        this.direccion = direccion;
        this.iconID=iconID;
    }
    public String getNombre() {return Nombre; }

    public String getDireccion() {
        return direccion;
    }
    public int getIconID() {  return iconID; }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
