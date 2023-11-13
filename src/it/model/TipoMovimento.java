package it.model;


import java.util.Locale;

public enum TipoMovimento {
	APERTURA,
	PRELIEVO,
	VERSAMENTO,
	GIROCONTO,
	CHIUSURA_CONTO;
	
	public String toString() {
		return super.toString().toLowerCase(Locale.ROOT);
	}
}
