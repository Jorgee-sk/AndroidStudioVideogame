package dadm.scaffold.counter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;


public class MainMenuFragment extends BaseFragment implements View.OnClickListener {

    private int arrayPlanes[] = new int[3];
    private int imagenActual = 0;


    public MainMenuFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_start).setOnClickListener(this);

        SharedPreferences settings = getActivity().getSharedPreferences("planeSelected", 0);

        Animation pulseAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.button_pulse);
        view.findViewById(R.id.btn_start).startAnimation(pulseAnimation);

        Animation titleAnimation1 = AnimationUtils.loadAnimation(getActivity(),
                R.anim.title_enter);
        view.findViewById(R.id.img_title).startAnimation(titleAnimation1);

        //view.findViewById(R.id.imageView2).startAnimation(pulseAnimation);

        arrayPlanes[0]= R.drawable.avion;
        arrayPlanes[1]= R.drawable.ave1;
        arrayPlanes[2]= R.drawable.ave2;

        imagenActual = settings.getInt("img1",0);

        ImageView imgAvion = (ImageView) getView().findViewById(R.id.imageView2);
        ImageButton btnDesplazarDcha = (ImageButton) getView().findViewById(R.id.buttonAvion);
        ImageButton btnDesplazarIzq = (ImageButton) getView().findViewById(R.id.buttonAvion2);

        if(settings.contains("img1")){

            imgAvion.setImageResource(arrayPlanes[settings.getInt("img1",0)]);

        }
        TypedValue typedvalue = new TypedValue();
        imgAvion.setBackgroundResource(typedvalue.resourceId);

        btnDesplazarDcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(imagenActual == 2)
                    imagenActual=-1;

                imagenActual++;
                if(settings.contains("img1")){

                    imgAvion.setImageResource(arrayPlanes[imagenActual]);

                }
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("img1", imagenActual);
                // Commit the edits!
                editor.commit();
            }
        });

        btnDesplazarIzq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(imagenActual == 0)
                    imagenActual=+3;

                imagenActual--;

                if(settings.contains("img1")){

                    imgAvion.setImageResource(arrayPlanes[imagenActual]);

                }
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("img1", imagenActual);
                // Commit the edits!
                editor.commit();
            }
        });



       /* Animation titleAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.title_enter);
        view.findViewById(R.id.GameTitle).startAnimation(titleAnimation);

        Animation subtitleAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.subtitle_enter);
        view.findViewById(R.id.GameSubtitle).startAnimation(subtitleAnimation);*/
    }

    @Override
    public void onClick(View v) {
        ((ScaffoldActivity)getActivity()).startGame();
    }
}
