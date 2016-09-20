package ar.com.universitas.clapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
/**
 * Created by Julian on 12/06/2016.
 *2333
 *
 *
 * ESTO ES PARA PROBAR EL COMMITEO para Cesar
 */
public class InicioLogin extends Activity {

        ImageView clapplogoinic;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.iniciologin);

            clapplogoinic = (ImageView) findViewById(R.id.clapplogoini);

        }

        public void onClickclapplogoinic (View view){
            Intent i = new Intent(this,Login.class);
            startActivity(i);
        }

}