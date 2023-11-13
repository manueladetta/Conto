package it.model;

import java.util.Random;

public class ContoInvestimento extends Conto {
	
	public ContoInvestimento(String titolare) {
		super(titolare);
	}

	@Override
	public double generaInteressi(int anno) {
        Random random = new Random();
        
		int sec = random.nextInt(100);
        String tipo = sec % 2 == 0 ? "plus" : "minus";
        
        double tassoInteresse = random.nextInt(100); // tasso annuo
		tassoInteresse = tassoInteresse / (100 * 365); // tasso giornaliero
        
        double interessi = super.calcolaInteressi(anno, tassoInteresse);
		
		return (tipo.equals("plus")) ? interessi : (-interessi); // se plus allora interessi attivi, altrimenti passivi
                        
	}

}
