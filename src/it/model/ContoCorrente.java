package it.model;

public class ContoCorrente extends Conto {
	private final double tassoInteresse = 0.07/365; // giornaliero
	
	public ContoCorrente(String titolare) {
		super(titolare);
	}

	@Override
	public double generaInteressi(int anno) {
		return super.calcolaInteressi(anno, tassoInteresse);
	}

}
