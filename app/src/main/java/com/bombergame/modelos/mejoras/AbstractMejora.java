package com.bombergame.modelos.mejoras;

import android.content.Context;
import android.graphics.Canvas;

import com.bombergame.graficos.Ar;
import com.bombergame.graficos.Sprite;
import com.bombergame.modelos.Jugador;
import com.bombergame.modelos.Modelo;

public abstract class AbstractMejora extends Modelo{

    protected Sprite sprite;

    public int tipo;
    public static final int BOMBA = 1;
    public static final int EXPLOSION = 2;
    public static final int VELOCIDAD_MOVIMIENTO = 3;
    public static final int EXPLOSION_DISTANCIA = 4;

    public AbstractMejora(Context context, double x, double y) {
        super(context, x, y, Ar.ancho(58), Ar.alto(58));
        setSprite();
    }

    protected abstract void setSprite();
    public abstract void mejorar(Jugador jugador);

    @Override
    protected void doDibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x, (int) y, false);
    }
}
