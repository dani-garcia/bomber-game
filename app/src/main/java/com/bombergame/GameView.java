package com.bombergame;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.bombergame.controlesJugador.ControladorJugaror;
import com.bombergame.controlesJugador.MoverJugadorAbajo;
import com.bombergame.controlesJugador.MoverJugadorArriba;
import com.bombergame.controlesJugador.MoverJugadorDerecha;
import com.bombergame.controlesJugador.MoverJugadorIzquierda;
import com.bombergame.gestores.GestorNiveles;
import com.bombergame.modelos.Bomba;
import com.bombergame.modelos.Jugador;
import com.bombergame.modelos.Nivel;
import com.bombergame.modelos.Tile;
import com.bombergame.modelos.controles.BotonBomba;
import com.bombergame.modelos.controles.Pad;

import java.util.HashMap;
import java.util.LinkedList;
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

    private Pad pad;
    private BotonBomba botonBomba;

    private Map<Integer, ControladorJugaror> controladores;


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
        controladores = GestorNiveles.getInstancia().getControladores();
        nivel = new Nivel(context, numeroNivel, mapaTiles, jugadores);

        pad = new Pad(context);
        botonBomba = new BotonBomba(context);

        Log.e("GAMEVIEW", "numero de controladores:" + controladores.keySet().size());

//        controladores = new HashMap<>();
//        controladores.put(KeyEvent.KEYCODE_W, new MoverJugadorArriba(nivel.getJugadores().get(0)));
//        controladores.put(KeyEvent.KEYCODE_S, new MoverJugadorAbajo(nivel.getJugadores().get(0)));
//        controladores.put(KeyEvent.KEYCODE_A, new MoverJugadorIzquierda(nivel.getJugadores().get(0)));
//        controladores.put(KeyEvent.KEYCODE_D, new MoverJugadorDerecha(nivel.getJugadores().get(0)));


//        if (nivel.getJugadores().size()>1) {
//            controladores.put(KeyEvent.KEYCODE_DPAD_UP, new MoverJugadorArriba(nivel.getJugadores().get(1)));
//            controladores.put(KeyEvent.KEYCODE_DPAD_DOWN, new MoverJugadorAbajo(nivel.getJugadores().get(1)));
//            controladores.put(KeyEvent.KEYCODE_DPAD_LEFT, new MoverJugadorIzquierda(nivel.getJugadores().get(1)));
//            controladores.put(KeyEvent.KEYCODE_DPAD_RIGHT, new MoverJugadorDerecha(nivel.getJugadores().get(1)));
//        }
    }

    public void actualizar(long tiempo) throws Exception {
        nivel.actualizar(tiempo);
    }

    protected void dibujar(Canvas canvas) {
        nivel.dibujar(canvas);
        pad.dibujar(canvas);
        botonBomba.dibujar(canvas);
    }


    public void procesarEventosTouch() {
        boolean pulsacionPadMover = false;
        for (int i = 0; i < 6; i++) {
            if (accion[i] != NO_ACTION) {
                if (accion[i] == ACTION_DOWN && nivel.nivelPausado) {
                    nivel.nivelPausado = false;
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
                        // TODO

                        Jugador jugador = nivel.getJugadorTactil();
                        if (jugador.bombasColocadas < jugador.bombasLimite) {
                            Bomba b = new Bomba(context, jugador);
                            nivel.bombas.add(b);
                        }

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

        procesarEventosTouch();
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
        ControladorJugaror accion = controladores.get(keyCode);
        if (accion != null)
            accion.keyUp();
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ControladorJugaror accion = controladores.get(keyCode);
        Log.e("GAMEVIEW", "-------------  Pulsación tecla:" + keyCode);
        if (accion != null)
            accion.keyDown();
        return super.onKeyUp(keyCode, event);
    }
}

