package com.bombergame.graficos;

import com.bombergame.GameView;
import com.bombergame.modelos.Tile;

public class Ar {
    public static double factor;

    public static int offsetX;
    public static int offsetY;

    public static void configurar(int tilesX, int tilesY) {
        double factorX = (double) GameView.pantallaAncho / (Tile.ancho * tilesX);
        double factorY = (double) GameView.pantallaAlto / (Tile.altura * tilesY);

        factor = Math.min(factorX, factorY);

        // Calcular offsets
        offsetX = (int) ((GameView.pantallaAncho - Ar.ancho(Tile.ancho * tilesX)) / Ar.min(2));
        offsetY = (int) ((GameView.pantallaAlto - Ar.alto(Tile.altura * tilesY)) / Ar.min(2));
    }

    public static double y(double y) {
        return y * factor + offsetY;
    }

    public static double x(double x) {
        return x * factor + offsetX;
    }

    public static double min(double d) {
        return d * factor;
    }

    public static int alto(int alto) {
        return (int) (alto * factor);
    }

    public static int ancho(int ancho) {
        return (int) (ancho * factor);
    }
}
