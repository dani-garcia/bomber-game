package com.bombergame.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.bombergame.R;
import com.bombergame.graficos.Ar;
import com.bombergame.graficos.Sprite;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Cristian on 12/12/2015.
 */
public class Enemigo extends Modelo{

    private String ultimoSentido = "";
    private static final String CAMINANDO_IZQUIERDA = "Caminando_izquierda";
    private static final String CAMINANDO_DERECHA = "Caminando_derecha";
    private static final String CAMINANDO_ARRIBA = "Caminando_arriba";
    private static final String CAMINANDO_ABAJO = "Caminando_abajo";

    public int orientacion;

    public static final int IZQUIERDA = 1;
    public static final int DERECHA = 2;
    public static final int ARRIBA = 3;
    public static final int ABAJO = 4;
    public double aMover;

    private Sprite sprite;
    private final HashMap<String, Sprite> sprites = new HashMap<>();

    public double velocidadMovimiento = 3f;
    public boolean movimiento;

    public Enemigo(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, Ar.ancho(115), Ar.alto(62));

        cIzquierda = cDerecha = Ar.ancho(20);
        cArriba = Ar.alto(25);
        cAbajo = Ar.alto(25);

        inicializar();
        asignarSentidoOrientacion(true, true, true, true);
    }

    private void inicializar(){
        sprites.put(CAMINANDO_DERECHA, Sprite.create(context, R.drawable.enemy_right, ancho, altura, 11, 7, true));
        sprites.put(CAMINANDO_IZQUIERDA, Sprite.create(context, R.drawable.enemy_left, ancho, altura, 11, 7, true));

        sprites.put(CAMINANDO_ARRIBA, Sprite.create(context, R.drawable.enemy_up, ancho, altura, 10, 6, true));
        sprites.put(CAMINANDO_ABAJO, Sprite.create(context, R.drawable.enemy_down, ancho, altura, 10, 6, true));
    }

    @Override
    public void actualizar(long tiempo) {
        sprite.actualizar(tiempo);
    }

    @Override
    protected void doDibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x, (int) y - Tile.altura / 2, false);
    }

    public void mover(Nivel nivel) {
        // Si no nos estamos moviendo, y nos envian un movimiento, nos movemos
        int tileX = nivel.getTileXFromCoord(x);
        int tileY = nivel.getTileYFromCoord(y);
        Tile tileSuperior = nivel.getMapaTiles()[tileX][tileY + 1];
        Tile tileInferior = nivel.getMapaTiles()[tileX][tileY - 1];
        Tile tileDerecho = nivel.getMapaTiles()[tileX + 1][tileY];
        Tile tileIzquierdo = nivel.getMapaTiles()[tileX - 1][tileY];

        double paso = Math.min(aMover, velocidadMovimiento);
        aMover -= paso;

        switch (orientacion) {
            case ARRIBA:
                if (tileInferior.tipoColision == Tile.PASABLE) {
                    y -= paso;
                    aMover = Ar.alto(Tile.altura);
                }
                break;
            case ABAJO:
                if (tileSuperior.tipoColision == Tile.PASABLE) {
                    y += paso;
                    aMover = Ar.alto(Tile.altura);
                }
                break;
            case IZQUIERDA:
                if (tileIzquierdo.tipoColision == Tile.PASABLE) {
                    x -= paso;
                    aMover = Ar.alto(Tile.altura);
                }
                break;
            case DERECHA:
                if (tileDerecho.tipoColision == Tile.PASABLE) {
                    x += paso;
                    aMover = Ar.alto(Tile.altura);
                }
                break;
        }

        actualizarMovimiento(tileSuperior, tileInferior, tileDerecho, tileIzquierdo);
    }

    public void actualizarMovimiento(Tile superior, Tile inferior, Tile derecho, Tile izquierdo){
        boolean abajo = true;
        boolean arriba = true;
        boolean derecha = true;
        boolean izquierda = true;

        boolean nuevaRuta = false;

        if(ultimoSentido.equals(CAMINANDO_ABAJO) && inferior.tipoColision != Tile.PASABLE) {
            nuevaRuta = true;
        }
        else if(ultimoSentido.equals(CAMINANDO_ARRIBA) && superior.tipoColision != Tile.PASABLE){
            nuevaRuta = true;
        }
        else if(ultimoSentido.equals(CAMINANDO_DERECHA) && derecho.tipoColision != Tile.PASABLE){
            nuevaRuta = true;
        }
        else if(ultimoSentido.equals(CAMINANDO_IZQUIERDA) && izquierdo.tipoColision != Tile.PASABLE){
            nuevaRuta = true;
        }

        if(nuevaRuta){
            if(inferior.tipoColision != Tile.PASABLE)
                abajo = false;
            if(superior.tipoColision != Tile.PASABLE)
                arriba = false;
            if(derecho.tipoColision != Tile.PASABLE)
                derecha = false;
            if(izquierdo.tipoColision != Tile.PASABLE)
                izquierda = false;
            asignarSentidoOrientacion(arriba, abajo, derecha, izquierda);
        }

    }


    public void asignarSentidoOrientacion(boolean arriba, boolean abajo, boolean derecha, boolean izquierda){
        boolean encontrada = false;
        Random r = new Random();
        while(!encontrada){

            int num = r.nextInt(4);

            switch (num){
                case 0:
                    if(arriba){
                        sprite = sprites.get(CAMINANDO_ARRIBA);
                        ultimoSentido = CAMINANDO_ARRIBA;
                        orientacion = ARRIBA;
                        aMover = Ar.alto(Tile.altura);
                        encontrada = true;
                    }
                    break;
                case 1:
                    if(abajo){
                        sprite = sprites.get(CAMINANDO_ABAJO);
                        ultimoSentido = CAMINANDO_ABAJO;
                        orientacion = ABAJO;
                        aMover = Ar.alto(Tile.altura);
                        encontrada = true;
                    }
                    break;
                case 2:
                    if(derecha){
                        sprite = sprites.get(CAMINANDO_DERECHA);
                        ultimoSentido = CAMINANDO_DERECHA;
                        orientacion = DERECHA;
                        aMover = Ar.alto(Tile.ancho);
                        encontrada = true;
                    }
                    break;
                case 3:
                    if(izquierda){
                        sprite = sprites.get(CAMINANDO_IZQUIERDA);
                        ultimoSentido = CAMINANDO_IZQUIERDA;
                        orientacion = IZQUIERDA;
                        aMover = Ar.alto(Tile.ancho);
                        encontrada = true;
                    }
                    break;

            }
        }
    }
}
