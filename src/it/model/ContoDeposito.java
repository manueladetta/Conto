package it.model;

import java.time.LocalDate;

public class ContoDeposito extends Conto {

	private double tassoInteresse = 0.1/365; // giornaliero
	private double limite = 1000;
	
	public ContoDeposito(String titolare) {
		super(titolare);
	}
	
	@Override
	public void generaMovimento(double importo, String tipo, LocalDate data) {
		if(tipo.equals("P") && importo > limite) {
//			System.out.println("Impossibile prelevare più di 1.000 euro in una sola volta");

			logger.warn("Impossibile prelevare più di 1.000 euro in una sola volta");

			super.generaMovimento(1000, tipo, data);
		} else {
			super.generaMovimento(importo, tipo, data);
		}
	}

	

	@Override
	public double generaInteressi(int anno) {
		return super.calcolaInteressi(anno, tassoInteresse);
	}

}
