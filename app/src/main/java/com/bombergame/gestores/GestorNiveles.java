package com.bombergame.gestores;


import android.content.Context;
import android.graphics.Point;
import android.util.Log;

import com.bombergame.R;
import com.bombergame.graficos.Ar;
import com.bombergame.modelos.Jugador;
import com.bombergame.modelos.Tile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GestorNiveles {

    private List<Jugador> jugadores;
    private int numeroJugadores = 1;
    private int nivelActual = 1;

    private List<Point> iniciosJugadores;
    private Tile[][] mapaTiles;

    private static GestorNiveles instancia = null;

    private GestorNiveles() {
    }

    public static GestorNiveles getInstancia() {
        synchronized (GestorNiveles.class) {
            if (instancia == null)
                instancia = new GestorNiveles();
            return instancia;
        }
    }

    public int getNumeroJugadores() {
        return numeroJugadores;
    }

    public void setNumeroJugadores(int numeroJugadores) {
        this.numeroJugadores = numeroJugadores;
    }

    public int getNivelActual() {
        return nivelActual;
    }

    public void setNivelActual(int nivelActual) {
        this.nivelActual = nivelActual;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public Tile[][] getMapaTiles() {
        return mapaTiles;
    }

    public void cargarDatosNivel(Context context) {
        jugadores = new LinkedList<>();
        iniciosJugadores = new LinkedList<>();

        inicializarMapaTiles(context);
        inicializarJugadores(context);
        inicializarContoles();
    }

    private void inicializarContoles() {

    }

    private void inicializarJugadores(Context context) {
        for (int i = 0;
             i < iniciosJugadores.size() && i < numeroJugadores;
             i++) {
            double xCentroAbajoTile = Ar.x(iniciosJugadores.get(i).x * Tile.ancho + Tile.ancho / 2);
            double yCentroAbajoTile = Ar.y(iniciosJugadores.get(i).y * Tile.altura + Tile.altura);

            Jugador jugador = new Jugador(context, xCentroAbajoTile, yCentroAbajoTile);
            jugadores.add(jugador);
        }
    }

    private void inicializarMapaTiles(Context context) {
        try {
            int anchoLinea;
            List<String> lineas = new ArrayList<>();
            InputStream is = context.getAssets().open(nivelActual + ".txt");
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
                    mapaTiles[x][y] = inicializarTile(context, tipoDeTile, x, y);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Tile inicializarTile(Context context, char codigoTile, int x, int y) {
        // Posicion centro abajo
        //double xCentroAbajoTile = Ar.x(x * Tile.ancho + Tile.ancho / 2);
        //double yCentroAbajoTile = Ar.y(y * Tile.altura + Tile.altura);

        switch (codigoTile) {
            case 'J':
                iniciosJugadores.add(new Point(x, y));
                //jugadores.add(new Jugador(context, xCentroAbajoTile, yCentroAbajoTile));
                //numeroJugadores++;
                return Tile.VACIO;

            case '#':
                return new Tile(CargadorGraficos.cargarDrawable(context,
                        R.drawable.tile_solid_block), Tile.SOLIDO);

            default:
                //cualquier otro caso
                return Tile.VACIO;
        }
    }

    private int anchoMapaTiles() {
        return mapaTiles.length;
    }

    private int altoMapaTiles() {
        return mapaTiles[0].length;
    }
}
