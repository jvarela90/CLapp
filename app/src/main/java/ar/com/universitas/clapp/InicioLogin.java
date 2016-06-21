package ar.com.universitas.clapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Julian on 19/06/2016.
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
            Intent i = new Intent(this,MainLogin.class);
            startActivity(i);
        }

}
