package com.simplonsuivi.co.enumeration;
import static com.simplonsuivi.co.constant.Situation.*;

public enum Situation {

		
	Situation_apprenant(apprenant),
    Situation_En_entreprise(professionnel);
		
		
	private String[] situations;

	private Situation(String[] situations) {
		this.situations = situations;
	}

	public String[] getSituations() {
		return situations;
	}
		
		

}
