package com.bombergame;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.bombergame.gestores.GestorNiveles;

public class MultiPlayerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pantalla completa, sin titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_multi_player);


        // Recuperamos el control a partir de su ID
        ImageButton botonDosJugadores = (ImageButton) findViewById(R.id.imageButton5);
        botonDosJugadores.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Seleccionar número de jugadores
                GestorNiveles.getInstancia().setNumeroJugadores(2);
                // Abrir una nueva actividad desde otra
                Intent actividadJuego = new Intent(MultiPlayerActivity.this, MainActivity.class);
                startActivity(actividadJuego);
                // Cerrar la actividad actual
                finish();
            }
        });

        // Recuperamos el control a partir de su ID
        ImageButton botonTresJugadores = (ImageButton) findViewById(R.id.imageButton6);
        botonTresJugadores.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Seleccionar número de jugadores
                GestorNiveles.getInstancia().setNumeroJugadores(3);
                // Abrir una nueva actividad desde otra
                Intent actividadJuego = new Intent(MultiPlayerActivity.this, MainActivity.class);
                startActivity(actividadJuego);
                // Cerrar la actividad actual
                finish();
            }
        });

        // Recuperamos el control a partir de su ID
        ImageButton botonCuatroJugadores = (ImageButton) findViewById(R.id.imageButton7);
        botonCuatroJugadores.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Seleccionar número de jugadores
                GestorNiveles.getInstancia().setNumeroJugadores(4);
                // Abrir una nueva actividad desde otra
                Intent actividadJuego = new Intent(MultiPlayerActivity.this, MainActivity.class);
                startActivity(actividadJuego);
                // Cerrar la actividad actual
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multi_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
