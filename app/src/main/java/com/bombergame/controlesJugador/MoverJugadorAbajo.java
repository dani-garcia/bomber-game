package com.bombergame.controlesJugador;

import com.bombergame.modelos.Jugador;

public class MoverJugadorAbajo implements ControladorJugador {
    private Jugador jugador;

    public MoverJugadorAbajo(Jugador jugador) {
        this.jugador = jugador;
    }

    @Override
    public void keyDown() {
        jugador.ordenMovimientoAbajo();
    }

    @Override
    public void keyUp() {
        jugador.ordenFinMovimientoAbajo();
    }


}
