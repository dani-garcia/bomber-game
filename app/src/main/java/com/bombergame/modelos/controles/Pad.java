package com.bombergame.modelos.controles;

import android.content.Context;

import com.bombergame.GameView;
import com.bombergame.R;
import com.bombergame.gestores.CargadorGraficos;
import com.bombergame.modelos.Modelo;

public class Pad extends Modelo {

    public Pad(Context context) {
        super(context, GameView.pantallaAncho * 0.15, GameView.pantallaAlto * 0.75,
                (int) (GameView.pantallaAncho * 0.20), (int) (GameView.pantallaAncho * 0.20));

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.pad);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        return clickX <= (x + ancho / 2)
                && clickX >= (x - ancho / 2)
                && clickY <= (y + altura / 2)
                && clickY >= (y - altura / 2);
    }

    public int getOrientacionX(float clickX) {
        return (int) (clickX - x);
    }

    public int getOrientacionY(float clickY) {
        return (int) (clickY - y);
    }
}

