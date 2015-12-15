package com.bombergame.controlesJugador;

import com.bombergame.modelos.Jugador;

public class ExplotarBombas implements ControladorJugador {
    private Jugador jugador;

    public ExplotarBombas(Jugador jugador) {
        this.jugador = jugador;
    }

    @Override
    public void keyDown() {
        jugador.ordenExplotarBombas();
    }

    @Override
    public void keyUp() {
        jugador.ordenFinExplotarBombas();
    }

}