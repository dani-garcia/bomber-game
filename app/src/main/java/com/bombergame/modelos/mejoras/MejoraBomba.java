package com.bombergame.modelos.mejoras;

import android.content.Context;

import com.bombergame.R;
import com.bombergame.graficos.Sprite;

/**
 * Created by Cristian on 11/12/2015.
 */
public class MejoraBomba extends Mejora{
    public MejoraBomba(Context context, double x, double y) {
        super(context, x, y);
    }

    @Override
    public void setSprite() {
        sprite = Sprite.create(context, R.drawable.powerup_bomb, ancho, altura, 12, 1, true);
    }

}
