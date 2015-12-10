package com.bombergame.gestores;


import android.content.Context;
import android.util.Log;

import com.bombergame.R;
import com.bombergame.controlesJugador.ControladorJugaror;
import com.bombergame.controlesJugador.MoverJugadorAbajo;
import com.bombergame.controlesJugador.MoverJugadorArriba;
import com.bombergame.controlesJugador.MoverJugadorDerecha;
import com.bombergame.controlesJugador.MoverJugadorIzquierda;
import com.bombergame.controlesJugador.PonerBomba;
import com.bombergame.graficos.Ar;
import com.bombergame.modelos.Jugador;
import com.bombergame.modelos.Tile;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class GestorNiveles {

    private List<Jugador> jugadores;
    private int numeroJugadores = 1;
    private int nivelActual = 1;

    //private Map<Integer, Point> iniciosJugadores;
    private Tile[][] mapaTiles;

    private Map<Integer, ControladorJugaror> controladores;

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

    public Map<Integer, ControladorJugaror> getControladores() {
        return controladores;
    }

    public void cargarDatosNivel(Context context) {
        jugadores = new LinkedList<>();
        //iniciosJugadores = new HashMap<>();
        controladores = new HashMap<>();

        inicializarMapaTiles(context);
        //comprobarNumeroJugadores(context);
        inicializarContoles(context);
    }

    private void inicializarContoles(Context context) {
        ParserXML parser = new ParserXML();
        String textoFicheroNivel = "";
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.controles);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(inputStream));
            String linea = bufferedReader.readLine();
            while (linea != null) {
                textoFicheroNivel += linea;
                linea = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (Exception ex) {
        }

        Document doc = parser.getDom(textoFicheroNivel);

        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression expr = null;
        NodeList nl = null;
        for (Jugador jugador : jugadores) {
            try {
                expr = xpath.compile("//jugador[@id=\"" + jugador.getId() + "\"]");
                nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
                Element elementoActual = (Element) nl.item(0);
                controladores.put(
                        Integer.parseInt(parser.getValor(elementoActual, "abajo"))
                        , new MoverJugadorAbajo(jugador));
                controladores.put(
                        Integer.parseInt(parser.getValor(elementoActual, "arriba"))
                        , new MoverJugadorArriba(jugador));
                controladores.put(
                        Integer.parseInt(parser.getValor(elementoActual, "derecha"))
                        , new MoverJugadorDerecha(jugador));
                controladores.put(
                        Integer.parseInt(parser.getValor(elementoActual, "izquierda"))
                        , new MoverJugadorIzquierda(jugador));
                controladores.put(
                        Integer.parseInt(parser.getValor(elementoActual, "ponerbomba"))
                        , new PonerBomba(jugador));
                Log.e("GESTORNIVELES", "numero de controladores: " + controladores.size());
//                Log.e("GESTORNIVELES", "---- botón:" + parser.getValor(elementoActual, "abajo"));
//                Log.e("GESTORNIVELES", "---- botón:" + parser.getValor(elementoActual, "arriba"));
//                Log.e("GESTORNIVELES", "---- botón:" + parser.getValor(elementoActual, "derecha"));
//                Log.e("GESTORNIVELES", "---- botón:" + parser.getValor(elementoActual, "izquierda"));
//                Log.e("GESTORNIVELES", "---- botón:" + parser.getValor(elementoActual, "ponerbomba"));
            } catch (XPathExpressionException e) {
                Log.e("GESTOR_NIVELES", e.getMessage());
            }
        }
//        NodeList nodos = doc.getElementsByTagName("jugador");
//        for (Jugador jugador : jugadores) {
//            nodositem
//        }
//
//        LinkedList<PowerUp> powerUps = new LinkedList<PowerUp>();
//        NodeList nodos = doc.getElementsByTagName("powerup");
//        for (int i = 0; i < nodos.getLength(); i++) {
//            Element elementoActual = (Element) nodos.item(i);
//            String type = parser.getValor(elementoActual, "type");
//            String x = parser.getValor(elementoActual, "x");
//            String y = parser.getValor(elementoActual, "y");
//            powerUps.add(powerUpCreators.get(type).
//                    create(context, Ar.x(Double.parseDouble(x)), Ar.y(Double.parseDouble(y))));
//        }
    }

    private void comprobarNumeroJugadores(Context context) {
//        if(jugadores.size() > numeroJugadores) {
//            for (int i = numeroJugadores; i<jugadores.size();i++) {
//                jugadores.remove(i);
//            }
//        }
//        for (int i = 0;
//             i < iniciosJugadores.size() && i < numeroJugadores;
//             i++) {
//            double xCentroAbajoTile = Ar.x(iniciosJugadores.get(i).x * Tile.ancho + Tile.ancho / 2);
//            double yCentroAbajoTile = Ar.y(iniciosJugadores.get(i).y * Tile.altura + Tile.altura);
//
//            Jugador jugador = new Jugador(context, xCentroAbajoTile, yCentroAbajoTile, 1);
//            jugadores.add(jugador);
//        }
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
            case '1':
            case '2':
            case '3':
            case '4':
                int tileNumeric = Character.getNumericValue(codigoTile);
                if (tileNumeric <= numeroJugadores) {
                    double xCentroAbajoTile = Ar.x(x * Tile.ancho + Tile.ancho / 2);
                    double yCentroAbajoTile = Ar.y(y * Tile.altura + Tile.altura / 2);

                    jugadores.add(new Jugador(context, xCentroAbajoTile, yCentroAbajoTile, tileNumeric));
                    //iniciosJugadores.put(Integer.getInteger("" + codigoTile), new Point(x, y));
                }
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
