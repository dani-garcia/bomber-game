package com.bombergame.controlesJugador;

import com.bombergame.modelos.Jugador;

public class MoverJugadorDerecha implements ControladorJugaror{
    private Jugador jugador;
    public MoverJugadorDerecha(Jugador jugador) {
        this.jugador = jugador;
    }
    @Override
    public void keyDown() {
        jugador.ordenMovimientoDerecha();
    }

    @Override
    public void keyUp() {
        jugador.ordenFinMovimientoDerecha();
    }


}
