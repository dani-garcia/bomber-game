package com.bombergame.modelos;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class Modelo {

    public static boolean DEBUG_HITBOX = true;

    public Context context;
    public double x;
    public double y;
    public int altura;
    public int ancho;
    protected Drawable imagen;

    //Rectangulo de colision
    int cDerecha;
    int cIzquierda;
    int cArriba;
    int cAbajo;

    public Modelo(Context context, double x, double y, int altura, int ancho) {
        this.context = context;
        this.x = x;
        this.y = y;
        this.altura = altura;
        this.ancho = ancho;

        cDerecha = ancho / 2;
        cIzquierda = ancho / 2;
        cArriba = altura / 2;
        cAbajo = altura / 2;
    }

    public final void dibujar(Canvas canvas) {
        doDibujar(canvas);

        if (DEBUG_HITBOX) {
            Paint paint = new Paint();

            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
            paint.setStrokeWidth(1);

            canvas.drawRect(
                    (float) x - cIzquierda,
                    (float) y - cArriba,
                    (float) x + cDerecha,
                    (float) y + cAbajo, paint);
        }
    }

    protected void doDibujar(Canvas canvas) {
        int yArriba = (int) y - altura / 2;
        int xIzquierda = (int) x - ancho / 2;

        imagen.setBounds(xIzquierda, yArriba, xIzquierda + ancho, yArriba + altura);
        imagen.draw(canvas);
    }

    // No Actualiza
    public void actualizar(long tiempo) {

    }

    public boolean colisiona(Modelo m) {
        return (m.x - m.cIzquierda) <= (x + cDerecha)
                && (m.x + m.cDerecha) >= (x - cIzquierda)
                && (y + cAbajo) >= (m.y - m.cArriba)
                && (y - cArriba) < (m.y + m.cAbajo);
    }
}


