package com.bombergame.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.bombergame.R;
import com.bombergame.graficos.Ar;
import com.bombergame.graficos.Sprite;
import com.bombergame.modelos.controles.Marcador;

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

    // Indicadores de buffos
    public int bombasLimite = 1;
    public int bombasColocadas;
    public int alcanceBombas = 1;
    public int buffosVelodidad = 3;
    public boolean buffoPateaBombas = true;
    public boolean buffoExplosionDistanciaBombas = false;

    private boolean moverArriba;
    private boolean moverAbajo;
    private boolean moverIzquierda;
    private boolean moverDerecha;
    private boolean ponerBomba;
    private boolean pateaBomba;
    private boolean explosionDistancia;

    private long msInmunidad;
    private int vidas = VIDAS_INICIALES;
    private static final int VIDAS_INICIALES = 3;

    private Marcador marcador;

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
            int tileX = nivel.getTileXFromCoord(x);
            int tileY = nivel.getTileYFromCoord(y);

            if (moverAbajo) {
                sprite = sprites.get(CAMINANDO_ABAJO);
                orientacion = ABAJO;

                if (nivel.getMapaTiles()[tileX][tileY + 1].tipoColision == Tile.PASABLE &&
                        nivel.getBombaEnTile(tileX, tileY + 1) == null) {
                    movimiento = true;
                    aMover = Ar.alto(Tile.altura);
                }

            } else if (moverArriba) {
                sprite = sprites.get(CAMINANDO_ARRIBA);
                orientacion = ARRIBA;

                if (nivel.getMapaTiles()[tileX][tileY - 1].tipoColision == Tile.PASABLE &&
                        nivel.getBombaEnTile(tileX, tileY - 1) == null) {
                    movimiento = true;
                    aMover = Ar.alto(Tile.altura);
                }

            } else if (moverDerecha) {
                sprite = sprites.get(CAMINANDO_DERECHA);
                orientacion = DERECHA;

                if (nivel.getMapaTiles()[tileX + 1][tileY].tipoColision == Tile.PASABLE &&
                        nivel.getBombaEnTile(tileX + 1, tileY) == null) {
                    movimiento = true;
                    aMover = Ar.ancho(Tile.ancho);
                }

            } else if (moverIzquierda) {
                sprite = sprites.get(CAMINANDO_IZQUIERDA);
                orientacion = IZQUIERDA;

                if (nivel.getMapaTiles()[tileX - 1][tileY].tipoColision == Tile.PASABLE &&
                        nivel.getBombaEnTile(tileX - 1, tileY) == null) {
                    movimiento = true;
                    aMover = Ar.ancho(Tile.ancho);
                }
            }
        }

        // Explotar todas las bombas del jugador si procede
        if (buffoExplosionDistanciaBombas && explosionDistancia) {
            nivel.explotarBombas(this);
        }

        // Poner bomba si procede
        if (ponerBomba) {
            ponerBomba = false;

            if (bombasColocadas < bombasLimite) {
                if (nivel.getBombaEnCoords(x, y) == null) {
                    Bomba b = new Bomba(context, this, nivel);
                    nivel.bombas.add(b);
                    nivel.addBombaEnTile(b);
                }
            }
        }

        // Patear bomba si procede
        if (buffoPateaBombas && pateaBomba) {

            int xAxisOffset = 0;
            int yAxisOffset = 0;
            switch (orientacion) {
                case ARRIBA:
                    yAxisOffset = -1;
                    break;

                case ABAJO:
                    yAxisOffset = 1;
                    break;

                case IZQUIERDA:
                    xAxisOffset = -1;
                    break;

                case DERECHA:
                    xAxisOffset = 1;
                    break;
            }
            int xTile = nivel.getTileXFromCoord(x);
            int yTile = nivel.getTileYFromCoord(y);
            int xTileEnFrente = xTile + xAxisOffset;
            int yTileEnFrente = yTile + yAxisOffset;
            Bomba bombaEnFrente = nivel
                    .getBombaEnTile(xTileEnFrente, yTileEnFrente);

            if (bombaEnFrente != null) {
                bombaEnFrente.velocidadMovimiento = bombaEnFrente.velocidadLimite;
                bombaEnFrente.orientacion = orientacion;
            }
        }
    }


    public void aplicarReglasDeMovimiento(Nivel nivel) {
        if (movimiento) {
            if (aMover > 0) {
                // Nos movemos con la velocidad
                double paso = Math.min(aMover, velocidadMovimiento + (buffosVelodidad * 5));
                aMover -= paso;

                switch (orientacion) {
                    case Jugador.ARRIBA:
                        y -= paso;
                        break;
                    case Jugador.ABAJO:
                        y += paso;
                        break;
                    case Jugador.IZQUIERDA:
                        x -= paso;
                        break;
                    case Jugador.DERECHA:
                        x += paso;
                        break;
                }

                // Se acabo el movimiento
            } else {
                movimiento = false;

                // Movemos el jugador a la casilla mas cercana (deberia estar ya ahi, pero por si acaso hay errores de redondeo o lo que sea)
                x = Ar.x(nivel.getTileXFromCoord(x) * Tile.ancho + Tile.ancho / 2);
                y = Ar.y(nivel.getTileYFromCoord(y) * Tile.altura + Tile.altura / 2);
            }
        }
    }

    public void setMarcador(Marcador marcador) {
        this.marcador = marcador;
    }

    @Override
    public void actualizar(long tiempo) {
        if (msInmunidad > 0) {
            msInmunidad -= tiempo;
        }

        if (movimiento)
            sprite.actualizar(tiempo);
    }

    public void golpeado() {
        // Si no es inmune
        if (msInmunidad <= 0) {
            vidas--;

            // Quedan vidas
            if (vidas > 0) {
                msInmunidad = 2000; // Dos segundos de inmunidad
                restablecerPosicionInicial();
            }
        }
    }

    public int getVidas() {
        return vidas;
    }

    @Override
    protected void doDibujar(Canvas canvas) {
        sprite.dibujarSprite(canvas, (int) x, (int) y - Tile.altura / 2, msInmunidad > 0);
        if (marcador != null) marcador.dibujar(canvas);
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

    public void ordenPatearBomba() {
        pateaBomba = true;
    }

    public void ordenFinPatearBomba() {
        pateaBomba = false;
    }

    public void ordenExplotarBombas() {
        explosionDistancia = true;
    }

    public void ordenFinExplotarBombas() {
        explosionDistancia = false;
    }
}
