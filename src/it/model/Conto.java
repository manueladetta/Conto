package it.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import it.utils.Utils;

public abstract class Conto {
	
	private final int annoInizio = 2021;
	
    private static final String DIRECTORY_PATH = "EstrattoConto"; // Nome della sottocartella

	private static final double RITENUTA = 0.26;
	
	protected static final Logger logger = LogManager.getLogger("Conto");

	protected String titolare;
	protected LocalDate dataApertura;
	protected double saldo;
	protected List<Movimento> operazioni;
	
	// Costruttore
	public Conto() {
		super();
	}
	public Conto(String titolare) {
		super();
		this.titolare = titolare;
		this.dataApertura = Utils.getRandomData(2021);
		this.saldo = 1000;
		this.operazioni = new ArrayList<>();
        this.operazioni.add(new Movimento(TipoMovimento.APERTURA, this.saldo, this.saldo, this.dataApertura));
	}

	// Getters and setters
	public String getTitolare() {
		return titolare;
	}
	public void setTitolare(String titolare) {
		this.titolare = titolare;
	}

	public LocalDate getDataApertura() {
		return dataApertura;
	}
	public void setDataApertura(LocalDate dataApertura) {
		this.dataApertura = dataApertura;
	}

	public double getSaldo() {
		return saldo;
	}
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	public List<Movimento> getOperazioni() {
		return operazioni;
	}
	public void setOperazioni(List<Movimento> operazioni) {
		this.operazioni = operazioni;
	}
	
	// Metodo per effettuare un prelievo
	public void preleva(double importo) {
    	this.saldo = this.saldo - importo;
	}
	
	// Metodo per effettuare un versamento
	public void versa(double importo) {
    	this.saldo = this.saldo + importo;
	}
	
	// Metodo wrapper per generare il movimento generico
	public void generaMovimento(double importo, String tipo, LocalDate data) {
		Movimento movimento = new Movimento();
    	movimento.setTipo(tipo);
    	movimento.setImporto(importo);
    	movimento.setData(data);
    	switch(tipo) {
    	case "P":
    		this.preleva(importo);
    		break;
    	case "V":
    		this.versa(importo);
    		break;
    	}    	
    	movimento.setSaldoDopoOperazione(this.saldo);
    	this.operazioni.add(movimento);
    	Collections.sort(operazioni, new Comparator<Movimento>() {
            public int compare(Movimento m1, Movimento m2) {
                return m1.getData().compareTo(m2.getData());
            }
        });
    	this.setOperazioni(operazioni);
	}
	
	// Metodo per il calcolo degli interessi per l'anno passato come parametro
	public abstract double generaInteressi(int anno);
	
	// Metodo che aggiorna il saldo aggiungendo i relativi interessi netti
	private void aggiungiInteressi(double importo, LocalDate data) {
		Movimento movimento = new Movimento();
    	movimento.setTipo("G");
    	movimento.setImporto(importo);
    	movimento.setData(data);
    	this.versa(importo);
    	movimento.setSaldoDopoOperazione(this.saldo);
    	this.operazioni.add(movimento);
//    	Collections.sort(operazioni, new Comparator<Movimento>() {
//            public int compare(Movimento m1, Movimento m2) {
//                return m1.getData().compareTo(m2.getData());
//            }
//        });
	}

	// Metodo di supporto che calcola l'ammontare degli interessi in un determinato anno
	public double calcolaInteressi(int anno, double tassoInteresse) {
		logger.info(this.getClass().getCanonicalName() + " - Interessi anno: " + anno + ", Tasso annuo: " + (tassoInteresse*365) + ", Tasso giornaliero: " + tassoInteresse);
		 double interessi = 0, saldoPostOperazione = saldo;
			int giorni = 0;
			LocalDate start;
			if(anno == annoInizio) {
				start = dataApertura; // del conto
			} else {
				start = LocalDate.of(anno,  1, 1); // 1° gennaio dell'anno preso in considerazione
			}
			for(Movimento m : operazioni) {
				if(m.getData().getYear() == anno) {
					giorni = Utils.calcolaNumeroGiorni(start, m.getData());
//					interessi = interessi + (m.getSaldoDopoOperazione() * tassoInteresse * giorni);
					
					logger.info("Date: " + start.toString() + " - " + m.getData().toString() + " --> Numero giorni: " + giorni);
					logger.info("Interessi del periodo: " + Utils.formattaCosto((m.getSaldoDopoOperazione() + interessi) * tassoInteresse * giorni));
					
					interessi = interessi + ((m.getSaldoDopoOperazione() + interessi) * tassoInteresse * giorni);
//					if(interessi > 0) {
//						this.saldo += interessi;
//					} else if(getClass().getCanonicalName() == "it.model.ContoInvestimento") {
//						this.saldo -= interessi;
//					}
					
					start = m.getData(); // aggiorno la data di riferimento per il calcolo degli interessi
					saldoPostOperazione = m.getSaldoDopoOperazione() + interessi;
				}
			}
			
			// calcolo gli interessi fino al 31/12 se anno != da anno corrente altrimenti fino al gionro odierno
			if(LocalDate.now().getYear() != anno) {
				giorni = Utils.calcolaNumeroGiorni(start, LocalDate.of(anno,  12, 31));
				interessi = interessi + (saldoPostOperazione * tassoInteresse * giorni);
			} else {
				giorni = Utils.calcolaNumeroGiorni(start, LocalDate.now());
				interessi = interessi + (saldoPostOperazione * tassoInteresse * giorni);
			}
			
			if(interessi <= 0 && getClass().getCanonicalName() != "it.model.ContoInvestimento") {
				return 0;
			}
			
			return interessi;
	}
	
	// Metodo per il calcolo degli interessi netti
	private double calcolaInteressiNetti(double interessiLordi) {
		return interessiLordi - (interessiLordi * RITENUTA);
	}
	
	// Metodo per creare n movimenti randomici sul conto
	public void effettuaOperazioniCasuale(int numeroMovimenti, LocalDate data) {
		Random random = new Random();
		LocalDate dataOperazione = Utils.getRandomData(data);
		
		for(int i = 0; i < numeroMovimenti; i++) {
			int sec = random.nextInt(100);
            String tipo = sec % 2 == 0 ? "P" : "V"; // pari == prelievo; versamento altrimenti
            
            int importo = random.nextInt(5000);
            
            this.generaMovimento(importo, tipo, dataOperazione);    
            
            dataOperazione = Utils.getRandomData(dataOperazione);
		}
	}
	
	// Metodo per stampare l'estratto conto in PDF
	public void stampaEstrattoContoPDF(LocalDate data) {
		try {
			String nomeDocumentoPDF = DIRECTORY_PATH + "/EC_" + titolare + "_" + data + ".pdf";
			Document document = creaDocumento();
			PdfWriter.getInstance(document, new FileOutputStream(nomeDocumentoPDF));
			document.open();
			
			// Intestazione
			aggiungiParagrafo(document, new String("Estratto Conto Finale | Correntista: " + this.titolare + " | Data: " + data));
			aggiungiParagrafo(document, "Tipo conto: " + trovaTipoConto());
			
	        document.add(Chunk.NEWLINE);
			
			aggiungiTabellaMovimenti(document, data);
			
			double interessiLordi = this.generaInteressi(data.getYear());
			
			double interessiNetti = calcolaInteressiNetti(interessiLordi);
			
			aggiungiParagrafo(document, new String("Interessi lordi: " + Utils.formattaCosto(interessiLordi)));
			aggiungiParagrafo(document, new String("Interessi netti: " + Utils.formattaCosto(interessiNetti)));
			
			aggiungiInteressi(interessiNetti, data);
			
			aggiungiParagrafo(document, new String("Saldo finale: " + Utils.formattaCosto(this.saldo)));

	        document.close();	
	        
	        System.out.println("Documento " + nomeDocumentoPDF + " creato con successo");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
	}
	
	// Metodo che restituisce il tipo di conto in maniera presentabile all'utente
	private String trovaTipoConto() {
		String tipo = "";
		
		switch(getClass().getCanonicalName()) {
		case "it.model.ContoCorrente":
			tipo = "Conto corrente";
			break;
		case "it.model.ContoDeposito":
			tipo = "Conto deposito";
			break;
		case "it.model.ContoInvestimento":
			tipo = "Conto investimento";
			break;
		default:
			break;
		}
		
		return tipo;
	}
	
	// Metodo che itera per i vari movimenti effettuati sul conto in un determinato anno e li aggiunge al documento in formato tabellare
	private void aggiungiTabellaMovimenti(Document document, LocalDate data) throws DocumentException {
        PdfPTable table = new PdfPTable(4);  // 4 colonne
        
        aggiungiIntestazioneTabella(table);
        
        for (Movimento movimento : operazioni) {
        	if(movimento.getData().getYear() == data.getYear()) {
        		aggiungiCella(table, new String(movimento.getData().toString()));
	    		aggiungiCella(table, new String(movimento.getTipo()));
	    		aggiungiCella(table, Utils.formattaCosto(movimento.getImporto()));
	    		aggiungiCella(table, Utils.formattaCosto(movimento.getSaldoDopoOperazione()));
        	}
        }
        
        document.add(table); 
        
	}
	
	// Metodo per aggiungere la singola cella all'interno di una tabella
	private void aggiungiCella(PdfPTable tabella, String testo) {
		PdfPCell cell = new PdfPCell(new Paragraph(testo));
        tabella.addCell(cell);
	}
	
	// Metodo che si occupa di creare l'intestazione della tabella(
	private void aggiungiIntestazioneTabella(PdfPTable table) {
		aggiungiCella(table, new String("Data"));
        aggiungiCella(table, new String("Tipo"));
        aggiungiCella(table, new String("Importo"));
        aggiungiCella(table, new String("Saldo dopo operazione"));
	}
	
	// Metodo per aggiungere un paragrafo ad un documento
	private void aggiungiParagrafo(Document document, String testo) throws DocumentException {
		document.add(new Paragraph(testo));
	}
	
	// Metodo che genera e restituisce il documento desiderato
	private Document creaDocumento() {
		Document document = new Document();
		
		File directory = new File(DIRECTORY_PATH);
         
        if (!directory.exists()) {
            directory.mkdir(); // Crea la sottocartella se non esiste
        }
                  
        return document;
	}
	
}
