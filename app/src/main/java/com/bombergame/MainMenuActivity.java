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

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Pantalla completa, sin titulo
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main_menu);

        // Recuperamos el control a partir de su ID
        ImageButton botonSinglePlayer = (ImageButton) findViewById(R.id.imageButton2);
        botonSinglePlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Abrir una nueva actividad desde otra
                Intent actividadJuego = new Intent(MainMenuActivity.this, MainActivity.class);
                startActivity(actividadJuego);
                // Cerrar la actividad actual
                finish();
            }
        });

        // Recuperamos el control a partir de su ID
        ImageButton botonMultiPlayer = (ImageButton) findViewById(R.id.imageButton3);
        botonMultiPlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Abrir una nueva actividad desde otra
                Intent actividadJuego = new Intent(MainMenuActivity.this, MultiPlayerActivity.class);
                startActivity(actividadJuego);
                // Cerrar la actividad actual
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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
