import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SchedulingDisco extends Thread {
	private int numCilindri = 100;
	private Testina testina;
	private ArrayList<RichiestaIO> listaRichieste;
	private AlgoritmiTypes algoritmo;
	
	public SchedulingDisco(File richieste, AlgoritmiTypes algoritmo) {
		listaRichieste = new ArrayList<RichiestaIO>();
		this.algoritmo = algoritmo;
		this.start();
	}
	
	public void aggiungiRichieste(RichiestaIO richiesta) {
		listaRichieste.add(richiesta);
	}
	
	public void run() {
		while (!listaRichieste.isEmpty()) {
			synchronized (this) {
				switch (algoritmo) {
				case FCFS:
					for (RichiestaIO tempRichiesta : listaRichieste) {
						tempRichiesta.start();
						try {
							tempRichiesta.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						listaRichieste.remove(tempRichiesta);
					}
					break;
					
				case SSTF: 
					RichiestaIO richiestaPiuVicina = listaRichieste.get(0);
					int posizioneCorrenteTestina = testina.getInizio();
					int distanzaMinore = Math.abs(posizioneCorrenteTestina-richiestaPiuVicina.getCilindroRichiesto());

					for (RichiestaIO tempRichiesta : listaRichieste) {
						if (Math.abs(posizioneCorrenteTestina-tempRichiesta.getCilindroRichiesto()) < distanzaMinore) {
							distanzaMinore = Math.abs(posizioneCorrenteTestina-tempRichiesta.getCilindroRichiesto());
							richiestaPiuVicina = tempRichiesta;
						}
					}
					richiestaPiuVicina.start();
					try {
						richiestaPiuVicina.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					listaRichieste.remove(richiestaPiuVicina);
					break;
					
				default:
					if (testina.getDirezione().equals("Destra")) {
						switch (algoritmo) {
						case SCAN:
							
							break;

						default:
							break;
						}
					}
					else {
						
					}
					break;
				}
			}
			
		}
		
	}

}
