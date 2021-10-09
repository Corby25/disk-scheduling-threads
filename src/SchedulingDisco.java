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
	private RichiestaIO richiestaPiuVicina;
	
	public SchedulingDisco(Testina testina, AlgoritmiTypes algoritmo) {
		this.testina = testina;
		this.algoritmo = algoritmo;
		listaRichieste = new ArrayList<RichiestaIO>();
		richiestaPiuVicina = null;
		this.start();
	}
	
	public void aggiungiRichieste(RichiestaIO richiesta) {
		listaRichieste.add(richiesta);
	}
	
	public void run() {
		
			synchronized (this) {

				int posizioneCorrenteTestina = testina.getInizio();

				switch (algoritmo) {
				case FCFS:
					while (!listaRichieste.isEmpty()) {

						for (RichiestaIO tempRichiesta : listaRichieste) {
							tempRichiesta.start();
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

						richiestaPiuVicina = trovaRichiestaPiùVicina(posizioneCorrenteTestina);
						
						posizioneCorrenteTestina = richiestaPiuVicina.getCilindroRichiesto();
						
						richiestaPiuVicina.start();
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
								richiestaPiuVicina = trovaRichiestaPiùVicinaDestra(posizioneCorrenteTestina);
								
								if (richiestaPiuVicina == null) {
									System.out.print("100 ");
									testina.setDirezione("Sinistra");
									break;
								}
								
								richiestaPiuVicina.start();
								try {
									richiestaPiuVicina.join();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								listaRichieste.remove(richiestaPiuVicina);
								break;
								
							case LOOK:
								richiestaPiuVicina = trovaRichiestaPiùVicinaDestra(posizioneCorrenteTestina);
								
								if (richiestaPiuVicina == null) {
									testina.setDirezione("Sinistra");
									break;
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
								System.out.println("Algoritmo non esistente");
								break;
							}
						}
						
						while (testina.getDirezione().equals("Sinistra")) {
							switch (algoritmo) {
							
							case SCAN:	
								richiestaPiuVicina = trovaRichiestaPiùVicinaSinistra(posizioneCorrenteTestina);
								
								if (richiestaPiuVicina == null) {
									System.out.print("0 ");
									testina.setDirezione("Destra");
									break;
								}
								
								richiestaPiuVicina.start();
								try {
									richiestaPiuVicina.join();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								listaRichieste.remove(richiestaPiuVicina);
								break;
								
							case LOOK:
								richiestaPiuVicina = trovaRichiestaPiùVicinaSinistra(posizioneCorrenteTestina);
								
								if (richiestaPiuVicina == null) {
									testina.setDirezione("Destra");
									break;
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
								System.out.println("Algoritmo non esistente");
								break;
							}
						}

				}
			}
			
		}
		
	}
	
	public RichiestaIO trovaRichiestaPiùVicina(int posizioneCorrenteTestina) {
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
	
	public RichiestaIO trovaRichiestaPiùVicinaDestra(int posizioneCorrenteTestina) {
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
	
	public RichiestaIO trovaRichiestaPiùVicinaSinistra(int posizioneCorrenteTestina) {
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
