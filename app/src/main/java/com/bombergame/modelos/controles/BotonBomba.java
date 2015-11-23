package com.bombergame.modelos.controles;

import android.content.Context;

import com.bombergame.GameView;
import com.bombergame.R;
import com.bombergame.gestores.CargadorGraficos;
import com.bombergame.modelos.Modelo;

public class BotonBomba extends Modelo {

    public BotonBomba(Context context) {
        super(context, GameView.pantallaAncho * 0.9, GameView.pantallaAlto * 0.8,
                (int) (GameView.pantallaAncho * 0.1), (int) (GameView.pantallaAncho * 0.1));

        imagen = CargadorGraficos.cargarDrawable(context, R.drawable.button_bomb);
    }

    public boolean estaPulsado(float clickX, float clickY) {
        return clickX <= (x + ancho / 2)
                && clickX >= (x - ancho / 2)
                && clickY <= (y + altura / 2)
                && clickY >= (y - altura / 2);
    }
}
