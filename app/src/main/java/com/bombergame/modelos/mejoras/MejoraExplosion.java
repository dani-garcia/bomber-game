package com.bombergame.modelos.mejoras;

import android.content.Context;

import com.bombergame.R;
import com.bombergame.graficos.Sprite;

/**
 * Created by Cristian on 11/12/2015.
 */
public class MejoraExplosion extends Mejora {
    public MejoraExplosion(Context context, double x, double y) {
        super(context, x, y);
        tipo = Mejora.EXPLOSION;
    }

    @Override
    public void setSprite() {
        sprite = Sprite.create(context, R.drawable.powerup_fire, ancho, altura, 12, 1, true);
    }
}
