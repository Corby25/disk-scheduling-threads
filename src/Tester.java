import java.util.ArrayList;

public class Tester {

	public static void main(String[] args) {
		Testina testina = new Testina(45, "Destra");
		SchedulingDisco scheduler = new SchedulingDisco(testina, AlgoritmiTypes.SSTF);
		
		ArrayList<RichiestaIO> richieste = new ArrayList<RichiestaIO>();
		for (int i=0; i<10; i++) {
			richieste.add(new RichiestaIO());
		}
		System.out.print("Lista Richieste:\n");
		for (RichiestaIO tempRichiesta : richieste) {
			scheduler.aggiungiRichieste(tempRichiesta);
			System.out.print(tempRichiesta.getCilindroRichiesto() + " ");
		}
		System.out.print("\n\nLista esecuzione richieste: \n");

		scheduler.run();
	}

}
