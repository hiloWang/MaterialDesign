package com.hilo.animotions.Attention;

import android.view.View;

import com.hilo.animotions.BaseAnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public class Flash extends BaseAnimatorSet {
	public Flash() {
		duration = 1000;
	}

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0, 1, 0, 1));
	}
}
