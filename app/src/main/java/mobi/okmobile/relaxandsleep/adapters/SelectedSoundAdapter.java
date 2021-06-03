package mobi.okmobile.relaxandsleep.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mobi.okmobile.relaxandsleep.R;
import mobi.okmobile.relaxandsleep.models.Sounds;

public class SelectedSoundAdapter extends RecyclerView.Adapter<SelectedSoundAdapter.SelectedSoundViewHolder>{

    private Context mContext;
    private List<Sounds> selectedSounds;
    private List<Sounds> allSounds;
    private SelectedSoundClickListener soundClickListener;

    public SelectedSoundAdapter(Context mContext, List<Sounds> selectedSounds, SelectedSoundClickListener soundClickListener) {
        this.mContext = mContext;
        this.selectedSounds = selectedSounds;
        this.soundClickListener = soundClickListener;
    }

    @NonNull
    @Override
    public SelectedSoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_selected_sounds_item, parent, false);
        return new SelectedSoundViewHolder(view, soundClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectedSoundViewHolder holder, int position) {
        holder.bind(selectedSounds.get(position));
    }

    @Override
    public int getItemCount() {
        return selectedSounds.size();
    }

    public class SelectedSoundViewHolder extends RecyclerView.ViewHolder {
        private ImageView soundIconImageView;
        private SeekBar selectedSoundSeekBar;
        private ConstraintLayout selectedDeleteIcon;
        SelectedSoundClickListener soundClickListener;
        public SelectedSoundViewHolder(@NonNull View itemView, SelectedSoundClickListener soundClickListener) {
            super(itemView);
            soundIconImageView = itemView.findViewById(R.id.soundIconImageView);
            selectedSoundSeekBar = itemView.findViewById(R.id.selectedSoundSeekBar);
            selectedDeleteIcon = itemView.findViewById(R.id.selectedDeleteIcon);
            this.soundClickListener = soundClickListener;
        }

        void bind(final Sounds sound){
            soundIconImageView.setImageResource(sound.getIconName());
            selectedDeleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedSounds.remove(sound);
                    soundClickListener.onSelectedSoundClick(sound.getPlayingId(), getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }
    }

    public interface SelectedSoundClickListener {
        void onSelectedSoundClick(int deletedIconMusicId, int selectedMusicValue);
    }
}
