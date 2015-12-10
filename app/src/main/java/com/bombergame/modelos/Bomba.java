package com.bombergame.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.bombergame.R;
import com.bombergame.graficos.Ar;
import com.bombergame.graficos.Sprite;

import java.util.List;

/**
 * Created by Cristian on 08/12/2015.
 */
public class Bomba extends Modelo{

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
        if(estado == PUESTA && System.currentTimeMillis() - tiempoPuesta >= duracionBomba) {
            estado = EXPLOTANDO;
            generarExplosiones();
            tiempoPuesta = System.currentTimeMillis();
        } else if(estado == EXPLOTANDO && System.currentTimeMillis() - tiempoPuesta >= Explosion.tiempoExplosion){
            estado = INACTIVA;
            jugador.bombasColocadas--;
        }

    }

    @Override
    protected void doDibujar(Canvas canvas) {
        if(estado == PUESTA)
            sprite.dibujarSprite(canvas, (int) x, (int) y, false);
    }

    private void generarExplosiones(){
        int tileX = nivel.getTileXFromCoord(x);
        int tileY = nivel.getTileYFromCoord(y);
        nivel.explosiones.add(new Explosion(context, x, y, nivel));
        generarExplosionesEjeXPositivo(tileX, tileY);
        generarExplosionesEjeXNegativo(tileX, tileY);
        generarExplosionesEjeYPositivo(tileX, tileY);
        generaExplosionesEjeYNegativo(tileX, tileY);

    }

    private void generarExplosionesEjeXPositivo(int tileX, int tileY){
        boolean existeMuro = false;
        for(int i=1; i<=jugador.alcanceBombas; i++){
            if (!existeMuro && nivel.getMapaTiles()[tileX + i][tileY].tipoColision == Tile.PASABLE){
                nivel.explosiones.add(new Explosion(context, x + Tile.ancho * i, y, nivel));
            } else {
                existeMuro = true;
            }
        }
    }

    private void generarExplosionesEjeXNegativo(int tileX, int tileY){
        boolean existeMuro = false;
        for(int i=1; i<=jugador.alcanceBombas; i++){
            if (!existeMuro && nivel.getMapaTiles()[tileX - i][tileY].tipoColision == Tile.PASABLE){
                nivel.explosiones.add(new Explosion(context, x - Tile.ancho * i, y, nivel));
            } else {
                existeMuro = true;
            }
        }
    }

    private void generarExplosionesEjeYPositivo(int tileX, int tileY){
        boolean existeMuro = false;
        for(int i=1; i<=jugador.alcanceBombas; i++){
            if (!existeMuro && nivel.getMapaTiles()[tileX][tileY + i].tipoColision == Tile.PASABLE){
                nivel.explosiones.add(new Explosion(context, x, y + Tile.altura * i, nivel));
            } else {
                existeMuro = true;
            }
        }
    }

    private void generaExplosionesEjeYNegativo(int tileX, int tileY){
        boolean existeMuro = false;
        for(int i=1; i<=jugador.alcanceBombas; i++){
            if (!existeMuro && nivel.getMapaTiles()[tileX][tileY - i].tipoColision == Tile.PASABLE){
                nivel.explosiones.add(new Explosion(context, x, y - Tile.altura * i, nivel));
            } else {
                existeMuro = true;
            }
        }
    }


}
