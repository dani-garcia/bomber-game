package com.bombergame.modelos.mejoras;

import android.content.Context;

import com.bombergame.R;
import com.bombergame.graficos.Sprite;
import com.bombergame.modelos.Jugador;

public class MejoraVelocidad extends AbstractMejora {
    public MejoraVelocidad(Context context, double x, double y) {
        super(context, x, y);
        tipo = AbstractMejora.VELOCIDAD_MOVIMIENTO;
    }

    @Override
    protected void setSprite() {
        sprite = Sprite.create(context, R.drawable.powerup_speed, ancho, altura, 12, 1, true);
    }

    @Override
    public void mejorar(Jugador jugador) {
        if ( jugador.buffosVelocidad < jugador.buffosVelocidadMaximos) jugador.buffosVelocidad++;
    }
}
