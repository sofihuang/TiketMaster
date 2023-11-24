package tiqueto;

import java.util.ArrayList;
import java.util.List;

import tiqueto.model.FanGrupo;
import tiqueto.model.PromotoraConciertos;
import tiqueto.model.WebCompraConciertos;

public class EjemploTicketMaster {

	// Total de entradas que se vender�n
	public static int TOTAL_ENTRADAS = 10;// 10

	// El número de entradas que reponerá cada vez el promotor
	public static int REPOSICION_ENTRADAS = 2; // antes estaba 2

	// El número máximo de entradas por fan
	public static int MAX_ENTRADAS_POR_FAN =10; // 10

// hay que aplicar venta cerrada ************************************************

	// El número total de fans
	public static int NUM_FANS = 5; //5
	public static int TOTAL_ENTRADAS_RESTANTE = TOTAL_ENTRADAS;
	public static int VECES_ENTRADA_FAN=0;

	public static int LIMITE_ENTRADA=NUM_FANS*MAX_ENTRADAS_POR_FAN;

	public static void main(String[] args) throws InterruptedException {

		String mensajeInicial = "[ Empieza la venta de tickets. Se esperan %d fans, y un total de %d entradas ]";
		System.out.println(String.format(mensajeInicial, NUM_FANS, TOTAL_ENTRADAS_RESTANTE));
		WebCompraConciertos webCompra = new WebCompraConciertos();
		PromotoraConciertos liveNacion = new PromotoraConciertos(webCompra);
		List<FanGrupo> fans = new ArrayList<>();

		// Creamos todos los fans
		for (int numFan = 1; numFan <= NUM_FANS; numFan++) {
			FanGrupo fan = new FanGrupo(webCompra, numFan);
			fans.add(fan);
			fan.start();
		}

		//Lanzamos al promotor para que empiece a reponer entradas
		liveNacion.start();

		//Esperamos a que el promotor termine, para preguntar a los fans cu�ntas entradas tienen compradas
		liveNacion.join();

		System.out.println("\n [ Terminada la fase de venta - Sondeamos a pie de calle a los compradores ] \n");


		System.out.println("Total entradas ofertadas: " + TOTAL_ENTRADAS);
		System.out.println("Total entradas disponibles en la web: " + webCompra.entradasRestantes());

		// Les preguntamos a cada uno
		for (FanGrupo fan : fans) {
			fan.dimeEntradasCompradas();
		}
	}

}
