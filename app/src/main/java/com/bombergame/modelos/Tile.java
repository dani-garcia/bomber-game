package com.bombergame.modelos;

import android.graphics.drawable.Drawable;

public class Tile {
    public static final int PASABLE = 0;
    public static final int SOLIDO = 1;

    /**
     * Para no tener que crear un tile vacio cada vez
     */
    public static final Tile VACIO = new Tile(null, PASABLE);

    public int tipoColision; // PASABLE o SOLIDO

    public static final int ancho = 64;
    public static final int altura = 64;

    public final Drawable imagen;

    public Tile(Drawable imagen, int tipo) {
        this.imagen = imagen;
        this.tipoColision = tipo;
    }
}