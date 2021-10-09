
public class RichiestaIO extends Thread{
	private int cilindroRichiesto;
	
	public RichiestaIO() {
		cilindroRichiesto = (int)(Math.random()*100);
	}

	public int getCilindroRichiesto() {
		return cilindroRichiesto;
	}

	public void setCilindroRichiesto(int cilindroRichiesto) {
		this.cilindroRichiesto = cilindroRichiesto;
	}
	
	public void run() {
		System.out.print(cilindroRichiesto + " ");
	}

}
