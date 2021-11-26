package dadm.scaffold.counter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.sound.SoundManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PausaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PausaFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PausaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PausaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PausaFragment newInstance(String param1, String param2) {
        PausaFragment fragment = new PausaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pausa, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {


        //BOTON EXIT---------------------------------------------------
        ImageButton btnExit = (ImageButton) getView().findViewById(R.id.ExitButton);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ((ScaffoldActivity)getActivity()).theGameEngine.stopGame();
                ((ScaffoldActivity)getActivity()).doublenavigateBack(); //Cerramos fragmento propio y el de juego


            }
            });


        //BOTON RESUME---------------------------------------------------
        ImageButton btnResume = (ImageButton) getView().findViewById(R.id.ResumeButton);
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ((ScaffoldActivity)getActivity()).theGameEngine.resumeGame();
                ((ScaffoldActivity)getActivity()).navigateBack(); //Cerramos fragmento propio y el de juego


            }
        });
        //BOTON SILENCIAR FX----------------------------------------------------------
        ImageButton btnMuteFX = (ImageButton) getView().findViewById(R.id.buttonSound);
        btnMuteFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.getId() == R.id.buttonMuteSound) {
                    SoundManager soundManager = ((ScaffoldActivity) getActivity()).getSoundManager();
                    soundManager.toggleMusicStatus();
                    updateSoundAndMusicButtons();
                }


            }
        });
        //BOTON SILENCIAR MUSICA--------------------------------------------
        ImageButton btnMuteMusic = (ImageButton) getView().findViewById(R.id.buttonSound2);
        btnMuteMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (view.getId() == R.id.buttonMuteMusic) {
                    SoundManager soundManager = ((ScaffoldActivity) getActivity()).getSoundManager();
                    soundManager.toggleMusicStatus();
                    updateSoundAndMusicButtons();
                }

            }
        });







    }//FIN ON CREATE VIEW


    private void updateSoundAndMusicButtons() {
        SoundManager soundManager = ((ScaffoldActivity) getActivity()).getSoundManager();

        ImageView btnMusic = (ImageView) getView().findViewById(R.id.imageSoundFX);
        if (soundManager.getMusicStatus()) {
            btnMusic.setImageResource(R.drawable.ic_baseline_volume_up_24);
        }
        else {
            btnMusic.setImageResource(R.drawable.ic_baseline_volume_off_24);
        }

        ImageView btnSounds = (ImageView) getView().findViewById(R.id.imageMusic);
        if (soundManager.getSoundStatus()) {
            btnSounds.setImageResource(R.drawable.notamusical);
        }
        else {
            btnSounds.setImageResource(R.drawable.notamusicalmuted);
        }


    }

}


