package com.bombergame.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.bombergame.R;
import com.bombergame.gestores.CargadorGraficos;
import com.bombergame.graficos.Ar;
import com.bombergame.modelos.mejoras.AbstractMejora;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Nivel {
    private Context context = null;
    private int numeroNivel;
    private Fondo fondo;
    private Tile[][] mapaTiles;

    public boolean inicializado;

    public boolean nivelPausado = false;

    public List<Bomba> bombas;
    public List<Explosion> explosiones;

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public Jugador getJugadorTactil() {
        return jugadores.get(0);
    }

    public Tile[][] getMapaTiles() {
        return mapaTiles;
    }

    private List<Jugador> jugadores;
    private List<Enemigo> enemigos;
    public List<AbstractMejora> mejoras;

    public Nivel(Context context, int numeroNivel, Tile[][] mapaTiles, List<Jugador> jugadores, List<Enemigo> enemigos) throws Exception {
        inicializado = false;

        this.context = context;
        this.numeroNivel = numeroNivel;
        this.mapaTiles = mapaTiles;
        this.jugadores = jugadores;
        this.enemigos = enemigos;
        inicializar();
        inicializado = true;
    }

    public void inicializar() throws Exception {
        fondo = new Fondo(context, CargadorGraficos.cargarDrawable(context, R.drawable.fondo));
        bombas = new LinkedList<>();
        explosiones = new LinkedList<>();
        mejoras = new LinkedList<>();
    }


    public void actualizar(long tiempo) {
        if (inicializado) {
            for (Jugador jugador : jugadores) {
                jugador.procesarOrdenes(this);
                jugador.actualizar(tiempo);
            }

            for (Enemigo enemigo : enemigos) {
                enemigo.mover(this);
                enemigo.actualizar(tiempo);
            }

            for (Iterator<Bomba> iterator = bombas.iterator(); iterator.hasNext(); ) {
                Bomba bomba = iterator.next();
                if(bomba.estado != Bomba.INACTIVA)
                    bomba.actualizar(tiempo);
                else {
                    iterator.remove();
                    continue;
                }
            }

            List<Explosion> explosionesEliminar = new LinkedList<>();
            for (Explosion e : explosiones) {
                if (e.estado == Explosion.FIN_EXPLOSION) {
                    explosionesEliminar.add(e);
                } else {
                    e.actualizar(tiempo);
                }
            }
            for (Explosion e : explosionesEliminar) {
                explosiones.remove(e);
            }

            aplicarReglasMovimiento();

            comprobarColisiones();

            comprobarVictoria();
        }
    }

    public int getTileXFromCoord(double x) {
        return (int) Math.round((((x - Ar.offsetX) / Ar.factor) - Tile.ancho / 2) / Tile.ancho);
    }

    public int getTileYFromCoord(double y) {
        return (int) Math.round((((y - Ar.offsetY) / Ar.factor) - Tile.altura / 2) / Tile.altura);
    }

    private void aplicarReglasMovimiento() {
        // Jugador
        for (Jugador jugador : jugadores) {
            jugador.aplicarReglasDeMovimiento(this);
        }
    }

    private void comprobarColisiones() {
        for (Iterator<Jugador> iterator = jugadores.iterator(); iterator.hasNext(); ) {
            Jugador jugador = iterator.next();

            // Con enemigo
            for (Enemigo enemigo : enemigos) {
                if (jugador.colisiona(enemigo)) {
                    jugador.golpeado();
                }
            }

            // Con explosion
            for (Explosion explosion : explosiones) {
                if (jugador.colisiona(explosion)) {
                    jugador.golpeado();
                }
            }

            // Si el jugador ha muerto, lo eliminamos
            if (jugador.getVidas() <= 0)
                iterator.remove();

            // Con mejoras
            for (Iterator<AbstractMejora> iterMejoras = mejoras.iterator(); iterMejoras.hasNext(); ) {
                AbstractMejora mejora = iterMejoras.next();
                if (jugador.colisiona(mejora)) {
                    mejora.mejorar(jugador);
                    iterMejoras.remove();
                }
            }
        }

        for (Iterator<Enemigo> iterator = enemigos.iterator(); iterator.hasNext(); ) {
            Enemigo enemigo = iterator.next();

            for (Explosion explosion : explosiones) {
                if (enemigo.colisiona(explosion)) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    private void comprobarVictoria() {
        // TODO Si no quedan enemigos y solo un jugador, ha ganado

        // TODO Si no quedan jugadores, se ha perdido
    }

    public void dibujar(Canvas canvas) {
        if (inicializado) {
            fondo.dibujar(canvas);

            dibujarTiles(canvas);

            for (Bomba b : bombas) {
                b.dibujar(canvas);
            }
            for (Explosion e : explosiones) {
                e.dibujar(canvas);
            }
            for (AbstractMejora m : mejoras) {
                m.dibujar(canvas);
            }
            for (Jugador jugador : jugadores) {
                jugador.dibujar(canvas);
            }

            for (Enemigo enemigo : enemigos) {
                enemigo.dibujar(canvas);
            }
        }
    }

    private void dibujarTiles(Canvas canvas) {
        for (int y = 0; y < altoMapaTiles(); ++y) {
            for (int x = 0; x < anchoMapaTiles(); ++x) {
                if (mapaTiles[x][y].imagen != null) {
                    // Calcular la posición en pantalla correspondiente
                    // izquierda, arriba, derecha , abajo
                    mapaTiles[x][y].imagen.setBounds(
                            (int) Ar.x((x * Tile.ancho)),
                            (int) Ar.y(y * Tile.altura),
                            (int) Ar.x((x * Tile.ancho) + Tile.ancho),
                            (int) Ar.y(y * Tile.altura + Tile.altura));
                    mapaTiles[x][y].imagen.draw(canvas);
                }
            }
        }
    }

    private int anchoMapaTiles() {
        return mapaTiles.length;
    }

    private int altoMapaTiles() {
        return mapaTiles[0].length;
    }
}

