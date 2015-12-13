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

    public int orientacion = ABAJO;

    public double velocidadMovimiento = 5f;
    public double aMover;
    public boolean movimiento;

    public int bombasLimite = 1;
    public int bombasColocadas;
    public int alcanceBombas = 1;

    private boolean moverArriba;
    private boolean moverAbajo;
    private boolean moverIzquierda;
    private boolean moverDerecha;
    private boolean ponerBomba;

    private long msInmunidad;
    private int vidas = VIDAS_INICIALES;
    private static final int VIDAS_INICIALES = 3;

    public Jugador(Context context, double xInicial, double yInicial, int idJugador) {
        super(context, xInicial, yInicial, Ar.ancho(115), Ar.alto(62));
        this.xInicial = xInicial;
        this.yInicial = yInicial;

        this.idJugador = idJugador;

        cIzquierda = cDerecha = Ar.ancho(20);
        cArriba = Ar.alto(25);
        cAbajo = Ar.alto(25);

        inicializar();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getId() {
        return idJugador;
    }

    private void inicializar() {
        sprites.put(CAMINANDO_DERECHA, Sprite.create(context, R.drawable.bman_right1 + (idJugador - 1), ancho, altura, 12, 8, true));
        sprites.put(CAMINANDO_IZQUIERDA, Sprite.create(context, R.drawable.bman_left1 + (idJugador - 1), ancho, altura, 12, 8, true));

        sprites.put(CAMINANDO_ARRIBA, Sprite.create(context, R.drawable.bman_up1 + (idJugador - 1), ancho, altura, 12, 8, true));
        sprites.put(CAMINANDO_ABAJO, Sprite.create(context, R.drawable.bman_down1 + (idJugador - 1), ancho, altura, 12, 8, true));

        // animación actual
        sprite = sprites.get(CAMINANDO_ABAJO);
    }

    public void procesarOrdenes(Nivel nivel) {
        // Si no nos estamos moviendo, y nos envian un movimiento, nos movemos
        if (!movimiento) {
            if (moverAbajo) {
                movimiento = true;
                aMover = Ar.alto(Tile.altura);
                sprite = sprites.get(CAMINANDO_ABAJO);
                orientacion = ABAJO;
            } else if (moverArriba) {
                movimiento = true;
                aMover = Ar.alto(Tile.altura);
                sprite = sprites.get(CAMINANDO_ARRIBA);
                orientacion = ARRIBA;
            } else if (moverDerecha) {
                movimiento = true;
                aMover = Ar.ancho(Tile.ancho);
                sprite = sprites.get(CAMINANDO_DERECHA);
                orientacion = DERECHA;
            } else if (moverIzquierda) {
                movimiento = true;
                aMover = Ar.ancho(Tile.ancho);
                sprite = sprites.get(CAMINANDO_IZQUIERDA);
                orientacion = IZQUIERDA;
            }
        }

        if (ponerBomba) {
            ponerBomba = false;

            if (bombasColocadas < bombasLimite) {
                Bomba b = new Bomba(context, this, nivel);
                nivel.bombas.add(b);
            }
        }
    }

    @Override
    public void actualizar(long tiempo) {
        if (msInmunidad > 0) {
            msInmunidad -= tiempo;
        }

        if (movimiento)
            sprite.actualizar(tiempo);
    }

    public int golpeado() {
        if (msInmunidad > 0)
            return vidas; // No se golpea

        if (--vidas > 0) {
            // Queda vidas
            msInmunidad = 2000; // Dos segundos de inmunidad
            restablecerPosicionInicial();
        }

        return vidas;
    }

    public int getVidas() {
        return vidas;
    }

    @Override
    protected void doDibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x, (int) y - Tile.altura / 2, msInmunidad > 0);
    }

    public void restablecerPosicionInicial() {
        this.x = xInicial;
        this.y = yInicial;

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
