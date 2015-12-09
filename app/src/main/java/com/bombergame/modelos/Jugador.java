package com.bombergame.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.bombergame.R;
import com.bombergame.graficos.Ar;
import com.bombergame.graficos.Sprite;

import java.util.HashMap;

public class Jugador extends Modelo {
    private static final String CAMINANDO_IZQUIERDA = "Caminando_izquierda";
    private static final String CAMINANDO_DERECHA = "Caminando_derecha";
    private static final String CAMINANDO_ARRIBA = "Caminando_arriba";
    private static final String CAMINANDO_ABAJO = "Caminando_abajo";

    private int idJugador;

    private Sprite sprite;
    private final HashMap<String, Sprite> sprites = new HashMap<>();

    public static final int IZQUIERDA = 1;
    public static final int DERECHA = 2;
    public static final int ARRIBA = 3;
    public static final int ABAJO = 4;

    double xInicial;
    double yInicial;

    public int orientacion;

    public double velocidadMovimiento = 5f;
    public double aMover;
    public boolean movimiento;

    public int bombasLimite;
    public int bombasColocadas;
    public int velocidad;
    public int alcanceBombas;
    public boolean patearBombas;
    public boolean explosionRemota;

    private boolean moverArriba;
    private boolean moverAbajo;
    private boolean moverIzquierda;
    private boolean moverDerecha;
    private boolean ponerBomba;
    public boolean patearBomba;
    public boolean explotarBombas;

//    public int vidas;
//    public double msInmunidad = 0;
//    public boolean golpeado = false;

    public Jugador(Context context, double xInicial, double yInicial, int idJugador) {
        super(context, 0, 0, Ar.ancho(115), Ar.alto(62));

        this.xInicial = xInicial;
        this.yInicial = yInicial - altura / 2;

        this.x = this.xInicial;
        this.y = this.yInicial;

        cIzquierda = cDerecha = Ar.ancho(20);
        cArriba = Ar.alto(-5);
        cAbajo = Ar.alto(45);

        orientacion = ABAJO;
//        vidas = 3;


        this.idJugador = idJugador;

        bombasLimite = 1;
        bombasColocadas = 0;
        velocidad = 1;
        alcanceBombas = 1;
        patearBombas = false;
        explosionRemota = false;

        inicializar();
    }

    public int getId() {
        return idJugador;
    }

    private void inicializar() {
        sprites.put(CAMINANDO_DERECHA, Sprite.create(context, R.drawable.bman_right, ancho, altura, 12, 8, true));
        sprites.put(CAMINANDO_IZQUIERDA, Sprite.create(context, R.drawable.bman_left, ancho, altura, 12, 8, true));

        sprites.put(CAMINANDO_ARRIBA, Sprite.create(context, R.drawable.bman_up, ancho, altura, 12, 8, true));
        sprites.put(CAMINANDO_ABAJO, Sprite.create(context, R.drawable.bman_down, ancho, altura, 12, 8, true));

        // animación actual
        sprite = sprites.get(CAMINANDO_ABAJO);
    }

    public void procesarOrdenes() {
        // Si no nos estamos moviendo, y nos envian un movimiento, nos movemos
        if (!movimiento) {
            if (moverAbajo) {
                movimiento = true;
                aMover = Ar.alto(Tile.altura);
                sprite =sprites.get(CAMINANDO_ABAJO);
                orientacion = ABAJO;
            }else if (moverArriba){
                movimiento = true;
                aMover = Ar.alto(Tile.altura);
                sprite =sprites.get(CAMINANDO_ARRIBA);
                orientacion = ARRIBA;
            } else if (moverDerecha) {
                movimiento = true;
                aMover = Ar.ancho(Tile.ancho);
                sprite =sprites.get(CAMINANDO_DERECHA);
                orientacion = DERECHA;
            } else if (moverIzquierda) {
                movimiento = true;
                aMover = Ar.ancho(Tile.ancho);
                sprite =sprites.get(CAMINANDO_IZQUIERDA);
                orientacion = IZQUIERDA;
            }
            /*// Solo nos movemos en un eje, asi que cojemos el que tenga un valor mayor
            if (Math.abs(orientacionPadX) > Math.abs(orientacionPadY)) {
                aMover = Ar.ancho(Tile.ancho);

                if (orientacionPadX > 0) {
                    sprite = sprites.get(CAMINANDO_DERECHA);
                    orientacion = DERECHA;
                } else if (orientacionPadX < 0) {
                    sprite = sprites.get(CAMINANDO_IZQUIERDA);
                    orientacion = IZQUIERDA;
                }
            } else {
                aMover = Ar.alto(Tile.altura);

                if (orientacionPadY > 0) {
                    sprite = sprites.get(CAMINANDO_ABAJO);
                    orientacion = ABAJO;
                } else if (orientacionPadY < 0) {
                    sprite = sprites.get(CAMINANDO_ARRIBA);
                    orientacion = ARRIBA;
                }
            }*/
        }
    }

    @Override
    public void actualizar(long tiempo) {
//        if (msInmunidad > 0) {
//            msInmunidad -= tiempo;
//        }

        if (movimiento)
            sprite.actualizar(tiempo);
    }

    public int golpeado() {
//        if (msInmunidad <= 0) {
//            if (vidas > 0) {
//                vidas--;
//                msInmunidad = 3000;
//                golpeado = true;
//            }
//        }
//        return vidas;
        return 0;
    }

    @Override
    protected void doDibujar(Canvas canvas) {
//        sprite.dibujarSprite(canvas, (int) x, (int) y, msInmunidad > 0);
        sprite.dibujarSprite(canvas, (int) x, (int) y, false);
    }

    public void restablecerPosicionInicial() {
        this.x = xInicial;
        this.y = yInicial;

//        vidas = 3;
//        golpeado = false;
//        msInmunidad = 0;

        sprite = sprites.get(CAMINANDO_DERECHA);
    }

    public void ordenMovimientoAbajo() {
        moverAbajo = true;
        moverArriba = false;
        moverDerecha = false;
        moverIzquierda = false;
    }
    public void ordenFinMovimientoAbajo() {
        moverAbajo = false;
    }

    public void ordenMovimientoArriba() {
        moverAbajo = false;
        moverArriba = true;
        moverDerecha = false;
        moverIzquierda = false;
    }
    public void ordenFinMovimientoArriba() {
        moverArriba = false;
    }

    public void ordenMovimientoDerecha() {
        moverAbajo = false;
        moverArriba = false;
        moverDerecha = true;
        moverIzquierda = false;

    }
    public void ordenFinMovimientoDerecha() {
        moverDerecha = false;
    }

    public void ordenMovimientoIzquierda() {
        moverAbajo = false;
        moverArriba = false;
        moverDerecha = false;
        moverIzquierda = true;
    }
    public void ordenFinMovimientoIzquierda() {
        moverIzquierda = false;
    }

    public void ordenPonerBomba() {
        ponerBomba = true;
    }
    public void ordenFinPonerBomba() {
        ponerBomba = false;
    }
}
