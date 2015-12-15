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
public class Enemigo extends Modelo {

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

    public Enemigo(Context context, double xInicial, double yInicial) {
        super(context, xInicial, yInicial, Ar.ancho(115), Ar.alto(62));

        cIzquierda = cDerecha = Ar.ancho(20);
        cArriba = Ar.alto(25);
        cAbajo = Ar.alto(25);

        inicializar();
    }

    private void inicializar() {
        sprites.put(CAMINANDO_DERECHA, Sprite.create(context, R.drawable.enemy_right, ancho, altura, 11, 7, true));
        sprites.put(CAMINANDO_IZQUIERDA, Sprite.create(context, R.drawable.enemy_left, ancho, altura, 11, 7, true));

        sprites.put(CAMINANDO_ARRIBA, Sprite.create(context, R.drawable.enemy_up, ancho, altura, 10, 6, true));
        sprites.put(CAMINANDO_ABAJO, Sprite.create(context, R.drawable.enemy_down, ancho, altura, 10, 6, true));

        sprite = sprites.get(CAMINANDO_ABAJO);
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
        if (aMover > 0) {
            double paso = Math.min(aMover, velocidadMovimiento);
            aMover -= paso;

            switch (orientacion) {
                case ARRIBA:
                    y -= paso;
                    break;

                case ABAJO:
                    y += paso;
                    break;

                case IZQUIERDA:
                    x -= paso;
                    break;

                case DERECHA:
                    x += paso;
                    break;
            }
        } else {
            int tileX = nivel.getTileXFromCoord(x);
            int tileY = nivel.getTileYFromCoord(y);
            Tile tileSuperior = nivel.getMapaTiles()[tileX][tileY - 1];
            Tile tileInferior = nivel.getMapaTiles()[tileX][tileY + 1];
            Tile tileDerecho = nivel.getMapaTiles()[tileX + 1][tileY];
            Tile tileIzquierdo = nivel.getMapaTiles()[tileX - 1][tileY];

            // Fin del movimiento y recalculamos
            actualizarMovimiento(tileSuperior, tileInferior, tileDerecho, tileIzquierdo);
        }
    }

    /**
     * Método que calcula si es necesaria una nueva ruta para que el enemigo continue su movimiento por el mapa
     *
     * @param superior
     * @param inferior
     * @param derecho
     * @param izquierdo
     */
    public void actualizarMovimiento(Tile superior, Tile inferior, Tile derecho, Tile izquierdo) {
        boolean abajo = inferior.tipoColision == Tile.PASABLE;
        boolean arriba = superior.tipoColision == Tile.PASABLE;
        boolean derecha = derecho.tipoColision == Tile.PASABLE;
        boolean izquierda = izquierdo.tipoColision == Tile.PASABLE;

        // TODO Puede que nos interese de vez en cuando cambiar la direccion de movimiento, para que el movimiento quede un poco mejor
        // Pseudocodigo
        //  if (random() > 0.8) { // 20% de posibilidad
        //      asignarSentidoOrientacion(arriba, abajo, derecha, izquierda);
        //      return;
        //  }

        // Si la dirección en la que íbamos sigue pasable, continuamos
        if (ultimoSentido.equals(CAMINANDO_ABAJO) && abajo) {
            aMover = Ar.alto(Tile.altura);

        } else if (ultimoSentido.equals(CAMINANDO_ARRIBA) && arriba) {
            aMover = Ar.alto(Tile.altura);

        } else if (ultimoSentido.equals(CAMINANDO_DERECHA) && derecha) {
            aMover = Ar.alto(Tile.ancho);

        } else if (ultimoSentido.equals(CAMINANDO_IZQUIERDA) && izquierda) {
            aMover = Ar.alto(Tile.ancho);
        } else {

            // Si esta bloqueada, obtenemos una nueva
            asignarSentidoOrientacion(arriba, abajo, derecha, izquierda);
        }
    }


    /**
     * Método que calcula de forma aleatoria la nueva orientación y el nuevo sentido, de entre los posible
     * según lo indique los parámetros, del enemigo
     *
     * @param arriba
     * @param abajo
     * @param derecha
     * @param izquierda
     */
    public void asignarSentidoOrientacion(boolean arriba, boolean abajo, boolean derecha, boolean izquierda) {
        boolean encontrada = false;
        Random r = new Random();
        while (!encontrada) {

            int num = r.nextInt(4);

            switch (num) {
                case 0:
                    if (arriba) {
                        sprite = sprites.get(CAMINANDO_ARRIBA);
                        ultimoSentido = CAMINANDO_ARRIBA;
                        orientacion = ARRIBA;
                        aMover = Ar.alto(Tile.altura);
                        encontrada = true;
                    }
                    break;
                case 1:
                    if (abajo) {
                        sprite = sprites.get(CAMINANDO_ABAJO);
                        ultimoSentido = CAMINANDO_ABAJO;
                        orientacion = ABAJO;
                        aMover = Ar.alto(Tile.altura);
                        encontrada = true;
                    }
                    break;
                case 2:
                    if (derecha) {
                        sprite = sprites.get(CAMINANDO_DERECHA);
                        ultimoSentido = CAMINANDO_DERECHA;
                        orientacion = DERECHA;
                        aMover = Ar.alto(Tile.ancho);
                        encontrada = true;
                    }
                    break;
                case 3:
                    if (izquierda) {
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
