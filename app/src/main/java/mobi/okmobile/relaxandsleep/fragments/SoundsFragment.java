package mobi.okmobile.relaxandsleep.fragments;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mobi.okmobile.relaxandsleep.R;
import mobi.okmobile.relaxandsleep.adapters.SelectedSoundAdapter;
import mobi.okmobile.relaxandsleep.adapters.SoundsAdapter;
import mobi.okmobile.relaxandsleep.models.Sounds;


public class SoundsFragment extends Fragment implements SoundsAdapter.SoundClickListener, SelectedSoundAdapter.SelectedSoundClickListener {

    private RecyclerView soundRecyclerView;
    private View view;
    private SoundsAdapter adapter;
    private SelectedSoundAdapter selectedSoundAdapter;
    private SoundPool soundPool;
    private ImageView bottomMenuPlayButton;
    private ImageButton bottomMenuTimerButton;
    private ImageButton bottomMenuButton;
    private RecyclerView soundPlayRecyclerView;
    private List<Sounds> soundsList = new ArrayList<>();
    private List<Sounds> selectedSoundsList = new ArrayList<>();
    private boolean isStop = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sounds, container, false);
        initializeViews();
        return view;
    }

    private AudioManager audioManager;
    float actualVolume;
    float maxVolume;
    float volume;
    private void initializeViews(){
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        actualVolume =(float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVolume =(float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume= actualVolume/maxVolume;
        soundPlayRecyclerView = view.findViewById(R.id.soundPlayRecyclerView);
        bottomMenuTimerButton = view.findViewById(R.id.bottomMenuTimerButton);
        bottomMenuButton = view.findViewById(R.id.bottomMenuButton);
        soundRecyclerView = view.findViewById(R.id.recyclerViewSounds);
        bottomMenuPlayButton = view.findViewById(R.id.bottomMenuPlayButton);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        soundRecyclerView.setLayoutManager(gridLayoutManager);
        initializeSoundList();
        adapter = new SoundsAdapter(getActivity(), soundsList, this);
        soundRecyclerView.setAdapter(adapter);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(4)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else{
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        bottomMenuPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlaying()){
                    manageTrackingSounds();
                }
            }
        });

        bottomMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedSoundsList.size() >= 1){
                    if(soundPlayRecyclerView.getVisibility() == View.VISIBLE) {
                        soundPlayRecyclerView.setVisibility(View.GONE);
                        bottomMenuButton.setImageResource(R.drawable.ic_top_arrow);
                    }
                    else {
                        soundPlayRecyclerView.setVisibility(View.VISIBLE);
                        bottomMenuButton.setImageResource(R.drawable.ic_down_arrow);
                    }
                }
                else
                    soundPlayRecyclerView.setVisibility(View.GONE);
            }
        });
//        loadSoundInPool();
    }

    private void loadSoundInPool(){
        for (Sounds sound : soundsList) {
            soundPool.load(getActivity(), sound.getSoundId(), 1);
        }
    }

    private void manageTrackingSounds(){
        Map<Integer,Sounds> trackingSounds = new HashMap<>();
        for (int i = 0; i < soundsList.size() ; i++) {
            if (soundsList.get(i).getPlayingId() != -1) {
                trackingSounds.put(i, soundsList.get(i));
            }
        }
        if(!isStop) {
            for (Sounds tracking : trackingSounds.values()) {
                soundPool.pause(tracking.getPlayingId());
            }
            bottomMenuPlayButton.setImageResource(R.drawable.ic_play);
            isStop = true;
        }
        else {
            bottomMenuPlayButton.setImageResource(R.drawable.ic_pause);
            for (int tracking : trackingSounds.keySet()) {
                int newValue = soundPool.load(getActivity(), soundsList.get(tracking).getSoundId(), 1);
                Log.i("TAG", "manageTrackingSounds: "+newValue);
                Log.i("TAG", "manageTrackingSounds: "+tracking);

                soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                        soundsList.get(tracking).setPlayingId(soundPool.play(soundsList.get(tracking).getSoundId(), volume, volume, 0, -1, 1));
                    }
                });
                soundPool.resume(soundsList.get(tracking).getSoundId());
            }
            isStop = false;
        }
        Log.i("TAG", "manageTrackingSounds: "+soundsList.get(0).getPlayingId());
        Log.i("TAG", "manageTrackingSounds: "+soundsList.get(3).getPlayingId());

    }

    private void initializeSoundList(){
        soundsList.add(new Sounds("Light Rain", R.drawable.rain, R.raw.nature_rain, -1));
        soundsList.add(new Sounds("Heavy Rain", R.drawable.rainy, R.raw.nature_heavy_rain, -1));
        soundsList.add(new Sounds("Thunder", R.drawable.thunder, R.raw.nature_thunder, -1));
        soundsList.add(new Sounds("Rain on Umbrella", R.drawable.umbrella, R.raw.rain_on_umbrella, -1));
        soundsList.add(new Sounds("Rain on Window", R.drawable.window, R.raw.rain_on_window, -1));
        soundsList.add(new Sounds("Snow", R.drawable.snowflake, R.raw.snow, -1));
        soundsList.add(new Sounds("Rain on Roof", R.drawable.roof, R.raw.rain_on_roof, -1));
        soundsList.add(new Sounds("Rain on Tent", R.drawable.tent, R.raw.rain_on_tent, -1));
        soundsList.add(new Sounds("Rain on Puddle", R.drawable.puddle, R.raw.rain_on_puddle, -1));
        soundsList.add(new Sounds("Ocean", R.drawable.wave, R.raw.ocean, -1));
        soundsList.add(new Sounds("Lake", R.drawable.lake, R.raw.ocean, -1));
        soundsList.add(new Sounds("Forest", R.drawable.forest, R.raw.nature_forest, -1));
        soundsList.add(new Sounds("Wind Leaves", R.drawable.leaffall, R.raw.wind_leaves, -1));
        soundsList.add(new Sounds("Wind", R.drawable.wind, R.raw.nature_wind, -1));
        soundsList.add(new Sounds("Waterfall", R.drawable.waterfall, R.raw.waterfall, -1));
        soundsList.add(new Sounds("Drip", R.drawable.drops, R.raw.nature_dripping, -1));
        soundsList.add(new Sounds("Underwater", R.drawable.fish, R.raw.under_water, -1));
        soundsList.add(new Sounds("Farm", R.drawable.farmer, R.raw.farm, -1));
        soundsList.add(new Sounds("Grassland", R.drawable.grassland, R.raw.grassland, -1));
        soundsList.add(new Sounds("Fire", R.drawable.bonfire, R.raw.nature_firewood, -1));
        soundsList.add(new Sounds("Bird", R.drawable.bird, R.raw.animal_bird, -1));
        soundsList.add(new Sounds("Bird 2", R.drawable.bird, R.raw.bird2, -1));
        soundsList.add(new Sounds("Seagull", R.drawable.seagull, R.raw.seagull, -1));
        soundsList.add(new Sounds("Frog", R.drawable.frog, R.raw.animal_frog, -1));
        soundsList.add(new Sounds("Cricket", R.drawable.cricket, R.raw.animal_cricket, -1));
        soundsList.add(new Sounds("Cicada", R.drawable.cicada, R.raw.cicada, -1));
        soundsList.add(new Sounds("Wolf", R.drawable.wolf, R.raw.wolf, -1));
        soundsList.add(new Sounds("Cat Purring", R.drawable.cat, R.raw.cat_purring, -1));
        soundsList.add(new Sounds("Whale", R.drawable.whale, R.raw.whale, -1));
        soundsList.add(new Sounds("Owl", R.drawable.owl, R.raw.animal_owl, -1));
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSoundClick(int position) {
        boolean loaded = false;
        if(soundsList.get(position).getPlayingId() != -1) {
            soundPool.stop(soundsList.get(position).getPlayingId());
            selectedSoundsList.remove(soundsList.get(position));
            //Adapter Clear and Full Again
            soundPlayRecyclerView.setAdapter(null);
            selectedSoundAdapter = new SelectedSoundAdapter(getActivity(), selectedSoundsList, this);
            soundPlayRecyclerView.setAdapter(selectedSoundAdapter);
            // or just use recyclerView.post() or [Fragment]getView().post()
            // instead, but make sure views haven't been destroyed while you were
            // parsing
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
//                            adapter.notifyDataSetChanged();
                    adapter.notifyItemChanged(position);
                    soundRecyclerView.smoothScrollToPosition(position);
                }
            });
            if(selectedSoundsList.size() >= 1) {
                soundPlayRecyclerView.setVisibility(View.VISIBLE);
                bottomMenuButton.setImageResource(R.drawable.ic_down_arrow);
            }
            else {
                soundPlayRecyclerView.setVisibility(View.GONE);
                bottomMenuButton.setImageResource(R.drawable.ic_top_arrow);
            }
            soundsList.get(position).setPlayingId(-1);
            if(isPlaying())
                bottomMenuPlayButton.setImageResource(R.drawable.ic_pause);
            else
                bottomMenuPlayButton.setImageResource(R.drawable.ic_play);
            soundRecyclerView.setAdapter(null);
            adapter = new SoundsAdapter(getActivity(), soundsList, this);
            soundRecyclerView.setAdapter(adapter);
            // or just use recyclerView.post() or [Fragment]getView().post()
            // instead, but make sure views haven't been destroyed while you were
            // parsing
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
//                    adapter.notifyDataSetChanged();
                    adapter.notifyItemChanged(position);
                    soundRecyclerView.smoothScrollToPosition(position);
                }
            });
        }
        else {
            int sound = soundPool.load(getActivity(), soundsList.get(position).getSoundId(), 1);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                    soundsList.get(position).setPlayingId(soundPool.play(sound, 1, 1, 0, -1, 1));
                    selectedSoundsList.add(soundsList.get(position));
                    if(isPlaying())
                        bottomMenuPlayButton.setImageResource(R.drawable.ic_pause);
                    else
                        bottomMenuPlayButton.setImageResource(R.drawable.ic_play);
                    soundRecyclerView.setAdapter(null);
                    adapter = new SoundsAdapter(getActivity(), soundsList, SoundsFragment.this);
                    soundRecyclerView.setAdapter(adapter);
                    // or just use recyclerView.post() or [Fragment]getView().post()
                    // instead, but make sure views haven't been destroyed while you were
                    // parsing
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        public void run() {
//                            adapter.notifyDataSetChanged();
                            adapter.notifyItemChanged(position);
                            soundRecyclerView.smoothScrollToPosition(position);
                        }
                    });
                    soundPlayRecyclerView.setVisibility(View.VISIBLE);
                    soundPlayRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    selectedSoundAdapter = new SelectedSoundAdapter(getActivity(), selectedSoundsList, SoundsFragment.this::onSelectedSoundClick);
                    soundPlayRecyclerView.setAdapter(selectedSoundAdapter);
                    bottomMenuButton.setImageResource(R.drawable.ic_down_arrow);
                }
            });
        }
    }

    private boolean isPlaying(){
        int count = 0;
        for(Sounds sound : soundsList){
            if(sound.getPlayingId() != -1) {
                count++;
                break;
            }
        }
        if(count > 0){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onFailedSoundClick() {
        Toast.makeText(getActivity(), "You cannot play more than 4 sound.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }

    @Override
    public void onSelectedSoundClick(int deletedIconMusicId, int selectedMusicValue) {
        int i = 0;
        Sounds deletedSound;
        int soundValue = 0;
        for(Sounds sound : soundsList){
            if(sound.getPlayingId() == deletedIconMusicId){
                 deletedSound = sound;
                 soundValue = i;
            }
            i++;
        }
        soundPool.stop(soundsList.get(soundValue).getPlayingId());
        selectedSoundsList.remove(soundsList.get(selectedMusicValue));
        //Adapter Clear and Full Again
        soundPlayRecyclerView.setAdapter(null);
        selectedSoundAdapter = new SelectedSoundAdapter(getActivity(), selectedSoundsList, this);
        soundPlayRecyclerView.setAdapter(selectedSoundAdapter);

        int finalSoundValue = soundValue;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
//                            adapter.notifyDataSetChanged();
                adapter.notifyItemChanged(finalSoundValue);
                soundRecyclerView.smoothScrollToPosition(finalSoundValue);
            }
        });
        if(selectedSoundsList.size() >= 1) {
            soundPlayRecyclerView.setVisibility(View.VISIBLE);
            bottomMenuButton.setImageResource(R.drawable.ic_down_arrow);
        }
        else {
            soundPlayRecyclerView.setVisibility(View.GONE);
            bottomMenuButton.setImageResource(R.drawable.ic_top_arrow);
        }
        soundsList.get(finalSoundValue).setPlayingId(-1);
        if(isPlaying())
            bottomMenuPlayButton.setImageResource(R.drawable.ic_pause);
        else
            bottomMenuPlayButton.setImageResource(R.drawable.ic_play);
        soundRecyclerView.setAdapter(null);
        adapter = new SoundsAdapter(getActivity(), soundsList, this);
        soundRecyclerView.setAdapter(adapter);
        // or just use recyclerView.post() or [Fragment]getView().post()
        // instead, but make sure views haven't been destroyed while you were
        // parsing
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
//                    adapter.notifyDataSetChanged();
                adapter.notifyItemChanged(finalSoundValue);
                soundRecyclerView.smoothScrollToPosition(finalSoundValue);
            }
        });
    }
}