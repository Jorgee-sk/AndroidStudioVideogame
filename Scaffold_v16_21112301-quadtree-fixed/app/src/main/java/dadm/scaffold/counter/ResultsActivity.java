package dadm.scaffold.counter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        int puntuacion = getIntent().getExtras().getInt("puntuacion");


        TextView intPuntuacion = (TextView) findViewById(R.id.intPuntuacion);
        intPuntuacion.setText(puntuacion + "");



        ImageButton btnMainMenu = (ImageButton) findViewById(R.id.ButtonMainMenu);
        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), ScaffoldActivity.class);
                startActivity(intent);


            }


        });





    }
}