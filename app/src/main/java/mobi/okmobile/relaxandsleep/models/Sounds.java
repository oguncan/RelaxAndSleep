package mobi.okmobile.relaxandsleep.models;

public class Sounds {
    private String soundName;
    private int iconName;
    private int soundId;
    private int playingId;

    public Sounds(String soundName, int iconName, int soundId, int playingId) {
        this.soundName = soundName;
        this.iconName = iconName;
        this.soundId = soundId;
        this.playingId = playingId;
    }

    public int getPlayingId() {
        return playingId;
    }

    public void setPlayingId(int playingId) {
        this.playingId = playingId;
    }

    public int getSoundId() {
        return soundId;
    }

    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public int getIconName() {
        return iconName;
    }

    public void setIconName(int iconName) {
        this.iconName = iconName;
    }
}
