package com.bombergame.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.bombergame.R;
import com.bombergame.gestores.CargadorGraficos;
import com.bombergame.graficos.Ar;
import com.bombergame.graficos.Sprite;
import com.bombergame.modelos.mejoras.Mejora;
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
    public static int FIN_EXPLOSION = 1;
    public static int EXPLOTANDO_DESTRUIBLE = 2;
    private long tiempoFuego;
    private Nivel nivel;
    private final int probabilidadNoMejora = 8; //A MAYOR probabilidad MENOR probabilidad hay de que al destruir un bloque salga una mejora


    public Explosion(Context context, double x, double y, Nivel nivel) {
        super(context, x, y, Ar.ancho(58), Ar.alto(58));

        sprite = Sprite.create(context, R.drawable.flame, ancho, altura, 12, 5, true);
        estado = EXPLOTANDO;
        tiempoFuego = System.currentTimeMillis();
        this.nivel = nivel;
    }

    public Explosion(Context context, double x, double y, Nivel nivel, int estado) {
        super(context, x, y, Ar.ancho(58), Ar.alto(58));
        this.estado = estado;
        tiempoFuego = System.currentTimeMillis();
        sprite = Sprite.create(context, R.drawable.flame, ancho, altura, 12, 5, true);
        this.nivel = nivel;
    }

    @Override
    public void actualizar(long tiempo) {
        //if(estado == EXPLOTANDO) //Solo se actualiza si está explotando
        sprite.actualizar(tiempo);
        if ((estado == EXPLOTANDO || estado == EXPLOTANDO_DESTRUIBLE) && System.currentTimeMillis() - tiempoFuego >= tiempoExplosion) {
            estado = FIN_EXPLOSION;
            int tileX = nivel.getTileXFromCoord(x);
            int tileY = nivel.getTileYFromCoord(y);
            if (nivel.getMapaTiles()[tileX][tileY].tipoColision == Tile.DESTRUIBLE) {
                System.out.println("Hay que dejar una mejora");
                dejarMejora();
                nivel.getMapaTiles()[tileX][tileY] = new Tile(null, Tile.PASABLE);
            }
        }
    }

    private void dejarMejora() {
        Random r = new Random();
        int n = r.nextInt(probabilidadNoMejora);
        switch (n) {
            case 0:
                nivel.mejoras.add(new MejoraBomba(context, x, y));
                break;
            case 1:
                nivel.mejoras.add(new MejoraExplosion(context, x, y));
                break;
            case 2:
                nivel.mejoras.add(new MejoraVelocidad(context, x, y));
                break;
        }
    }

    @Override
    protected void doDibujar(Canvas canvas) {
        if (estado == EXPLOTANDO || estado == EXPLOTANDO_DESTRUIBLE)
            sprite.dibujarSprite(canvas, (int) x, (int) y, false);
    }
}