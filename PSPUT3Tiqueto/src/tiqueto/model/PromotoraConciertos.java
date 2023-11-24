package tiqueto.model;

import tiqueto.EjemploTicketMaster;

import java.util.concurrent.ThreadLocalRandom;

public class PromotoraConciertos extends Thread {

    final WebCompraConciertos webCompra;

    public PromotoraConciertos(WebCompraConciertos webCompra) {
        super();
        this.webCompra = webCompra;
    }

    @Override
    public void run() {

        // si queda entrada para reponer y las fans no han alcanzado su número máximo de comprar entrada (reposicion es par)
        while (EjemploTicketMaster.TOTAL_ENTRADAS_RESTANTE > 0 && EjemploTicketMaster.VECES_ENTRADA_FAN < EjemploTicketMaster.LIMITE_ENTRADA) {
            // para cuando reposicion es número impar
            if (EjemploTicketMaster.REPOSICION_ENTRADAS % 2 != 0 && EjemploTicketMaster.VECES_ENTRADA_FAN == EjemploTicketMaster.LIMITE_ENTRADA) {
                break;
            }

            if (EjemploTicketMaster.TOTAL_ENTRADAS_RESTANTE < EjemploTicketMaster.REPOSICION_ENTRADAS) {
                mensajePromotor("Preparo a reponer las entradas");
                int tiket1 = webCompra.reponerEntradas(EjemploTicketMaster.TOTAL_ENTRADAS_RESTANTE);
                mensajePromotor("repuso " + tiket1 + " entradas ");
                EjemploTicketMaster.TOTAL_ENTRADAS_RESTANTE = 0;
            } else {
                mensajePromotor("Preparo a reponer las entradas");
                int tiket2 = webCompra.reponerEntradas(EjemploTicketMaster.REPOSICION_ENTRADAS);
                mensajePromotor("repuso " + tiket2 + " entradas ");
                EjemploTicketMaster.TOTAL_ENTRADAS_RESTANTE = EjemploTicketMaster.TOTAL_ENTRADAS_RESTANTE - EjemploTicketMaster.REPOSICION_ENTRADAS;
            }

            mensajePromotor("promotor queda " + EjemploTicketMaster.TOTAL_ENTRADAS_RESTANTE + " ENTRADA");
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(3000, 8000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (EjemploTicketMaster.VECES_ENTRADA_FAN == EjemploTicketMaster.LIMITE_ENTRADA) {
            mensajePromotor("fans han gastado todos sus dineros, no hay nadie tiene dinero para comprar entrada");
        } else {
            mensajePromotor("no tengo más entrada para reponer");
        }

        // llama web para cerrar la venta
        webCompra.cerrarVenta();

    }

    /**
     * Método a usar para cada impresión por pantalla
     *
     * @param mensaje Mensaje que se quiere lanzar por pantalla
     */
    private void mensajePromotor(String mensaje) {
        System.out.println(System.currentTimeMillis() + "| Promotora: " + mensaje);

    }
}
