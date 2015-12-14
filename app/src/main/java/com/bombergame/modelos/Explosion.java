package com.bombergame.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.bombergame.R;
import com.bombergame.graficos.Ar;
import com.bombergame.graficos.Sprite;
import com.bombergame.modelos.mejoras.MejoraBomba;
import com.bombergame.modelos.mejoras.MejoraExplosion;
import com.bombergame.modelos.mejoras.MejoraVelocidad;

import java.util.Random;

/**
 * Created by Cristian on 09/12/2015.
 */
public class Explosion extends Modelo {

    private Sprite sprite;

    public static long tiempoExplosion = 3000;
    public int estado;
    public static int EXPLOTANDO = 0;
    public static int INACTIVA = 1;
//    public static int EXPLOTANDO_DESTRUIBLE = 2;
    private long tiempoFuego;
    private Nivel nivel;


    public Explosion(Context context, double x, double y, Nivel nivel) {
        super(context, x, y, Ar.ancho(58), Ar.alto(58));

        sprite = Sprite.create(context, R.drawable.flame, ancho, altura, 12, 5, true);
        estado = EXPLOTANDO;
        tiempoFuego = System.currentTimeMillis();
        this.nivel = nivel;
    }

    @Override
    public void actualizar(long tiempo) {
        sprite.actualizar(tiempo);
        if (estado == EXPLOTANDO && System.currentTimeMillis() - tiempoFuego >= tiempoExplosion) {
            estado = INACTIVA;
        }
    }



    @Override
    protected void doDibujar(Canvas canvas) {
        if (estado != INACTIVA)
            sprite.dibujarSprite(canvas, (int) x, (int) y, false);
    }
}