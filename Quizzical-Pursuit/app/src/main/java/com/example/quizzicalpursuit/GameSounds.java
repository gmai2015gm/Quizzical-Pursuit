package com.example.quizzicalpursuit;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;

public class GameSounds {
    static MediaPlayer fxPlayer;
    static MediaPlayer musicPlayer;
    static boolean music = true;
    static boolean sound = true;
    public static void playMusic(Context c)
    {
        if (music && (musicPlayer == null || !musicPlayer.isPlaying()))
        {
            musicPlayer = MediaPlayer.create(c, R.raw.lofi_loops);

            musicPlayer.setLooping(true);
            musicPlayer.start();
        }
    }
    public static void stopMusic()
    {
        if (musicPlayer != null && musicPlayer.isPlaying())
            musicPlayer.stop();
    }
    public static void clickSound(Context c)
    {
        if (sound)
        {
            fxPlayer = MediaPlayer.create(c, R.raw.click);
            fxPlayer.start();
        }

    }
    public static void correctSound(Context c)
    {
        if (sound)
        {
            fxPlayer = MediaPlayer.create(c, R.raw.correct);
            fxPlayer.start();
        }
    }
    public static void wrongSound(Context c)
    {
        if (sound)
        {
            fxPlayer = MediaPlayer.create(c, R.raw.mistakemod);
            fxPlayer.start();
        }
    }
    public static void endSound(Context c)
    {
        if (sound)
        {
            fxPlayer = MediaPlayer.create(c, R.raw.complete);
            fxPlayer.start();
        }
    }
    public static void vineBoom(Context c)
    {
        if (sound)
        {
            fxPlayer = MediaPlayer.create(c, R.raw.vineboom);
            fxPlayer.start();
        }

    }
}
