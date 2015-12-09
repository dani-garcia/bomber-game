package com.bombergame.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.bombergame.R;
import com.bombergame.graficos.Ar;
import com.bombergame.graficos.Sprite;

import java.util.HashMap;

/**
 * Created by Cristian on 08/12/2015.
 */
public class Bomba extends Modelo{

    private Sprite sprite;

    double x;
    double y;
    Jugador jugador;

    public static int EXPLOSION_REALIZADA = 2;
    public static int EXPLOTADA = 1;
    public static int PUESTA = 0;
    public int estado;

    long tiempoPuesta;


    public Bomba(Context context, Jugador jugador) {
        super(context, 0, 0, Ar.ancho(48), Ar.alto(48));

        this.x = jugador.getX();
        this.y = jugador.getY() + 35;
        this.jugador = jugador;
        estado = PUESTA;
        sprite = Sprite.create(context, R.drawable.bomb, ancho, altura, 1, 3, true);
        tiempoPuesta = System.currentTimeMillis();
        jugador.bombasColocadas++;
    }

    @Override
    public void actualizar(long tiempo) {
        sprite.actualizar(tiempo);
        if(estado == PUESTA && System.currentTimeMillis() - tiempoPuesta >=3000) {
            explotar();
            tiempoPuesta = System.currentTimeMillis();
        }
        else if(estado == EXPLOTADA && System.currentTimeMillis() - tiempoPuesta >=3000){
            estado = EXPLOSION_REALIZADA;
            jugador.bombasColocadas--;
        }
    }

    @Override
    protected void doDibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x, (int) y, false);
    }

    public void explotar(){
        estado = EXPLOTADA;
        sprite = Sprite.create(context, R.drawable.flame, ancho, altura, 12, 5, true);
    }
}
