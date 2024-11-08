package com.maunc.jetpackmvvm.utils;

import android.content.res.TypedArray;
import android.widget.ImageView;

import com.maunc.jetpackmvvm.BaseApp;

public class FrameAnimUtils {

    private static final int SELECTED_A = 1;
    private static final int SELECTED_B = 2;
    private static final int SELECTED_C = 3;
    private static final int SELECTED_D = 4;

    private AnimationListener mAnimationListener;

    private final ImageView mImageView;
    private final int[] mFrameRes;
    private final int defRes;
    private int mDuration; //每帧动画的播放间隔
    private int[] mDurations; //每帧动画的播放间隔数组
    private final int mLastFrame;
    private boolean mIsRepeat;
    private int mDelay; //下一遍动画播放的延迟时间

    private int mCurrentSelect;
    private int mCurrentFrame;
    private boolean mNext;
    private boolean mPause;

    /**
     * @param iv       播放动画的控件
     * @param frameRes 播放的图片数组
     * @param duration 每帧动画的播放间隔(毫秒)
     * @param isRepeat 是否循环播放
     */
    public FrameAnimUtils(ImageView iv, int frameRes, int defRes, int duration, boolean isRepeat) {
        this.mImageView = iv;
        this.mFrameRes = getRes(frameRes);
        this.defRes = defRes;
        this.mDuration = duration;
        this.mLastFrame = mFrameRes.length - 1;
        this.mIsRepeat = isRepeat;
        play(0);
    }

    /**
     * @param iv        播放动画的控件
     * @param frameRes  播放的图片数组
     * @param durations 每帧动画的播放间隔(毫秒)
     * @param isRepeat  是否循环播放
     */
    public FrameAnimUtils(ImageView iv, int frameRes, int defRes, int[] durations, boolean isRepeat) {
        this.mImageView = iv;
        this.mFrameRes = getRes(frameRes);
        this.defRes = defRes;
        this.mDurations = durations;
        this.mLastFrame = mFrameRes.length - 1;
        this.mIsRepeat = isRepeat;
        playByDurations(0);
    }

    /**
     * 循环播放动画
     *
     * @param iv       播放动画的控件
     * @param frameRes 播放的图片数组
     * @param duration 每帧动画的播放间隔(毫秒)
     * @param delay    循环播放的时间间隔
     */
    public FrameAnimUtils(ImageView iv, int frameRes, int defRes, int duration, int delay) {
        this.mImageView = iv;
        this.mFrameRes = getRes(frameRes);
        this.defRes = defRes;
        this.mDuration = duration;
        this.mDelay = delay;
        this.mLastFrame = mFrameRes.length - 1;
        playAndDelay(0);
    }

    /**
     * 循环播放动画
     *
     * @param iv        播放动画的控件
     * @param frameRes  播放的图片数组
     * @param durations 每帧动画的播放间隔(毫秒)
     * @param delay     循环播放的时间间隔
     */
    public FrameAnimUtils(ImageView iv, int frameRes, int defRes, int[] durations, int delay) {
        this.mImageView = iv;
        this.mFrameRes = getRes(frameRes);
        this.defRes = defRes;
        this.mDurations = durations;
        this.mDelay = delay;
        this.mLastFrame = mFrameRes.length - 1;
        playByDurationsAndDelay(0);
    }

    public void setAnimationListener(AnimationListener listener) {
        this.mAnimationListener = listener;
    }

    public void restartAnimation() {
        if (mPause) {
            mPause = false;
            switch (mCurrentSelect) {
                case SELECTED_A:
                    playByDurationsAndDelay(mCurrentFrame);
                    break;
                case SELECTED_B:
                    playAndDelay(mCurrentFrame);
                    break;
                case SELECTED_C:
                    playByDurations(mCurrentFrame);
                    break;
                case SELECTED_D:
                    play(mCurrentFrame);
                    break;
                default:
                    break;
            }
        }
    }

    public void pauseAnimation() {
        this.mPause = true;
    }

    public void release() {
        pauseAnimation();
        mCurrentFrame = 0;
        mImageView.setImageResource(defRes);
    }

    public boolean isPause() {
        return this.mPause;
    }

    /**
     * 获取需要播放的动画资源
     */
    private int[] getRes(int frameRes) {
        TypedArray typedArray = BaseApp.instance.getResources().obtainTypedArray(frameRes);
        int len = typedArray.length();
        int[] resId = new int[len];
        for (int i = 0; i < len; i++) {
            resId[i] = typedArray.getResourceId(i, -1);
        }
        typedArray.recycle();
        return resId;
    }

    private void play(final int i) {
        mImageView.postDelayed(() -> {
            if (mPause) {
                if (mPause) {
                    mCurrentSelect = SELECTED_D;
                    mCurrentFrame = i;
                    return;
                }
                return;
            }
            if (0 == i) {
                if (mAnimationListener != null) {
                    mAnimationListener.onAnimationStart();
                }
            }
            mImageView.setImageResource(mFrameRes[i]);
            if (i == mLastFrame) {
                if (mIsRepeat) {
                    if (mAnimationListener != null) {
                        mAnimationListener.onAnimationRepeat();
                    }
                    play(0);
                } else {
                    if (mAnimationListener != null) {
                        mAnimationListener.onAnimationEnd();
                    }
                }
            } else {
                play(i + 1);
            }
        }, mDuration);
    }

    private void playByDurations(final int i) {
        mImageView.postDelayed(() -> {
            if (mPause) {
                if (mPause) {
                    mCurrentSelect = SELECTED_C;
                    mCurrentFrame = i;
                    return;
                }
                return;
            }
            if (0 == i) {
                if (mAnimationListener != null) {
                    mAnimationListener.onAnimationStart();
                }
            }
            mImageView.setImageResource(mFrameRes[i]);
            if (i == mLastFrame) {
                if (mIsRepeat) {
                    if (mAnimationListener != null) {
                        mAnimationListener.onAnimationRepeat();
                    }
                    playByDurations(0);
                } else {
                    if (mAnimationListener != null) {
                        mAnimationListener.onAnimationEnd();
                    }
                }
            } else {

                playByDurations(i + 1);
            }
        }, mDurations[i]);

    }

    private void playAndDelay(final int i) {
        mImageView.postDelayed(() -> {
            if (mPause) {
                if (mPause) {
                    mCurrentSelect = SELECTED_B;
                    mCurrentFrame = i;
                    return;
                }
                return;
            }
            mNext = false;
            if (0 == i) {
                if (mAnimationListener != null) {
                    mAnimationListener.onAnimationStart();
                }
            }
            mImageView.setImageResource(mFrameRes[i]);
            if (i == mLastFrame) {
                if (mAnimationListener != null) {
                    mAnimationListener.onAnimationRepeat();
                }
                mNext = true;
                playAndDelay(0);
            } else {
                playAndDelay(i + 1);
            }
        }, mNext && mDelay > 0 ? mDelay : mDuration);

    }

    private void playByDurationsAndDelay(final int i) {
        mImageView.postDelayed(() -> {
            if (mPause) {   // 暂停和播放需求
                mCurrentSelect = SELECTED_A;
                mCurrentFrame = i;
                return;
            }
            if (0 == i) {
                if (mAnimationListener != null) {
                    mAnimationListener.onAnimationStart();
                }
            }
            mImageView.setImageResource(mFrameRes[i]);
            if (i == mLastFrame) {
                if (mAnimationListener != null) {
                    mAnimationListener.onAnimationRepeat();
                }
                mNext = true;
                playByDurationsAndDelay(0);
            } else {
                playByDurationsAndDelay(i + 1);
            }
        }, mNext && mDelay > 0 ? mDelay : mDurations[i]);

    }

    public interface AnimationListener {

        void onAnimationStart();

        void onAnimationEnd();

        void onAnimationRepeat();
    }

}
