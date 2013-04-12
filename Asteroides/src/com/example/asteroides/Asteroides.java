package com.example.asteroides;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Asteroides extends Activity {
	private Button bAcercaDe;
	private Button bPuntuaciones;
	
	public static AlmacenPuntuaciones almacen = new AlmacenPuntuacionesArray();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        bAcercaDe = (Button) findViewById(R.id.button1);
        bAcercaDe.setOnClickListener(new OnClickListener() {
    		public void onClick(View view) {
    			lanzarJuego(null);
    		}
        });
        
        bAcercaDe = (Button) findViewById(R.id.button3);
        bAcercaDe.setOnClickListener(new OnClickListener() {
    		public void onClick(View view) {
    			lanzarAcercaDe(null);
    		}
        });
        
        bPuntuaciones = (Button) findViewById(R.id.button4);
        bPuntuaciones.setOnClickListener(new OnClickListener() {
        	public void onClick(View view) {
        		lanzarPuntuaciones(null);
        	}
        });
	}
	
	public void onClickSalir(View view)
	{
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId()) {
			case R.id.acercaDe:
				lanzarAcercaDe(null);
				break;
			case R.id.config:
				lanzarPreferencias(null);
				break;
		}
		return true;
	}
	
	public void lanzarAcercaDe(View view) {
	    Intent i = new Intent(this, AcercaDe.class);
        startActivity(i);
    }
	
	public void lanzarPreferencias(View view) {
	    Intent i = new Intent(this, Preferencias.class);
        startActivity(i);
    }
	
	public void lanzarPuntuaciones(View view) {
		Intent i = new Intent(this, Puntuaciones.class);
		startActivity(i);
	}
	
	public void lanzarJuego(View view) {
		Intent i = new Intent(this, Juego.class);
		startActivity(i);
	}
}
