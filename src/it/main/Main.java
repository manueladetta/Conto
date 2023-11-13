package it.main;

import java.time.LocalDate;

import it.model.Conto;
import it.model.ContoCorrente;
import it.model.ContoDeposito;
import it.model.ContoInvestimento;

public class Main {

	public static void main(String[] args) {
		Conto deposito = new ContoDeposito("Manuela");
		Conto investimenti = new ContoInvestimento("Morena");
		Conto corrente = new ContoCorrente("Davide");
		
		// 2021
		deposito.effettuaOperazioniCasuale(3, deposito.getDataApertura());
		investimenti.effettuaOperazioniCasuale(3, investimenti.getDataApertura());
		corrente.effettuaOperazioniCasuale(3, corrente.getDataApertura());
		
		deposito.stampaEstrattoContoPDF(LocalDate.of(2021, 12, 31));
		investimenti.stampaEstrattoContoPDF(LocalDate.of(2021, 12, 31));
		corrente.stampaEstrattoContoPDF(LocalDate.of(2021, 12, 31));
		
		// 2022
		deposito.effettuaOperazioniCasuale(3, LocalDate.of(2022, 1, 1));
		investimenti.effettuaOperazioniCasuale(3, LocalDate.of(2022, 1, 1));
		corrente.effettuaOperazioniCasuale(3, LocalDate.of(2022, 1, 1));
		
		deposito.stampaEstrattoContoPDF(LocalDate.of(2022, 12, 31));
		investimenti.stampaEstrattoContoPDF(LocalDate.of(2022, 12, 31));
		corrente.stampaEstrattoContoPDF(LocalDate.of(2022, 12, 31));
		
		// 2023
		deposito.effettuaOperazioniCasuale(3, LocalDate.of(2023, 1, 1));
		investimenti.effettuaOperazioniCasuale(3, LocalDate.of(2023, 1, 1));
		corrente.effettuaOperazioniCasuale(3, LocalDate.of(2023, 1, 1));
		
		deposito.stampaEstrattoContoPDF(LocalDate.now());
		investimenti.stampaEstrattoContoPDF(LocalDate.now());
		corrente.stampaEstrattoContoPDF(LocalDate.now());

	}

}
