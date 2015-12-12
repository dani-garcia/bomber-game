package com.bombergame.modelos.mejoras;

import android.content.Context;
import android.graphics.Canvas;

import com.bombergame.R;
import com.bombergame.graficos.Ar;
import com.bombergame.graficos.Sprite;
import com.bombergame.modelos.Modelo;

/**
 * Created by Cristian on 11/12/2015.
 */
public abstract class Mejora extends Modelo {

    protected Sprite sprite;
    public int estado;
    public static int SIN_COGER = 0;
    public static int COGIDA = 1;

    public int tipo;
    public static int BOMBA = 1;
    public static int EXPLOSION = 2;
    public static int VELOCIDAD_MOVIMIENTO = 3;

    public Mejora(Context context, double x, double y) {
        super(context, x, y, Ar.ancho(58), Ar.alto(58));
        setSprite();
        estado = SIN_COGER;
    }

    public abstract void setSprite();

    @Override
    protected void doDibujar(Canvas canvas) {
        if (estado == SIN_COGER)
            sprite.dibujarSprite(canvas, (int) x, (int) y, false);
    }

}
