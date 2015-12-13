package com.bombergame.controlesJugador;

import com.bombergame.modelos.Jugador;

public class PonerBomba implements ControladorJugador {
    private Jugador jugador;

    public PonerBomba (Jugador jugador) {
        this.jugador = jugador;
    }
    @Override
    public void keyDown() {
        jugador.ordenPonerBomba();
    }

    @Override
    public void keyUp() {
        jugador.ordenFinPonerBomba();
    }


}
