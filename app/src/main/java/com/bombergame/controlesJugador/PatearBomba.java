package com.bombergame.controlesJugador;

import com.bombergame.modelos.Jugador;


public class PatearBomba implements ControladorJugador {
    private Jugador jugador;

    public PatearBomba(Jugador jugador) {
        this.jugador = jugador;
    }

    @Override
    public void keyDown() {
        jugador.ordenPatearBomba();
    }

    @Override
    public void keyUp() {
        jugador.ordenFinPatearBomba();
    }

}