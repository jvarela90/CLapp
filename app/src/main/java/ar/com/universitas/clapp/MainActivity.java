package ar.com.universitas.clapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnarmilist;
    Button btnfutcom;
    Button btnmialm;
    Button btnraprec;

    /**
     * Created by Julian on 12/06/2016.
     *
     *456
     *
     * ESTO ES PARA PROBAR EL COMMITEO para Cesar
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnarmilist = (Button) findViewById(R.id.btnarmilist);
        btnfutcom = (Button) findViewById(R.id.btnfutcom);
        btnmialm = (Button) findViewById(R.id.btnmialm);
        btnraprec = (Button) findViewById(R.id.btnraprec);
    }


    public void OnClickbtnarmitist (View view){
        Intent i = new Intent(this,Armilist.class);
        startActivity(i);
    }

    public void OnClickbtnfutcom (View view){
        Intent i = new Intent(this,Futcom.class);
        startActivity(i);
    }

    public void OnClickbtnmialm (View view){
        Intent i = new Intent(this,MiAlmacen.class);
        startActivity(i);

    }

    public void OnClickbtnraprec (View view){
        Intent i = new Intent(this,Raprec.class);
        startActivity(i);

    }

}
