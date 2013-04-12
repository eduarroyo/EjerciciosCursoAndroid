package com.example.asteroides;

import java.util.Vector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class VistaJuego extends View {

	// //// THREAD Y TIEMPO //////
	private ThreadJuego thread = new ThreadJuego(); // Thread encargado de
													// procesar el juego
	private static int PERIODO_PROCESO = 50; // Cada cuanto queremos procesar
												// cambios (ms)
	private long ultimoProceso = 0; // Cuando se realizó el último proceso

	// //// NAVE //////
	private Grafico nave;// Gráfico de la nave
	private int giroNave; // Incremento de dirección
	private float aceleracionNave; // aumento de velocidad
	private static final int PASO_GIRO_NAVE = 5; // Incremento estándar de giro
													// y aceleración
	private static final float PASO_ACELERACION_NAVE = 0.5f;

	// //// ASTEROIDES //////
	private Vector<Grafico> Asteroides; // Vector con los Asteroides
	private int numAsteroides = 5; // Número inicial de asteroides
	private int numFragmentos = 3; // Fragmentos en que se divide

	public VistaJuego(Context context, AttributeSet attrs) {
		super(context, attrs);

		// Para poder ver VistaJuego en el editor gráfico de Eclipse
		if (!isInEditMode()) {
			isInEditMode();
		}

		Drawable drawableNave, drawableAsteroide, drawableMisil;

		drawableNave = context.getResources().getDrawable(R.drawable.nave);
		nave = new Grafico(this, drawableNave);

		drawableAsteroide = context.getResources().getDrawable(
				R.drawable.asteroide1);
		Asteroides = new Vector<Grafico>();
		for (int i = 0; i < numAsteroides; i++) {
			Grafico asteroide = new Grafico(this, drawableAsteroide);
			asteroide.setIncY(Math.random() * 4 - 2);
			asteroide.setIncX(Math.random() * 4 - 2);
			asteroide.setAngulo((int) (Math.random() * 360));
			asteroide.setRotacion((int) (Math.random() * 8 - 4));
			Asteroides.add(asteroide);
		}
	}

	@Override
	protected void onSizeChanged(int ancho, int alto, int ancho_anter,
			int alto_anter) {
		super.onSizeChanged(ancho, alto, ancho_anter, alto_anter);

		nave.setPosX((ancho - nave.getAncho()) / 2);
		nave.setPosY((alto - nave.getAlto()) / 2);

		// Una vez que conocemos nuestro ancho y alto.
		for (Grafico asteroide : Asteroides) {
			do {
				asteroide.setPosX(Math.random()
						* (ancho - asteroide.getAncho()));
				asteroide.setPosY(Math.random() * (alto - asteroide.getAlto()));
			} while (asteroide.distancia(nave) < (ancho + alto) / 5);
		}

		ultimoProceso = System.currentTimeMillis();
		thread.start();

	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		nave.dibujaGrafico(canvas);
		for (Grafico asteroide : Asteroides) {
			asteroide.dibujaGrafico(canvas);
		}
	}

	protected synchronized void actualizaFisica() {
		long ahora = System.currentTimeMillis();
		// No hagas nada si el período de proceso no se ha cumplido.
		if (ultimoProceso + PERIODO_PROCESO > ahora) {
			return;
		}

		// Para una ejecución en tiempo real calculamos retardo
		double retardo = (ahora - ultimoProceso) / PERIODO_PROCESO;
		ultimoProceso = ahora; // Para la próxima vez
		// Actualizamos velocidad y dirección de la nave a partir de
		// giroNave y aceleracionNave (según la entrada del jugador)
		nave.setAngulo((int) (nave.getAngulo() + giroNave * retardo));
		double nIncX = nave.getIncX() + aceleracionNave
				* Math.cos(Math.toRadians(nave.getAngulo())) * retardo;
		double nIncY = nave.getIncY() + aceleracionNave
				* Math.sin(Math.toRadians(nave.getAngulo())) * retardo;
		// Actualizamos si el módulo de la velocidad no excede el máximo
		if (Math.hypot(nIncX, nIncY) <= Grafico.getMaxVelocidad()) {
			nave.setIncX(nIncX);
			nave.setIncY(nIncY);
		}
		// Actualizamos posiciones X e Y
		nave.incrementaPos(retardo);
		for (Grafico asteroide : Asteroides) {
			asteroide.incrementaPos(retardo);
		}
	}

	class ThreadJuego extends Thread {
		@Override
		public void run() {
			while (true) {
				actualizaFisica();
			}
		}
	}
}