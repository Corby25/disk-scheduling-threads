import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SchedulingDisco extends Thread {
	private int numCilindri = 100;
	private double tempoSpostamento = 0.1;
	private int spostamenti;
	private Testina testina;
	private ArrayList<RichiestaIO> listaRichieste;
	private ArrayList<Integer> listaRichiesteEvase;
	private AlgoritmiTypes algoritmo;
	private RichiestaIO richiestaPiuVicina;
	
	public SchedulingDisco(Testina testina, AlgoritmiTypes algoritmo) {
		this.testina = testina;
		this.algoritmo = algoritmo;
		spostamenti = 0;
		listaRichieste = new ArrayList<RichiestaIO>();
		listaRichiesteEvase = new ArrayList<Integer>();
		richiestaPiuVicina = null;
		this.start();
	}
	
	public void aggiungiRichieste(RichiestaIO richiesta) {
		listaRichieste.add(richiesta);
	}
	
	public void run() {
		
			synchronized (this) {

				int posizioneCorrenteTestina = testina.getInizio();
				listaRichiesteEvase.add(posizioneCorrenteTestina);

				switch (algoritmo) {
				case FCFS:
					while (!listaRichieste.isEmpty()) {

						for (RichiestaIO tempRichiesta : listaRichieste) {
							tempRichiesta.start();
							listaRichiesteEvase.add(tempRichiesta.getCilindroRichiesto());
							try {
								tempRichiesta.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					break;
					
				case SSTF: 
					while (!listaRichieste.isEmpty()) {

						richiestaPiuVicina = trovaRichiestaPi�Vicina(posizioneCorrenteTestina);
						
						posizioneCorrenteTestina = richiestaPiuVicina.getCilindroRichiesto();
						
						richiestaPiuVicina.start();
						listaRichiesteEvase.add(richiestaPiuVicina.getCilindroRichiesto());
						try {
							richiestaPiuVicina.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						listaRichieste.remove(richiestaPiuVicina);
					}
					break;
					
				default:
					while (!listaRichieste.isEmpty()) {
						while (testina.getDirezione().equals("Destra")) {
							switch (algoritmo) {
							
							case SCAN:	
								richiestaPiuVicina = trovaRichiestaPi�VicinaDestra(posizioneCorrenteTestina);
								
								if (richiestaPiuVicina == null) {
									System.out.print("100 ");
									testina.setDirezione("Sinistra");
									break;
								}
								
								richiestaPiuVicina.start();
								listaRichiesteEvase.add(richiestaPiuVicina.getCilindroRichiesto());
								try {
									richiestaPiuVicina.join();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								listaRichieste.remove(richiestaPiuVicina);
								break;
								
							case LOOK:
								richiestaPiuVicina = trovaRichiestaPi�VicinaDestra(posizioneCorrenteTestina);
								
								if (richiestaPiuVicina == null) {
									testina.setDirezione("Sinistra");
									break;
								}
								
								richiestaPiuVicina.start();
								listaRichiesteEvase.add(richiestaPiuVicina.getCilindroRichiesto());
								try {
									richiestaPiuVicina.join();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								listaRichieste.remove(richiestaPiuVicina);
								break;
	
							default:
								System.out.println("Algoritmo non esistente");
								break;
							}
						}
						
						while (testina.getDirezione().equals("Sinistra")) {
							switch (algoritmo) {
							
							case SCAN:	
								richiestaPiuVicina = trovaRichiestaPi�VicinaSinistra(posizioneCorrenteTestina);
								
								if (richiestaPiuVicina == null) {
									System.out.print("0 ");
									testina.setDirezione("Destra");
									break;
								}
								
								richiestaPiuVicina.start();
								listaRichiesteEvase.add(richiestaPiuVicina.getCilindroRichiesto());
								try {
									richiestaPiuVicina.join();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								listaRichieste.remove(richiestaPiuVicina);
								break;
								
							case LOOK:
								richiestaPiuVicina = trovaRichiestaPi�VicinaSinistra(posizioneCorrenteTestina);
								
								if (richiestaPiuVicina == null) {
									testina.setDirezione("Destra");
									break;
								}
								
								richiestaPiuVicina.start();
								listaRichiesteEvase.add(richiestaPiuVicina.getCilindroRichiesto());
								try {
									richiestaPiuVicina.join();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								listaRichieste.remove(richiestaPiuVicina);
								break;
	
							default:
								System.out.println("Algoritmo non esistente");
								break;
							}
						}

				}
			}
		}
		
	}
	
	public int spostamentiTotali() {
		for (int i=1; i<listaRichiesteEvase.size(); i++) {
			spostamenti += Math.abs(listaRichiesteEvase.get(i)-listaRichiesteEvase.get(i-1));
		}
		return spostamenti;
	}
	
	public double getTempoSpostamento( ) {
		return tempoSpostamento;
	}
	
	public RichiestaIO trovaRichiestaPi�Vicina(int posizioneCorrenteTestina) {
		RichiestaIO richiestaPiuVicina = listaRichieste.get(0);
		int distanzaMinore = Math.abs(posizioneCorrenteTestina-richiestaPiuVicina.getCilindroRichiesto());

		for (RichiestaIO tempRichiesta : listaRichieste) {
			if (Math.abs(posizioneCorrenteTestina-tempRichiesta.getCilindroRichiesto()) < distanzaMinore) {
				distanzaMinore = Math.abs(posizioneCorrenteTestina-tempRichiesta.getCilindroRichiesto());
				richiestaPiuVicina = tempRichiesta;
			}
		}
		return richiestaPiuVicina;
	}
	
	public RichiestaIO trovaRichiestaPi�VicinaDestra(int posizioneCorrenteTestina) {
		int count=0;
		richiestaPiuVicina = null;
		for (RichiestaIO tempRichiesta : listaRichieste) {
			if (tempRichiesta.getCilindroRichiesto() > posizioneCorrenteTestina) {
				richiestaPiuVicina = tempRichiesta;
				count++;
				break;
			}
		}
		if (count == 0) return null;
		
		int distanzaMinore = Math.abs(posizioneCorrenteTestina-richiestaPiuVicina.getCilindroRichiesto());

		for (RichiestaIO tempRichiesta : listaRichieste) {
			if (tempRichiesta.getCilindroRichiesto() > posizioneCorrenteTestina && Math.abs(posizioneCorrenteTestina-tempRichiesta.getCilindroRichiesto()) < distanzaMinore) {
				distanzaMinore = Math.abs(posizioneCorrenteTestina-tempRichiesta.getCilindroRichiesto());
				richiestaPiuVicina = tempRichiesta;
			}
		}
		return richiestaPiuVicina;
	}
	
	public RichiestaIO trovaRichiestaPi�VicinaSinistra(int posizioneCorrenteTestina) {
		int count=0;
		richiestaPiuVicina = null;
		for (RichiestaIO tempRichiesta : listaRichieste) {
			if (tempRichiesta.getCilindroRichiesto() < posizioneCorrenteTestina) {
				richiestaPiuVicina = tempRichiesta;
				count++;
				break;
			}
		}
		if (count == 0) return null;
		
		int distanzaMinore = Math.abs(posizioneCorrenteTestina-richiestaPiuVicina.getCilindroRichiesto());

		for (RichiestaIO tempRichiesta : listaRichieste) {
			if (tempRichiesta.getCilindroRichiesto() < posizioneCorrenteTestina && Math.abs(posizioneCorrenteTestina-tempRichiesta.getCilindroRichiesto()) < distanzaMinore) {
				distanzaMinore = Math.abs(posizioneCorrenteTestina-tempRichiesta.getCilindroRichiesto());
				richiestaPiuVicina = tempRichiesta;
			}
		}
		return richiestaPiuVicina;
	}

}
