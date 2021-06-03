package mobi.okmobile.relaxandsleep.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import mobi.okmobile.relaxandsleep.R;
import mobi.okmobile.relaxandsleep.models.Sounds;

public class SoundsAdapter extends RecyclerView.Adapter<SoundsAdapter.SoundsViewHolder> {

    private Context context;
    private List<Sounds> sounds = new ArrayList<>();
    private SoundClickListener soundClickListener;


    public SoundsAdapter(Context context, List<Sounds> sounds, SoundClickListener soundClickListener) {
        Log.i("MainActivity", "SoundsAdapter: xd");
        this.context = context;
        this.sounds = sounds;
        this.soundClickListener = soundClickListener;
    }

    @NonNull
    @Override
    public SoundsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_sounds_item, parent, false);
        return new SoundsViewHolder(view, soundClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundsViewHolder holder, int position) {
        holder.soundIconImageView.setImageResource(sounds.get(position).getIconName());
        holder.soundTitle.setText(sounds.get(position).getSoundName());
        Log.i("MainActivity", "onBindViewHolder: "+sounds.get(position).getPlayingId());
        if(sounds.get(position).getPlayingId() != -1){
            holder.soundIcon.setBackground(context.getResources().getDrawable(R.drawable.bg_sounds_selected_item));
        }
        else {
            holder.soundIcon.setBackground(context.getResources().getDrawable(R.drawable.bg_sounds_not_selected_item));
        }
    }

    @Override
    public int getItemCount() {
        return sounds.size();
    }

    public class SoundsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView soundTitle;
        private ImageView soundIconImageView;
        private ViewGroup soundIcon;
        SoundClickListener soundClickListener;
        public SoundsViewHolder(@NonNull View itemView, SoundClickListener soundClickListener) {
            super(itemView);
            soundTitle = itemView.findViewById(R.id.soundTitle);
            soundIconImageView = itemView.findViewById(R.id.soundIconImageView);
            soundIcon = itemView.findViewById(R.id.soundIcon);
            this.soundClickListener = soundClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int count = 0;
            for(Sounds sound : sounds){
                if(sound.getPlayingId() != -1)
                    count++;
            }
            if(count < 4)
                soundClickListener.onSoundClick(getAdapterPosition());
            else{
                if(sounds.get(getAdapterPosition()).getPlayingId() != -1)
                    soundClickListener.onSoundClick(getAdapterPosition());
                else
                    soundClickListener.onFailedSoundClick();
            }
        }
    }

    public interface SoundClickListener {
        void onSoundClick(int position);
        void onFailedSoundClick();
    }
}
