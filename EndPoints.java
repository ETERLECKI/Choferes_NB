package ar.com.nbcargo.nbcargo_choferes;

class EndPoints {
    public static String ROOT_URL = "http://192.168.5.14/";
    public static String GuardaImg_srv = "guarda_imagen.php?apicall=";
    public static final String UPLOAD_URL = ROOT_URL + GuardaImg_srv + "uploadpic";
    public static final String GET_PICS_URL = ROOT_URL + GuardaImg_srv + "getpics";
}
