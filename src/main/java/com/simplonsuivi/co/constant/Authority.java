package com.simplonsuivi.co.constant;

public class Authority {
	
	public static final String[] USER_AUTHORITIES= {"Apprenant:lecture"};
	public static final String[] HR_AUTHORITIES= {"user:lecture","user:Mise a jour"};
	public static final String[] MANAGER_AUTHORITIES= {"user:lecture","user:update"};
	public static final String[] ADMIN_AUTHORITIES= {"Administrateur:lecture","Administrateur:mise a jour","Administrateur:Création"};
	public static final String[] SUPER_USER_AUTHORITIES= {"Super Admin:lecture","Super Admin:Mise a jour","Super Admin:Création","Super Admin:Suppression"};
	

}
