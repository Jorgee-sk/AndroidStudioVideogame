package dadm.scaffold.counter;

import android.content.DialogInterface;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageButton;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.engine.FramesPerSecondCounter;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameView;
import dadm.scaffold.engine.LivesCounter;
import dadm.scaffold.engine.ParallaxBackground;
import dadm.scaffold.engine.ScoreGameObject;
import dadm.scaffold.input.JoystickInputController;
import dadm.scaffold.space.GameController;
import dadm.scaffold.space.SpaceShipPlayer;


public class GameFragment extends BaseFragment implements View.OnClickListener {
    //private GameEngine theGameEngine;

    public GameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.btn_play_pause).setOnClickListener(this);
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout(){
                //Para evitar que sea llamado múltiples veces,
                //se elimina el listener en cuanto es llamado
                observer.removeOnGlobalLayoutListener(this);
                GameView gameView = (GameView) getView().findViewById(R.id.gameView);
                ((ScaffoldActivity)getActivity()).theGameEngine = new GameEngine(getActivity(), gameView);
                ((ScaffoldActivity)getActivity()).theGameEngine.setSoundManager(getScaffoldActivity().getSoundManager());
                ((ScaffoldActivity)getActivity()).theGameEngine.setTheInputController(new JoystickInputController(getView()));
                ((ScaffoldActivity)getActivity()).theGameEngine.addGameObject(new ParallaxBackground(((ScaffoldActivity)getActivity()).theGameEngine,
                        20,R.drawable.fondo));
                ((ScaffoldActivity)getActivity()).theGameEngine.addGameObject(new ScoreGameObject(
                        getView(),R.id.score_value));
                ((ScaffoldActivity)getActivity()).theGameEngine.addGameObject(new LivesCounter(
                        getView(),R.id.lives_value));
                //((ScaffoldActivity)getActivity()).theGameEngine.addGameObject(new SpaceShipPlayer( ((ScaffoldActivity)getActivity()).theGameEngine));
                ((ScaffoldActivity)getActivity()).theGameEngine.addGameObject(new FramesPerSecondCounter( ((ScaffoldActivity)getActivity()).theGameEngine));
                ((ScaffoldActivity)getActivity()).theGameEngine.addGameObject(new GameController( ((ScaffoldActivity)getActivity()).theGameEngine));
                ((ScaffoldActivity)getActivity()).theGameEngine.startGame();
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_play_pause) {
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if ( ((ScaffoldActivity)getActivity()).theGameEngine.isRunning()){
            pauseGameAndShowPauseDialog();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((ScaffoldActivity)getActivity()).theGameEngine.stopGame();
    }

    @Override
    public boolean onBackPressed() {
        if ( ((ScaffoldActivity)getActivity()).theGameEngine.isRunning()) {
            pauseGameAndShowPauseDialog();
            return true;
        }
        return false;
    }

    private void pauseGameAndShowPauseDialog() {
        ((ScaffoldActivity)getActivity()).theGameEngine.pauseGame();

        ((ScaffoldActivity)getActivity()).addFragment(new PausaFragment()); //CREAMOS FRAGMENTO DE PAUSA

/*
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.pause_dialog_title)
                .setMessage(R.string.pause_dialog_message)
                .setPositiveButton(R.string.resume, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((ScaffoldActivity)getActivity()).theGameEngine.resumeGame();
                    }
                })
                .setNegativeButton(R.string.stop, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((ScaffoldActivity)getActivity()).theGameEngine.stopGame();
                        ((ScaffoldActivity)getActivity()).navigateBack();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        ((ScaffoldActivity)getActivity()).theGameEngine.resumeGame();
                    }
                })
                .create()
                .show();
*/


    }

    private void playOrPause() {
        ImageButton button = (ImageButton) getView().findViewById(R.id.btn_play_pause);
        if ( ((ScaffoldActivity)getActivity()).theGameEngine.isPaused()) {
            ((ScaffoldActivity)getActivity()).theGameEngine.resumeGame();
            //button.setText(R.string.pause);
        }
        else {
            ((ScaffoldActivity)getActivity()).theGameEngine.pauseGame();
            //button.setText(R.string.resume);
        }
    }
}
