package com.bombergame.modelos.mejoras;

import android.content.Context;

import com.bombergame.R;
import com.bombergame.graficos.Sprite;
import com.bombergame.modelos.Jugador;

public class MejoraBomba extends AbstractMejora {
    public MejoraBomba(Context context, double x, double y) {
        super(context, x, y);
        tipo = AbstractMejora.BOMBA;
    }

    @Override
    protected void setSprite() {
        sprite = Sprite.create(context, R.drawable.powerup_bomb, ancho, altura, 12, 1, true);
    }

    @Override
    public void mejorar(Jugador jugador) {
        jugador.bombasLimite++;
    }

}
