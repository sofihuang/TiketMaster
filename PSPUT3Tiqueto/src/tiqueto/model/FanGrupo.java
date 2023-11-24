package tiqueto.model;

import tiqueto.EjemploTicketMaster;

import java.util.concurrent.ThreadLocalRandom;

public class FanGrupo extends Thread {

	final WebCompraConciertos webCompra;
	int numeroFan;
	private String tabuladores = "\t\t\t\t";
	int entradasCompradas = 0;

	public FanGrupo(WebCompraConciertos web, int numeroFan) {
		super();
		this.numeroFan = numeroFan;
		this.webCompra = web;
	}

	@Override
	public void run() {
		mensajeFan("soy "+this.numeroFan+" saludo");

		while (EjemploTicketMaster.MAX_ENTRADAS_POR_FAN > this.entradasCompradas && !webCompra.VENTA_CERRADA) {
			if (webCompra.hayEntradas()) {  // Agregado: Verificar si hay entradas disponibles
				mensajeFan("soy " + this.numeroFan + " preparo para comprar");
				if (webCompra.comprarEntrada()) {
					entradasCompradas++;
					mensajeFan("soy " + this.numeroFan + " he conseguido de comprar una entrada, ahora tengo "+this.entradasCompradas+" entradas");
					try {
						Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
					} catch (InterruptedException e) {
						System.out.println("no se puede comprar entrada" + e);
					}
				}
			}
		}
		mensajeFan("La compra se ha cerrado...");
	}

	public void dimeEntradasCompradas() {
		mensajeFan("Sólo he conseguido: " + entradasCompradas);
	}
	
	/**
	 * Método a usar para cada impresión por pantalla
	 * @param mensaje Mensaje que se quiere lanzar por pantalla
	 */
	private void mensajeFan(String mensaje) {
		System.out.println(System.currentTimeMillis() + "|" + tabuladores +" Fan "+this.numeroFan +": " + mensaje);
	}
}
