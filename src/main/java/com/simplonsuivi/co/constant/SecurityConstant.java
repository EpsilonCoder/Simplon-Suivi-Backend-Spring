package com.simplonsuivi.co.constant;

public class SecurityConstant {
	
	public static final long EXPIRATION_TIME=432_000_000; // Cette constante represente 5jour en miliseconde
	
	public static final String TOKEN_PREFIX="Bearer";
	
    public static final String JWT_TOKEN_HEARDER="Jwt-Token";
    
    public static final String GET_ARRAYS_LLC="Simplon Sénégal , LLC";
    
    public static final String TOKEN_CANNOT_BE_VERIFIED="Votre jeton ne peut etre Vérifié";
    
    public static final String GET_ARRAYS_ADMINISTRATION="Simplon Sénégal portal";
    
    public static final String AUTHORITIES ="Authorities";
    
    public static final String FORBIDDEN_MESSAGE="Vous devez etre connecté pour avour acces a ce contenue";

    public static final String ACCESS_DENIED_MESSAGE="Vous n'avez pas la permissoon d'acceder a cette page";
    
    public static final String OPTIONS_HTTP_METHOD="OPTIONS";
    
    public static final String[] PUBLIC_URLS= {"user/login","user/register","user/resetpassword/**"};
    
}
