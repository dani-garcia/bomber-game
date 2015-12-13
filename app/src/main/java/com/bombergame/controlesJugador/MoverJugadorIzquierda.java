package com.bombergame.controlesJugador;

import com.bombergame.modelos.Jugador;

public class MoverJugadorIzquierda implements ControladorJugador {
    private Jugador jugador;
    public MoverJugadorIzquierda(Jugador jugador) {
        this.jugador = jugador;
    }
    @Override
    public void keyDown() {
        jugador.ordenMovimientoIzquierda();
    }

    @Override
    public void keyUp() {
        jugador.ordenFinMovimientoIzquierda();
    }


}
