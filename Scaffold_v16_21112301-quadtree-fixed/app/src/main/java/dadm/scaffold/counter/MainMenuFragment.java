package dadm.scaffold.counter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import dadm.scaffold.sound.SoundManager;


public class MainMenuFragment extends BaseFragment implements View.OnClickListener {


    private int arrayPlanes[] = new int[3];
    private int imagenActual = 0;
    public SharedPreferences settings2;

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

        settings2 = getActivity().getSharedPreferences("soundManager",0);

        view.findViewById(R.id.btn_start).setOnClickListener(this);

        ImageView btnSound = (ImageView) getView().findViewById(R.id.imageView3);
        ImageView btnMusic = (ImageView) getView().findViewById(R.id.imageView);


        if(settings2.getBoolean("Sound",true)){

            btnSound.setImageResource(R.drawable.ic_baseline_volume_up_24);

        }else if(settings2.getBoolean("Sound",false)){

            btnSound.setImageResource(R.drawable.ic_baseline_volume_off_24);
        }

        if(settings2.getBoolean("Music",true)){

            btnMusic.setImageResource(R.drawable.notamusical);

        }else if(settings2.getBoolean("Music",false)){

            btnMusic.setImageResource(R.drawable.notamusicalmuted);
        }



        SharedPreferences settings = getActivity().getSharedPreferences("planeSelected", 0);

        Animation pulseAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.button_pulse);
        view.findViewById(R.id.btn_start).startAnimation(pulseAnimation);

        Animation titleAnimation1 = AnimationUtils.loadAnimation(getActivity(),
                R.anim.title_enter);
        view.findViewById(R.id.img_title).startAnimation(titleAnimation1);

        //view.findViewById(R.id.imageView2).startAnimation(pulseAnimation);

        arrayPlanes[0]= R.drawable.avioningame;
        arrayPlanes[1]= R.drawable.ave1;
        arrayPlanes[2]= R.drawable.ave2;

        imagenActual = settings.getInt("img1",R.drawable.avioningame);
        if(imagenActual == R.drawable.avioningame){
            imagenActual = 0;
        }else if(R.drawable.ave1 == imagenActual){
            imagenActual = 1;
        }else{
            imagenActual = 2;
        }

        ImageView imgAvion = (ImageView) getView().findViewById(R.id.imageView2);
        ImageButton btnDesplazarDcha = (ImageButton) getView().findViewById(R.id.buttonAvion);
        ImageButton btnDesplazarIzq = (ImageButton) getView().findViewById(R.id.buttonAvion2);

        if(settings.contains("img1")){

            imgAvion.setImageResource(settings.getInt("img1",R.drawable.avioningame));

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
                editor.putInt("img1", arrayPlanes[imagenActual]);
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
                editor.putInt("img1", arrayPlanes[imagenActual]);
                // Commit the edits!
                editor.commit();
            }
        });

        //BOTON SILENCIAR FX----------------------------------------------------------
        ImageButton btnMuteFX = (ImageButton) getView().findViewById(R.id.buttonMuteSound);
        btnMuteFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.getId() == R.id.buttonMuteSound) {
                    SoundManager soundManager = ((ScaffoldActivity) getActivity()).getSoundManager();
                    soundManager.toggleSoundStatus();
                    if (soundManager.getSoundStatus()) {

                        btnSound.setImageResource(R.drawable.ic_baseline_volume_up_24);

                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("Sound", true);
                        editor.commit();
                    } else {
                        btnSound.setImageResource(R.drawable.ic_baseline_volume_off_24);

                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("Sound", false);
                        editor.commit();
                    }

                }

            }
        });

        //BOTON SILENCIAR MUSICA--------------------------------------------
        ImageButton btnMuteMusic = (ImageButton) getView().findViewById(R.id.buttonMuteMusic);
        btnMuteMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.getId() == R.id.buttonMuteMusic) {
                    SoundManager soundManager = ((ScaffoldActivity) getActivity()).getSoundManager();
                    soundManager.toggleMusicStatus();
                    //updateMusicButton();
                    if (soundManager.getMusicStatus()) {
                        btnMusic.setImageResource(R.drawable.notamusical);

                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("Music", true);
                        editor.commit();
                    } else {
                        btnMusic.setImageResource(R.drawable.notamusicalmuted);

                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("Music", false);
                        editor.commit();
                    }

                }

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
        if (v.getId() == R.id.btn_start){
            ((ScaffoldActivity) getActivity()).startGame();
        }

    }

    private void updateSoundButtons() {
        SoundManager soundManager = ((ScaffoldActivity) getActivity()).getSoundManager();

        ImageView btnSound = (ImageView) getView().findViewById(R.id.imageView3);
        if (soundManager.getMusicStatus()) {
            btnSound.setImageResource(R.drawable.ic_baseline_volume_up_24);

            SharedPreferences.Editor editor = settings2.edit();
            editor.putBoolean("Sound", true);
            editor.commit();
        } else {
            btnSound.setImageResource(R.drawable.ic_baseline_volume_off_24);

            SharedPreferences.Editor editor = settings2.edit();
            editor.putBoolean("Sound", false);
            editor.commit();
        }
    }
    private void updateMusicButtons(){
        SoundManager soundManager = ((ScaffoldActivity) getActivity()).getSoundManager();

        ImageView btnMusic = (ImageView) getView().findViewById(R.id.imageView);
        if (soundManager.getSoundStatus()) {
            btnMusic.setImageResource(R.drawable.notamusical);

            SharedPreferences.Editor editor = settings2.edit();
            editor.putBoolean("Music", true);
            editor.commit();
        }
        else {
            btnMusic.setImageResource(R.drawable.notamusicalmuted);

            SharedPreferences.Editor editor = settings2.edit();
            editor.putBoolean("Music", false);
            editor.commit();
        }
    }

}
