package com.herprogramacion.geekyweb.tools;

/**
 * Clase que contiene los códigos usados en "I Wish" para
 * mantener la integridad en las interacciones entre actividades
 * y fragmentos
 */
public class Constantes {
    /**
     * Transición Home -> Detalle
     */
    public static final int CODIGO_DETALLE = 100;

    /**
     * Transición Detalle -> Actualización
     */
    public static final int CODIGO_ACTUALIZACION = 101;
    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta carácteristica.
     */
    private static final String PUERTO_HOST = ":82";
    /**
     * Dirección IP de genymotion o AVD
     */
    private static final String IP = "192.168.196.1";//"172.17.28.81";
    /**
     * URLs del Web Service
     */
//http://192.168.52.2:82/proyecto/obtener_metas.php
    public static final String GET = "http://" + IP + PUERTO_HOST;
    public static final String GET_BY_ID = "http://" + IP + PUERTO_HOST + "/proyecto/obtener_meta_por_id.php";
    public static final String UPDATE = "http://" + IP + PUERTO_HOST + "/proyecto/actualizar_meta.php";
    public static final String DELETE = "http://" + IP + PUERTO_HOST + "/proyecto/borrar_meta.php";
    public static final String INSERT = "http://" + IP + PUERTO_HOST + "/proyecto1/insertarComentario.php";
    public static final String INSERTUSUARIO = "http://" + IP + PUERTO_HOST + "/proyecto1/insertarUsuario.php";
    public static final String UPDATEUSUARIO = "http://" + IP + PUERTO_HOST + "/proyecto1/actualizar_usuario.php";

   // String cadena = "http://192.168.196.1:82/proyecto1/obtener_preguntas.php";//"http://maps.googleapis.com/maps/api/geocode/json?latlng=";

    /**
     * Clave para el valor extra que representa al identificador de una meta
     */
    public static final String EXTRA_ID = "IDEXTRA";

}
