package it.model;

import java.time.LocalDate;

public class Movimento {

	private TipoMovimento tipo;
	private double importo, saldoDopoOperazione;
	private LocalDate data;
	/**
	 * @param tipo
	 * @param importo
	 * @param saldoDopoOperazione
	 * @param data
	 */
	public Movimento(TipoMovimento tipo, double importo, double saldoDopoOperazione, LocalDate data) {
		this.tipo = tipo;
		this.importo = importo;
		this.saldoDopoOperazione = saldoDopoOperazione;
		this.data = data;
	}
	public Movimento() {
		
	}
	
	
//	public TipoMovimento getTipo() {
//		return tipo;
//	}
	public String getTipo() {
		return tipo.toString();
	}
	public void setTipo(TipoMovimento tipo) {
		this.tipo = tipo;
	}
	public void setTipo(String tipo) {
		switch(tipo) {
		case "P":
			this.tipo = TipoMovimento.PRELIEVO;
			break;
		case "V":
			this.tipo = TipoMovimento.VERSAMENTO;
			break;
		case "C":
			this.tipo = TipoMovimento.CHIUSURA_CONTO;
			break;
		case "G":
			this.tipo = TipoMovimento.GIROCONTO;
			break;
		default:
			break;
		}
	}
	public double getImporto() {
		return importo;
	}
	public void setImporto(double importo) {
		this.importo = importo;
	}
	public double getSaldoDopoOperazione() {
		return saldoDopoOperazione;
	}
	public void setSaldoDopoOperazione(double saldoDopoOperazione) {
		this.saldoDopoOperazione = saldoDopoOperazione;
	}
	public LocalDate getData() {
		return data;
	}
	public void setData(LocalDate data) {
		this.data = data;
	}
	
	
}
