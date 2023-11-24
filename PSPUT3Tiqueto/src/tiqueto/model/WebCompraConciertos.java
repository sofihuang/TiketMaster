package tiqueto.model;

import tiqueto.EjemploTicketMaster;
import tiqueto.IOperacionesWeb;

import java.util.concurrent.ThreadLocalRandom;

public class WebCompraConciertos implements IOperacionesWeb {

    public int TIKET_DISPONIBLE = 0;

    public boolean VENTA_CERRADA = false;

    public WebCompraConciertos() {
        super();
    }

    @Override
    public synchronized boolean comprarEntrada() {
       if (!hayEntradas() && !VENTA_CERRADA) {
            try {
                mensajeWeb("Fan está esperando a que reponga la entrada");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (hayEntradas()) {
            TIKET_DISPONIBLE--;
            EjemploTicketMaster.VECES_ENTRADA_FAN++;
            mensajeWeb("Fan ha comprado una entrada, quedan " + TIKET_DISPONIBLE + " tickets disponibles");
            notifyAll();
            return true;
        }
        return false;
    }


    @Override
    public synchronized int reponerEntradas(int numeroEntradas) {
        if (hayEntradas() || VENTA_CERRADA) {
            mensajeWeb("promotor espera a que se agota la entrada");
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        TIKET_DISPONIBLE = numeroEntradas;
        mensajeWeb("se han repuesto " + TIKET_DISPONIBLE + " entradas disponibles");

        notifyAll();

        return TIKET_DISPONIBLE;
    }


    @Override
    public synchronized void cerrarVenta() {
        this.VENTA_CERRADA = true;
        notifyAll();
        mensajeWeb("CERRAR LA VENTA, NO HAY MÁS TIKETS");
    }


    @Override
    public synchronized boolean hayEntradas() {
        return TIKET_DISPONIBLE > 0;
    }


    @Override
    public synchronized int entradasRestantes() {
        if(EjemploTicketMaster.VECES_ENTRADA_FAN <= EjemploTicketMaster.LIMITE_ENTRADA && EjemploTicketMaster.REPOSICION_ENTRADAS %2 == 0) {  //caso si reposicion es número par
            return TIKET_DISPONIBLE + EjemploTicketMaster.TOTAL_ENTRADAS_RESTANTE ;

        }else if(EjemploTicketMaster.VECES_ENTRADA_FAN == EjemploTicketMaster.LIMITE_ENTRADA && EjemploTicketMaster.REPOSICION_ENTRADAS %2 != 0){  //caso si reposicion es número impar
           return  TIKET_DISPONIBLE + EjemploTicketMaster.TOTAL_ENTRADAS_RESTANTE;

        }

        return TIKET_DISPONIBLE;
    }


    /**
     * Método a usar para cada impresión por pantalla
     *
     * @param mensaje Mensaje que se quiere lanzar por pantalla
     */
    private void mensajeWeb(String mensaje) {
        System.out.println(System.currentTimeMillis() + "| WebCompra: " + mensaje);

    }

}
