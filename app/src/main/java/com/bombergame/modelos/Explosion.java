package com.bombergame.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.bombergame.R;
import com.bombergame.graficos.Ar;
import com.bombergame.graficos.Sprite;

/**
 * Created by Cristian on 09/12/2015.
 */
public class Explosion extends Modelo{

    private Sprite sprite;

    public static long tiempoExplosion = 3000;
    public int estado;
    public static int EXPLOTANDO = 0;
    public static int FIN_EXPLOSION = 1;
    private long tiempoFuego;


    public Explosion(Context context, double x, double y, Nivel nivel) {
        super(context, x, y, Ar.ancho(58), Ar.alto(58));

        sprite = Sprite.create(context, R.drawable.flame, ancho, altura, 12, 5, true);
        estado = EXPLOTANDO;
        tiempoFuego = System.currentTimeMillis();
    }

    @Override
    public void actualizar(long tiempo) {
        sprite.actualizar(tiempo);
        if(estado == EXPLOTANDO && System.currentTimeMillis() - tiempoFuego >= tiempoExplosion){
            estado = FIN_EXPLOSION;
        }
    }

    @Override
    protected void doDibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x, (int) y, false);
    }
}
