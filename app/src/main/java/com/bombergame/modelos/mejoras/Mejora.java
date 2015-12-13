package com.bombergame.modelos.mejoras;

import android.content.Context;
import android.graphics.Canvas;

import com.bombergame.graficos.Ar;
import com.bombergame.graficos.Sprite;
import com.bombergame.modelos.Modelo;

/**
 * Created by Cristian on 11/12/2015.
 */
public abstract class Mejora extends Modelo {

    protected Sprite sprite;

    public int tipo;
    public static final int BOMBA = 1;
    public static final int EXPLOSION = 2;
    public static final int VELOCIDAD_MOVIMIENTO = 3;

    public Mejora(Context context, double x, double y) {
        super(context, x, y, Ar.ancho(58), Ar.alto(58));
        setSprite();
    }

    public abstract void setSprite();

    @Override
    protected void doDibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x, (int) y, false);
    }
}
