package com.bombergame.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.bombergame.R;
import com.bombergame.gestores.CargadorGraficos;
import com.bombergame.graficos.Ar;
import com.bombergame.graficos.Sprite;

import java.util.List;

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
    private int duracionBomba = 3000;
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
        generarExplosionesEjeXPositivo(tileX, tileY);
        generarExplosionesEjeXNegativo(tileX, tileY);
        generarExplosionesEjeYPositivo(tileX, tileY);
        generaExplosionesEjeYNegativo(tileX, tileY);

    }

    private void generarExplosionesEjeXPositivo(int tileX, int tileY) {
        boolean existeMuro = false;
        for (int i = 1; i <= jugador.alcanceBombas; i++) { //Se recorre el eje X tantas casillas a la derecha como lo indique el alcance del jugador
            if (!existeMuro && nivel.getMapaTiles()[tileX + i][tileY].tipoColision == Tile.PASABLE) {//Si es una casilla pasable y para llegar a ella no
                nivel.explosiones.add(new Explosion(context, x + Tile.ancho * i, y, nivel));        //existe ningún muro por el medio, generamos la explosion
            } else {                                                                                //de forma normal. Si no es un tile pasable, comprobamos si es destruible
                if (!existeMuro && nivel.getMapaTiles()[tileX + i][tileY].tipoColision == Tile.DESTRUIBLE)  //y, si es así, se crea una explosion que va a destruir el bloque y generar una mejora.
                    nivel.explosiones.add(new Explosion(context, x + Tile.ancho * i, y, nivel, Explosion.EXPLOTANDO_DESTRUIBLE));//Si no es un tile ni pasable ni destruible, es de tipo muro y no hay explosión
                existeMuro = true;
            }
        }
    }

    private void generarExplosionesEjeXNegativo(int tileX, int tileY) {
        boolean existeMuro = false;
        for (int i = 1; i <= jugador.alcanceBombas; i++) { //Se recorre el eje X tantas casillas a la izquierda como lo indique el alcance del jugador
            if (!existeMuro && nivel.getMapaTiles()[tileX - i][tileY].tipoColision == Tile.PASABLE) {
                nivel.explosiones.add(new Explosion(context, x - Tile.ancho * i, y, nivel));
            } else {
                if (!existeMuro && nivel.getMapaTiles()[tileX - i][tileY].tipoColision == Tile.DESTRUIBLE)
                    nivel.explosiones.add(new Explosion(context, x - Tile.ancho * i, y, nivel, Tile.DESTRUIBLE));
                existeMuro = true;
            }
        }
    }

    private void generarExplosionesEjeYPositivo(int tileX, int tileY) {
        boolean existeMuro = false;
        for (int i = 1; i <= jugador.alcanceBombas; i++) { //Se recorre el eje Y tantas casillas hacia arriba como lo indique el alcance del jugador
            if (!existeMuro && nivel.getMapaTiles()[tileX][tileY + i].tipoColision == Tile.PASABLE) {
                nivel.explosiones.add(new Explosion(context, x, y + Tile.altura * i, nivel));
            } else {
                if (!existeMuro && nivel.getMapaTiles()[tileX][tileY + i].tipoColision == Tile.DESTRUIBLE)
                    nivel.explosiones.add(new Explosion(context, x, y + Tile.altura * i, nivel, Tile.DESTRUIBLE));
                existeMuro = true;
            }
        }
    }

    private void generaExplosionesEjeYNegativo(int tileX, int tileY) {
        boolean existeMuro = false;
        for (int i = 1; i <= jugador.alcanceBombas; i++) { //Se recorre el eje Y tantas casillas a abajo como lo indique el alcance del jugador
            if (!existeMuro && nivel.getMapaTiles()[tileX][tileY - i].tipoColision == Tile.PASABLE) {
                nivel.explosiones.add(new Explosion(context, x, y - Tile.altura * i, nivel));
            } else {
                if (!existeMuro && nivel.getMapaTiles()[tileX][tileY - i].tipoColision == Tile.DESTRUIBLE)
                    nivel.explosiones.add(new Explosion(context, x, y - Tile.altura * i, nivel, Tile.DESTRUIBLE));
                existeMuro = true;
            }
        }
    }
}
