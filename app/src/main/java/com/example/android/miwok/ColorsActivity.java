package com.example.android.miwok;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    private AudioFocusRequest mFocusRequest;
    private AudioAttributes mPlaybackAttributes;
    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {
                        case AudioManager.AUDIOFOCUS_GAIN:
                            mMediaPlayer.start();
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS:
                            releaseMediaPlayer();
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            mMediaPlayer.pause();
                            mMediaPlayer.seekTo(0);
                            break;
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            // ... pausing or ducking depends on your app
                            mMediaPlayer.pause();
                            mMediaPlayer.seekTo(0);
                            break;
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);


        //gain the AudioManger by context.getSystemService(Context.AUDIO_SERVICE)
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        //set the attributes of the audio with the type and content with the builder
        mPlaybackAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        /*
         ** define the required parameter AudioFocusRequest for AudioManager call method
         * AudioManager.requestAudioFocus
         */


        mFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                .setAudioAttributes(mPlaybackAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener(mOnAudioFocusChangeListener)//the listener is called while the status of audio focus is changing
                .build();

        final ArrayList<Word> words = new ArrayList<>();

        words.add(new Word("black", "kululli", R.drawable.color_black, R.raw.color_black));
        words.add(new Word("brown", "ṭakaakki", R.drawable.color_brown, R.raw.color_brown));
        words.add(new Word("dusty_yellow", "ṭopiisə", R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow));
        words.add(new Word("gray", "ṭopoppi", R.drawable.color_gray, R.raw.color_gray));
        words.add(new Word("green", "chokokki", R.drawable.color_green, R.raw.color_green));
        words.add(new Word("mustard_yellow", "chiwiiṭə", R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow));
        words.add(new Word("red", "weṭeṭṭi", R.drawable.color_red, R.raw.color_red));
        words.add(new Word("white", "kelelli", R.drawable.color_white, R.raw.color_white));

        // Create an {@link ArrayAdapter}, whose data source is a list of Strings. The
        // adapter knows how to create layouts for each item in the list, using the
        // simple_list_item_1.xml layout resource defined in the Android framework.
        // This list item layout contains a single {@link TextView}, which the adapter will set to
        // display a single word.
        WordAdapter itemsAdapter =
                new WordAdapter(this, words, R.color.category_colors);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml file.
        ListView listView = (ListView) findViewById(R.id.list);

        // Make the {@link ListView} use the {@link ArrayAdapter} we created above, so that the
        // {@link ListView} will display list items for each word in the list of words.
        // Do this by calling the setAdapter method on the {@link ListView} object and pass in
        // 1 argument, which is the {@link ArrayAdapter} with the variable name itemsAdapter.
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//release the media player if it currently exits because we're about to play a different sound file.
                releaseMediaPlayer();

                //request audio focus for playback
                int result = mAudioManager.requestAudioFocus(mFocusRequest);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //create and set up the{@Link MediaPlayer} for the audio resource associated with the current word
                    mMediaPlayer = MediaPlayer.create(ColorsActivity.this, words.get(i).getSourceAudio());

                    //start play
                    mMediaPlayer.start();

                    mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            releaseMediaPlayer();
                        }
                    });
                }
            }
        });


    }

    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
            //mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
