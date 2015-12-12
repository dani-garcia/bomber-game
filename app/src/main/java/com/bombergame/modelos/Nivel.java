package com.bombergame.modelos;

import android.content.Context;
import android.graphics.Canvas;

import com.bombergame.R;
import com.bombergame.gestores.CargadorGraficos;
import com.bombergame.graficos.Ar;
import com.bombergame.modelos.mejoras.Mejora;

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

    public Tile[][] getMapaTiles(){
        return mapaTiles;
    }

    private List<Jugador> jugadores;
    private List<Enemigo> enemigos;
    public List<Mejora> mejoras;

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

            for(Enemigo enemigo: enemigos){
                enemigo.mover(this);
                enemigo.actualizar(tiempo);
            }

            Bomba bombaEliminar = null;
            for(Bomba b:bombas){
                if(b.estado == b.INACTIVA)
                    bombaEliminar = b;
                else
                    b.actualizar(tiempo);
            }

            bombas.remove(bombaEliminar);

            List<Explosion> explosionesEliminar = new LinkedList<>();
            for(Explosion e: explosiones){
                if(e.estado == Explosion.FIN_EXPLOSION){
                    explosionesEliminar.add(e);
                } else {
                    e.actualizar(tiempo);
                }
            }
            for(Explosion e: explosionesEliminar){
                explosiones.remove(e);
            }

            List<Mejora> mejorasEliminar = new LinkedList<>();
            for(Mejora m: mejoras){
                if(m.estado == Mejora.COGIDA){
                    mejorasEliminar.add(m);
                }
            }
            for(Mejora m: mejorasEliminar){
                mejoras.remove(m);
            }

            aplicarReglasMovimiento();
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
            if (jugador.movimiento) {
                if (jugador.aMover > 0) {
                    int tileX = getTileXFromCoord(jugador.x);
                    int tileY = getTileYFromCoord(jugador.y);

                    // Nos movemos con la velocidad
                    double paso = Math.min(jugador.aMover, jugador.velocidadMovimiento);
                    jugador.aMover -= paso;

                    switch (jugador.orientacion) {
                        case Jugador.ARRIBA:
                            if (mapaTiles[tileX][tileY - 1].tipoColision == Tile.PASABLE)
                                jugador.y -= paso;
                            break;
                        case Jugador.ABAJO:
                            if (mapaTiles[tileX][tileY + 1].tipoColision == Tile.PASABLE)
                                jugador.y += paso;
                            break;
                        case Jugador.IZQUIERDA:
                            if (mapaTiles[tileX - 1][tileY].tipoColision == Tile.PASABLE)
                                jugador.x -= paso;
                            break;
                        case Jugador.DERECHA:
                            if (mapaTiles[tileX + 1][tileY].tipoColision == Tile.PASABLE)
                                jugador.x += paso;
                            break;
                    }

                    // Se acabo el movimiento
                } else {
                    jugador.movimiento = false;

                    // Movemos el jugador a la casilla mas cercana (deberia estar ya ahi, pero por si acaso hay errores de redondeo o lo que sea)
                    jugador.x = Ar.x(getTileXFromCoord(jugador.x) * Tile.ancho + Tile.ancho / 2);
                    jugador.y = Ar.y(getTileYFromCoord(jugador.y) * Tile.altura + Tile.altura / 2);
                }
            }
        }
    }


    public void dibujar(Canvas canvas) {
        if (inicializado) {
            fondo.dibujar(canvas);

            dibujarTiles(canvas);

            for(Bomba b:bombas) {
                b.dibujar(canvas);
            }
            for(Explosion e: explosiones){
                e.dibujar(canvas);
            }
            for(Mejora m:mejoras){
                m.dibujar(canvas);
            }
            for (Jugador jugador : jugadores) {
                jugador.dibujar(canvas);
            }

            for (Enemigo enemigo: enemigos){
                enemigo.dibujar(canvas);
            }


            // Lo demas
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

