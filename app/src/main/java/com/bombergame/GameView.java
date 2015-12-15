package com.bombergame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.bombergame.controlesJugador.ControladorJugador;
import com.bombergame.gestores.CargadorGraficos;
import com.bombergame.gestores.GestorNiveles;
import com.bombergame.modelos.Enemigo;
import com.bombergame.modelos.Jugador;
import com.bombergame.modelos.Nivel;
import com.bombergame.modelos.Tile;
import com.bombergame.modelos.controles.BotonBomba;
import com.bombergame.modelos.controles.Pad;

import java.util.List;
import java.util.Map;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    boolean iniciado = false;
    Context context;
    GameLoop gameloop;

    public static int pantallaAncho;
    public static int pantallaAlto;

    private Nivel nivel;
    public int numeroNivel;

    public Bitmap mensajeVictoria;
    public Bitmap mensajeDerrota;

    private Pad pad;
    private BotonBomba botonBomba;

    private Map<Integer, ControladorJugador> controladores;


    public GameView(Context context) {
        super(context);
        iniciado = true;

        getHolder().addCallback(this);
        setFocusable(true);

        this.context = context;
        gameloop = new GameLoop(this);
        gameloop.setRunning(true);
    }

    protected void inicializar() throws Exception {
        GestorNiveles.getInstancia().cargarDatosNivel(context);
        Tile[][] mapaTiles = GestorNiveles.getInstancia().getMapaTiles();
        List<Jugador> jugadores = GestorNiveles.getInstancia().getJugadores();
        List<Enemigo> enemigos = GestorNiveles.getInstancia().getEnemigos();
        controladores = GestorNiveles.getInstancia().getControladores();
        nivel = new Nivel(context, numeroNivel, mapaTiles, jugadores, enemigos);
        establecerModoJuego(jugadores);

        pad = new Pad(context);
        botonBomba = new BotonBomba(context);

        mensajeVictoria = CargadorGraficos.cargarBitmap(context, R.drawable.victoria);
        mensajeDerrota = CargadorGraficos.cargarBitmap(context, R.drawable.derrota);
    }

    private void establecerModoJuego(List<Jugador> jugadores) {
        if (jugadores.size() > 1)
            nivel.modo = Nivel.MULTIJUGADOR;
        else
            nivel.modo = Nivel.INDIVIDUAL;
    }

    public void actualizar(long tiempo) throws Exception {
        nivel.actualizar(tiempo);
    }

    protected void dibujar(Canvas canvas) {
        nivel.dibujar(canvas);
        pad.dibujar(canvas);
        botonBomba.dibujar(canvas);

        dibujarPantallaVictoriaDerrota(canvas);
    }

    private void dibujarPantallaVictoriaDerrota(Canvas canvas) {
        if (nivel.victoria || nivel.derrota) {
            int width = mensajeVictoria.getWidth();
            int height = mensajeVictoria.getHeight();

            float factorX = GameView.pantallaAncho / (float) width;
            float factorY = GameView.pantallaAlto / (float) height;

            float factorMenor = Math.min(factorX, factorY);
            int widthFactored = (int) (height * factorMenor);
            int heightFactored = (int) (height * factorMenor);

            Rect origen = new Rect(0, 0, width, height);
            Rect destino = new Rect(GameView.pantallaAncho / 2 - widthFactored / 2,
                    GameView.pantallaAlto / 2 - heightFactored / 2,
                    GameView.pantallaAncho / 2 + widthFactored / 2,
                    GameView.pantallaAlto / 2 + heightFactored / 2);
            canvas.drawBitmap(nivel.victoria ? mensajeVictoria : mensajeDerrota, origen, destino, null);
        }
    }

    public void procesarEventosTouch() throws Exception {
        boolean pulsacionPadMover = false;
        boolean pulsacionBotonBomba = false;
        for (int i = 0; i < 6; i++) {
            if (accion[i] != NO_ACTION) {
                if (accion[i] == ACTION_DOWN && (nivel.victoria || nivel.derrota)) {
                    inicializar();
                }

                if (pad.estaPulsado(x[i], y[i])) {
                    // Si almenosuna pulsacion está en el pad
                    if (accion[i] != ACTION_UP) {
                        pulsacionPadMover = true;
                        int orientacionPadX = pad.getOrientacionX(x[i]);
                        int orientacionPadY = pad.getOrientacionY(y[i]);
                        if (Math.abs(orientacionPadX) >= Math.abs(orientacionPadY)) {
                            if (orientacionPadX > 0) {
                                nivel.getJugadorTactil().ordenMovimientoDerecha();
                            } else if (orientacionPadX < 0) {
                                nivel.getJugadorTactil().ordenMovimientoIzquierda();
                            }
                        } else {
                            if (orientacionPadY > 0) {
                                nivel.getJugadorTactil().ordenMovimientoAbajo();
                            } else if (orientacionPadY < 0) {
                                nivel.getJugadorTactil().ordenMovimientoArriba();
                            }
                        }
                    }
                }

                if (botonBomba.estaPulsado(x[i], y[i])) {
                    if (accion[i] == ACTION_DOWN) {
                        Jugador jugador = nivel.getJugadorTactil();
                        jugador.ordenPonerBomba();
                        pulsacionBotonBomba = true;
                    }
                }
            }
        }
        if (!pulsacionPadMover) {
            nivel.getJugadorTactil().ordenFinMovimientoDerecha();
            nivel.getJugadorTactil().ordenFinMovimientoIzquierda();
            nivel.getJugadorTactil().ordenFinMovimientoAbajo();
            nivel.getJugadorTactil().ordenFinMovimientoArriba();
        }
        if (!pulsacionBotonBomba) {
            nivel.getJugadorTactil().ordenFinPonerBomba();
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        // valor a Binario
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        // Indice del puntero
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;

        int pointerId = event.getPointerId(pointerIndex);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                accion[pointerId] = ACTION_DOWN;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                accion[pointerId] = ACTION_UP;
                x[pointerId] = event.getX(pointerIndex);
                y[pointerId] = event.getY(pointerIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {
                    pointerIndex = i;
                    pointerId = event.getPointerId(pointerIndex);
                    accion[pointerId] = ACTION_MOVE;
                    x[pointerId] = event.getX(pointerIndex);
                    y[pointerId] = event.getY(pointerIndex);
                }
                break;
        }

        try {
            procesarEventosTouch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    int NO_ACTION = 0;
    int ACTION_MOVE = 1;
    int ACTION_UP = 2;
    int ACTION_DOWN = 3;
    int accion[] = new int[6];
    float x[] = new float[6];
    float y[] = new float[6];


    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        pantallaAncho = width;
        pantallaAlto = height;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        if (iniciado) {
            iniciado = false;
            if (gameloop.isAlive()) {
                iniciado = true;
                gameloop = new GameLoop(this);
            }

            gameloop.setRunning(true);
            gameloop.start();
        } else {
            iniciado = true;
            gameloop = new GameLoop(this);
            gameloop.setRunning(true);
            gameloop.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        iniciado = false;

        boolean intentarDeNuevo = true;
        gameloop.setRunning(false);
        while (intentarDeNuevo) {
            try {
                gameloop.join();
                intentarDeNuevo = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        ControladorJugador accion = controladores.get(keyCode);
        if (accion != null)
            accion.keyUp();
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ControladorJugador accion = controladores.get(keyCode);
        Log.e("GAMEVIEW", "Botón pulsado: " + keyCode + " " + KeyEvent.keyCodeToString(keyCode));
        if (accion != null)
            accion.keyDown();
        return super.onKeyUp(keyCode, event);
    }
}

