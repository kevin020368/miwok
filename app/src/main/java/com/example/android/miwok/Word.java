package com.example.android.miwok;

public class Word {
    private String mDefaultTranslation;
    private String mMiwokTranslation;
    private final int NO_IMAGE_PROVIDED = -1;
    private int mSourceImage = NO_IMAGE_PROVIDED;
    private int mSourceAudio;


    //constructor
    public Word(String defaultTranslation, String miwok, int sourceImage, int sourceAudio) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwok;
        mSourceImage = sourceImage;
        mSourceAudio = sourceAudio;
    }

    //constructor without image
    public Word(String defaultTranslation, String miwok) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwok;
    }

    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }

    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    public int getSourceImage() { return mSourceImage; }

    public boolean hasImage() {
        return mSourceImage != NO_IMAGE_PROVIDED;
    }

    public int getSourceAudio(){ return mSourceAudio; }
}
