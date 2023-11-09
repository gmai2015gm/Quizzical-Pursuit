package com.example.quizzicalpursuit;

import android.content.Context;
import android.media.MediaPlayer;

public class GameSounds {
    private static MediaPlayer fxPlayer; //Our player for the SFX
    private static MediaPlayer musicPlayer; //Our Player for the music
    static boolean music = true; //Whether or not we want music
    static boolean sound = true; //Whether or not we want sounds
    static int msec = 0;
    public static void playMusic(Context c)
    {
        //Make sure that we want music, and music does not already exist
        if (music && (musicPlayer == null || !musicPlayer.isPlaying()))
        {
            musicPlayer = MediaPlayer.create(c, R.raw.lofi_loops);

            musicPlayer.setLooping(true);
            musicPlayer.start();
            musicPlayer.seekTo(msec);
        }
    }
    public static void stopMusic()
    {
        //Make sure it's playing and it exists
        if (musicPlayer != null && musicPlayer.isPlaying())
        {
            musicPlayer.stop();
        }

    }

    public static void pauseMusic()
    {
        //Make sure it's playing and it exists
        if (musicPlayer != null && musicPlayer.isPlaying())
            musicPlayer.pause();

    }
    private static void makeASound(Context c, int id)
    {
        //If we want sound, we make sound
        if (sound)
        {
            fxPlayer = MediaPlayer.create(c, id);
            fxPlayer.setVolume(10000, 10000);
            fxPlayer.start();

            fxPlayer.setOnCompletionListener(mediaPlayer -> {
                fxPlayer.release();
            });
        }
    }
    public static void clickSound(Context c)
    {
        makeASound(c, R.raw.click);

    }
    public static void correctSound(Context c)
    {
        makeASound(c, R.raw.correct);

    }
    public static void wrongSound(Context c)
    {
        makeASound(c, R.raw.mistakemod);

    }
    public static void endSound(Context c)
    {
        makeASound(c, R.raw.complete);

    }
    public static void vineBoom(Context c)
    {
        makeASound(c, R.raw.vineboom);
    }
    public static void startSound(Context c)
    {
        makeASound(c, R.raw.newgame);
    }
    public static void storeProgress() {
        if(musicPlayer != null && musicPlayer.isPlaying())
            msec = musicPlayer.getCurrentPosition();
        else
            msec = 0;
    }
}
