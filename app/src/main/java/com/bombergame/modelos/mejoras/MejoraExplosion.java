package com.bombergame.modelos.mejoras;

import android.content.Context;

import com.bombergame.R;
import com.bombergame.graficos.Sprite;
import com.bombergame.modelos.Jugador;

public class MejoraExplosion extends AbstractMejora {
    public MejoraExplosion(Context context, double x, double y) {
        super(context, x, y);
        tipo = AbstractMejora.EXPLOSION;
    }

    @Override
    protected void setSprite() {
        sprite = Sprite.create(context, R.drawable.powerup_fire, ancho, altura, 12, 1, true);
    }

    @Override
    public void mejorar(Jugador jugador) {
        jugador.alcanceBombas++;
    }
}
