package com.bombergame.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import com.bombergame.R;
import com.bombergame.graficos.Ar;
import com.bombergame.graficos.Sprite;
import com.bombergame.modelos.mejoras.MejoraBomba;
import com.bombergame.modelos.mejoras.MejoraExplosion;
import com.bombergame.modelos.mejoras.MejoraVelocidad;

import java.util.Random;

/**
 * Created by Cristian on 08/12/2015.
 */
public class Bomba extends Modelo {

    private Sprite sprite;

    Jugador jugador;

    public static int INACTIVA = 2;
    public static int EXPLOTANDO = 1;
    public static int PUESTA = 0;
    public int estado;
    public Nivel nivel;
    public int duracionBomba = 3000;
    long tiempoPuesta;


    public Bomba(Context context, Jugador jugador, Nivel nivel) {
        super(context, Ar.x(nivel.getTileXFromCoord(jugador.x) * Tile.ancho + Tile.ancho / 2)
                , Ar.y(nivel.getTileYFromCoord(jugador.y) * Tile.altura + Tile.altura / 2),
                Ar.ancho(48),
                Ar.alto(48));
        this.nivel = nivel;
        this.jugador = jugador;
        estado = PUESTA;
        sprite = Sprite.create(context, R.drawable.bomb, ancho, altura, 1, 3, true);
        tiempoPuesta = System.currentTimeMillis();
        jugador.bombasColocadas++;
    }

    @Override
    public void actualizar(long tiempo) {
        sprite.actualizar(tiempo);
        if (estado == PUESTA && System.currentTimeMillis() - tiempoPuesta >= duracionBomba) {
            estado = EXPLOTANDO;
            generarExplosiones();
            tiempoPuesta = System.currentTimeMillis();
        } else if (estado == EXPLOTANDO && System.currentTimeMillis() - tiempoPuesta >= Explosion.tiempoExplosion) { //Cuando acaba la explosion
            estado = INACTIVA;                                                                                     //se libera un hueco de bomba
            jugador.bombasColocadas--;                                                                             //para el jugador
        }

    }

    @Override
    protected void doDibujar(Canvas canvas) {
        if (estado == PUESTA)
            sprite.dibujarSprite(canvas, (int) x, (int) y, false);
    }

    private void generarExplosiones() {
        int tileX = nivel.getTileXFromCoord(x);
        int tileY = nivel.getTileYFromCoord(y);
        nivel.explosiones.add(new Explosion(context, x, y, nivel)); //Esta explosión es donde se pone la bomba, por lo tanto siempre se crea
        generarExplosionesEnEje(tileX, tileY, 1, 0);
        generarExplosionesEnEje(tileX, tileY, -1, 0);
        generarExplosionesEnEje(tileX, tileY, 0, 1);
        generarExplosionesEnEje(tileX, tileY, 0, -1);

    }

    private void generarExplosionesEnEje(int xTileOrigen, int yTileOrigen, int xAxisOffset, int yAxisOffset) {
        boolean muroEncontrado = false;

        for (int i = 1; i <= jugador.alcanceBombas && muroEncontrado == false; i++) {
            int xTile = xTileOrigen + xAxisOffset * i;
            int yTile = yTileOrigen + yAxisOffset * i;
            double xCoord = Ar.x((xTile * Tile.ancho) + Tile.ancho / 2);
            double yCoord = Ar.y((yTile * Tile.altura) + Tile.altura / 2);
            Log.e("BOMBA", "Comprobando explosión en Tile: (" + xTile + ", " + yTile+ "), coords: (" + xCoord + ", " + yCoord + ")");
            Tile nextTileChecked = nivel.getMapaTiles()[xTile][yTile];
            if (nextTileChecked.tipoColision == Tile.PASABLE) {
                nivel.explosiones.add(new Explosion(context, xCoord, yCoord, nivel));
            } else if (nextTileChecked.tipoColision == Tile.DESTRUIBLE) {
                muroEncontrado = true;
                nivel.getMapaTiles()[xTile][yTile] = Tile.VACIO;
                generarMejora(xCoord, yCoord);
            } else {
                muroEncontrado = true;
            }
        }
    }

    private void generarMejora(double x, double y) {
        Random r = new Random();
        int probabilidadNoMejora = 8; //A MAYOR probabilidad MENOR probabilidad hay de que al destruir un bloque salga una mejora
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
}
