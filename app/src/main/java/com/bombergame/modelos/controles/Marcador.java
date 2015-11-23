package com.bombergame.modelos.controles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.bombergame.GameView;
import com.bombergame.modelos.Modelo;

public class Marcador extends Modelo {

    private int puntos = 10;
    private static final int textSize = 20;

    public Marcador(Context context) {
        super(context, GameView.pantallaAncho * 0.9, GameView.pantallaAlto * 0.1, 30, 30);

    }

    @Override
    protected void doDibujar(Canvas canvas) {
        // Dibujamos la puntuacion
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        canvas.drawText(String.valueOf(puntos), (int) x + ancho, (int) y + (altura - textSize) / 2, paint);
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
}
