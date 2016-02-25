package com.hilo.animotions.FadeExit;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;
import com.hilo.animotions.BaseAnimatorSet;

public class FadeExit extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0).setDuration(duration));
	}
}
