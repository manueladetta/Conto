package it.utils;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Utils {

	public static LocalDate getRandomData(int anno) {
        Random random = new Random();

		int mese = random.nextInt(11) + 1;
		int giorno =  0;
		
		List<Integer> mesi30 = Arrays.asList(Integer.valueOf(4), Integer.valueOf(6), Integer.valueOf(9), Integer.valueOf(11));
		List<Integer> mesi31 = Arrays.asList(Integer.valueOf(1), Integer.valueOf(3), Integer.valueOf(5), Integer.valueOf(7), Integer.valueOf(8), Integer.valueOf(10), Integer.valueOf(12));

		if(mesi30.contains(Integer.valueOf(mese))) { // mese da 30 giorni
			giorno = random.nextInt(29) + 1;
		} else if(mesi31.contains(Integer.valueOf(mese))) { // mese da 31 giorni
			giorno = random.nextInt(30) + 1;
		} else { // febbraio non bisestile
			giorno = random.nextInt(28) + 1;
		}
		return LocalDate.of(anno, mese, giorno);
	}
	
	public static LocalDate getRandomData(LocalDate data) {
        Random random = new Random();
        
        LocalDate oggi = LocalDate.now();
        
        int mese = random.nextInt(12 - data.getMonthValue()) + data.getMonthValue();
		int giorno =  0;
		
		List<Integer> mesi30 = Arrays.asList(4, 6, 9, 11);
	    List<Integer> mesi31 = Arrays.asList(1, 3, 5, 7, 8, 10, 12);
		
        if(data.getYear() == oggi.getYear() && data.getMonthValue() == oggi.getMonthValue()) {
        	mese = oggi.getMonthValue();
        	if(mesi30.contains(Integer.valueOf(mese))) { // mese da 30 giorni
    			if(mese == data.getMonthValue()) {
    				giorno = (random.nextInt(oggi.getDayOfMonth() - data.getDayOfMonth())) + data.getDayOfMonth();
    			} else {
    				giorno = random.nextInt(29) + 1;
    			}
    			
    		} else if(mesi31.contains(Integer.valueOf(mese))) { // mese da 31 giorni
    			if(mese == data.getMonthValue()) {
    				giorno = (random.nextInt(oggi.getDayOfMonth() - data.getDayOfMonth())) + data.getDayOfMonth();
    			} else {
    				giorno = random.nextInt(30) + 1;
    			}
    		} else { // febbraio non bisestile
    			if(mese == data.getMonthValue()) {
    				giorno = (random.nextInt(oggi.getDayOfMonth() - data.getDayOfMonth())) + data.getDayOfMonth();
    			} else {
    				giorno = random.nextInt(28) + 1;
    			}
    			
    		}
        } else {
        	if(mesi30.contains(Integer.valueOf(mese))) { // mese da 30 giorni
				if(mese == data.getMonthValue()) {
					giorno = (random.nextInt(30 - data.getMonthValue())) + data.getMonthValue();
				} else {
					giorno = random.nextInt(29) + 1;
				}
				
			} else if(mesi31.contains(Integer.valueOf(mese))) { // mese da 31 giorni
				if(mese == data.getMonthValue()) {
					giorno = (random.nextInt(31 - data.getMonthValue())) + data.getMonthValue();
				} else {
					giorno = random.nextInt(30) + 1;
				}
			} else { // febbraio non bisestile
				if(mese == data.getMonthValue()) {
					giorno = (random.nextInt(28 - data.getMonthValue())) + data.getMonthValue();
				} else {
					giorno = random.nextInt(28) + 1;
				}
				
			}
        }

		
		return LocalDate.of(data.getYear(), mese, giorno);
	}

	public static int calcolaNumeroGiorni(LocalDate start, LocalDate end) {
        return (int) ChronoUnit.DAYS.between(start, end);
	}
	
	public static String formattaCosto(double costo) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(costo);
	}
	
}
