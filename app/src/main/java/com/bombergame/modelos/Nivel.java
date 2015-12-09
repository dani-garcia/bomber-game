package com.bombergame.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.Keyboard;
import android.util.Log;
import android.view.KeyEvent;

import com.bombergame.R;
import com.bombergame.controlesJugador.ControladorJugaror;
import com.bombergame.controlesJugador.MoverJugadorArriba;
import com.bombergame.gestores.CargadorGraficos;
import com.bombergame.gestores.GestorNiveles;
import com.bombergame.graficos.Ar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Nivel {
    private Context context = null;
    private int numeroNivel;
    private Fondo fondo;
    private Tile[][] mapaTiles;

    public boolean inicializado;

    public boolean nivelPausado = false;

    public List<Bomba> bombas;

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public Jugador getJugadorTactil() {
        return jugadores.get(0);
    }

    private List<Jugador> jugadores;

    public Nivel(Context context, int numeroNivel, Tile[][] mapaTiles, List<Jugador> jugadores) throws Exception {
        inicializado = false;

        this.context = context;
        this.numeroNivel = numeroNivel;
        this.mapaTiles = mapaTiles;
        this.jugadores = jugadores;
        inicializar();
        inicializado = true;
    }

    public void inicializar() throws Exception {
        fondo = new Fondo(context, CargadorGraficos.cargarDrawable(context, R.drawable.fondo));
        bombas = new LinkedList<>();
        // Inicializamos los tiles
        //inicializarMapaTiles();

    }

    /*private void inicializarMapaTiles() {
        try {
            int anchoLinea;
            List<String> lineas = new ArrayList<>();
            InputStream is = context.getAssets().open(numeroNivel + ".txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            {
                String linea = reader.readLine();
                anchoLinea = linea.length();
                while (linea != null) {
                    lineas.add(linea);
                    if (linea.length() != anchoLinea) {
                        Log.e("ERROR", "Dimensiones incorrectas en la línea");
                        throw new Exception("Dimensiones incorrectas en la línea.");
                    }
                    linea = reader.readLine();
                }
            }

            // Configuramos la resolucion
            Ar.configurar(anchoLinea, lineas.size());

            // Inicializar la matriz
            mapaTiles = new Tile[anchoLinea][lineas.size()];
            // Iterar y completar todas las posiciones
            for (int y = 0; y < altoMapaTiles(); ++y) {
                for (int x = 0; x < anchoMapaTiles(); ++x) {
                    char tipoDeTile = lineas.get(y).charAt(x);//lines[y][x];
                    mapaTiles[x][y] = inicializarTile(tipoDeTile, x, y);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Tile inicializarTile(char codigoTile, int x, int y) {
        // Posicion centro abajo
        double xCentroAbajoTile = Ar.x(x * Tile.ancho + Tile.ancho / 2);
        double yCentroAbajoTile = Ar.y(y * Tile.altura + Tile.altura);

        switch (codigoTile) {
            case '1':
                // Jugador
                jugador = new Jugador(context, xCentroAbajoTile, yCentroAbajoTile);
                return Tile.VACIO;

            case '#':
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_solid_block), Tile.SOLIDO);

            default:
                //cualquier otro caso
                return Tile.VACIO;
        }
    }*/


    public void actualizar(long tiempo) {
        if (inicializado) {
            for (Jugador jugador : jugadores) {
                jugador.procesarOrdenes();
                jugador.actualizar(tiempo);
            }
            aplicarReglasMovimiento();
        }
    }

    private int getTileXFromCoord(double x) {
        return (int) Math.round((((x - Ar.offsetX) / Ar.factor) - Tile.ancho / 2) / Tile.ancho);
    }

    private int getTileYFromCoord(double y) {
        return (int) Math.round((((y - Ar.offsetY) / Ar.factor) - Tile.altura) / Tile.altura);
    }

    private void aplicarReglasMovimiento() {
        // Jugador
        for (Jugador jugador : jugadores) {

            if (jugador.movimiento) {
                if (jugador.aMover > 0) {
                    int tileX = getTileXFromCoord(jugador.x);
                    int tileY = getTileYFromCoord(jugador.y) + 1;

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
                    jugador.y = Ar.y(getTileYFromCoord(jugador.y) * Tile.altura + Tile.altura);
                }
            }
        }
    }


    public void dibujar(Canvas canvas) {
        if (inicializado) {
            fondo.dibujar(canvas);

            dibujarTiles(canvas);

            for (Jugador jugador : jugadores) {
                jugador.dibujar(canvas);
            }

            System.out.println(bombas.size());
            for(Bomba b:bombas) {

                if (b.estado == b.PUESTA)
                    b.dibujar(canvas);
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

