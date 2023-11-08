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
            musicPlayer.stop();
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
            fxPlayer.start();
        }
    }
    public static void clickSound(Context c)
    {
        makeASound(c, R.raw.click);

//        //If we want sound, we make sound
//        if (sound)
//        {
//            fxPlayer = MediaPlayer.create(c, R.raw.click);
//            fxPlayer.start();
//        }

    }
    public static void correctSound(Context c)
    {
        makeASound(c, R.raw.correct);
//        if (sound)
//        {
//            fxPlayer = MediaPlayer.create(c, R.raw.correct);
//            fxPlayer.start();
//        }
    }
    public static void wrongSound(Context c)
    {
        makeASound(c, R.raw.mistakemod);
//        if (sound)
//        {
//            fxPlayer = MediaPlayer.create(c, R.raw.mistakemod);
//            fxPlayer.start();
//        }
    }
    public static void endSound(Context c)
    {
        makeASound(c, R.raw.complete);
//        if (sound)
//        {
//            fxPlayer = MediaPlayer.create(c, R.raw.complete);
//            fxPlayer.start();
//        }
    }
    public static void vineBoom(Context c)
    {
        makeASound(c, R.raw.vineboom);
//        if (sound)
//        {
//            fxPlayer = MediaPlayer.create(c, R.raw.vineboom);
//            fxPlayer.start();
//        }

    }
    public static void storeProgress() {
        if(musicPlayer != null && musicPlayer.isPlaying())
            msec = musicPlayer.getCurrentPosition();
        else
            msec = 0;
    }
}
