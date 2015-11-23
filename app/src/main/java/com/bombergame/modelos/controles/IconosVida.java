package com.bombergame.modelos.controles;

import android.content.Context;
import android.graphics.Canvas;

import com.bombergame.R;
import com.bombergame.gestores.CargadorGraficos;
import com.bombergame.modelos.Modelo;

public class IconosVida extends Modelo {

    public IconosVida(Context context, double x, double y) {
        super(context, x, y, 40, 40);

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.bomb_f01);
    }

    public void dibujar(Canvas canvas, int vidas) {
        int yArriba = (int) y - altura / 2;
        int xIzquierda = (int) x - ancho / 2;

        for (int i = 0; i < vidas; i++) {
            imagen.setBounds(xIzquierda, yArriba, xIzquierda + ancho, yArriba + altura);
            imagen.draw(canvas);

            xIzquierda += 50;
        }
    }
}
