package ar.com.universitas.clapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

/**
 * Created by Chinuxs on 14/12/2016.
 */

public class DescripcionRecetas extends AppCompatActivity {

    Button btnvolver;
    TextView descripcionRecetaIndividual;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descripcionreceta);
        btnvolver = (Button) findViewById(R.id.btnvolver);

        descripcionRecetaIndividual = (TextView) findViewById(R.id.DescriptionRecetaIndividual);

        //SharedPreferences
        SharedPreferences sp1 = getSharedPreferences("DescripcionReceta",MODE_PRIVATE);
        String descripcion = sp1.getString("descripcion" , "0" );

        descripcionRecetaIndividual.setText(descripcion);

    }

    public void onClickbtnvolver (View view){
        Intent i = new Intent(this, Raprec.class);
        startActivity(i);

    }
}