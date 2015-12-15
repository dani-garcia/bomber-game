package com.bombergame.modelos.controles;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.bombergame.R;
import com.bombergame.gestores.CargadorGraficos;
import com.bombergame.modelos.Jugador;
import com.bombergame.modelos.Modelo;

public class Marcador extends Modelo {

    private Jugador jugador;
    private static final int textSize = 32;
    private static final int smallIconsSize = 32;
    private Drawable imagenBomba;
    private Drawable imagenFuego;
    private Drawable imagenVelocidad;
    private Drawable imagenPatearBomba;

    public Marcador(Context context, double xIzquierda, double yArriba, Jugador jugador) {
        super(context, xIzquierda, yArriba, 47, 47);
        this.jugador = jugador;
        this.imagen = CargadorGraficos.cargarDrawable(context,
                R.drawable.icon_player_1 + (jugador.getId() - 1));
        this.imagenBomba = CargadorGraficos.cargarDrawable(context,
                R.drawable.icon_bomb);
        this.imagenFuego = CargadorGraficos.cargarDrawable(context,
                R.drawable.icon_flame);
        this.imagenVelocidad = CargadorGraficos.cargarDrawable(context,
                R.drawable.powerup_speed);
        this.imagenPatearBomba = CargadorGraficos.cargarDrawable(context,
                R.drawable.powerup_kick);
    }

    @Override
    protected void doDibujar(Canvas canvas) {
        int xOrigenImagen = (int) x;
        int yOrigenImagen = (int) y;
        Paint formatoTexto = new Paint();
        formatoTexto.setColor(Color.WHITE);
        formatoTexto.setAntiAlias(true);
        formatoTexto.setTextSize(textSize);

        imagen.setBounds(xOrigenImagen, yOrigenImagen,
                xOrigenImagen + ancho, yOrigenImagen + altura);
        imagen.draw(canvas);

        xOrigenImagen = xOrigenImagen + ancho + (smallIconsSize / 5);
        yOrigenImagen = yOrigenImagen + ((altura - smallIconsSize) / 2);
        imagenBomba.setBounds(xOrigenImagen, yOrigenImagen,
                xOrigenImagen + smallIconsSize, yOrigenImagen + smallIconsSize);
        imagenBomba.draw(canvas);
        xOrigenImagen = xOrigenImagen + smallIconsSize + (smallIconsSize / 4);
        canvas.drawText(String.valueOf(jugador.bombasLimite), xOrigenImagen, yOrigenImagen + (smallIconsSize / 8) * 7, formatoTexto);

        xOrigenImagen = xOrigenImagen + smallIconsSize;
        imagenFuego.setBounds(xOrigenImagen, yOrigenImagen,
                xOrigenImagen + smallIconsSize, yOrigenImagen + smallIconsSize);
        imagenFuego.draw(canvas);
        xOrigenImagen = xOrigenImagen + smallIconsSize + (smallIconsSize / 4);
        canvas.drawText(String.valueOf(jugador.alcanceBombas), xOrigenImagen, yOrigenImagen + (smallIconsSize / 8) * 7, formatoTexto);

        xOrigenImagen = xOrigenImagen + smallIconsSize;
        imagenVelocidad.setBounds(xOrigenImagen, yOrigenImagen,
                xOrigenImagen + smallIconsSize, yOrigenImagen + smallIconsSize);
        imagenVelocidad.draw(canvas);
        xOrigenImagen = xOrigenImagen + smallIconsSize + (smallIconsSize / 4);
        canvas.drawText(String.valueOf(jugador.buffosVelodidad), xOrigenImagen, yOrigenImagen + (smallIconsSize / 8) * 7, formatoTexto);

        if ( jugador.buffoPateaBombas) {
            xOrigenImagen = xOrigenImagen + smallIconsSize;
            imagenPatearBomba.setBounds(xOrigenImagen, yOrigenImagen,
                    xOrigenImagen + smallIconsSize, yOrigenImagen + smallIconsSize);
            imagenPatearBomba.draw(canvas);
        }


    }
}
