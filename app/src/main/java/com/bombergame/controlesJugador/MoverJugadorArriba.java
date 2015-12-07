package com.bombergame.controlesJugador;

import com.bombergame.modelos.Jugador;

public class MoverJugadorArriba implements ControladorJugaror{
    private Jugador jugador;
    public MoverJugadorArriba(Jugador jugador) {
        this.jugador = jugador;
    }
    @Override
    public void keyDown() {
        jugador.ordenMovimientoArriba();
    }

    @Override
    public void keyUp() {
        jugador.ordenFinMovimientoArriba();
    }


}
