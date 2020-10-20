package Custom;

import android.animation.Animator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;

import Presenters.MainPresenter;

public class AnimationLib {

    private static AnimationLib instance ;

    public static AnimationLib builder() {

        if(instance == null)
            instance = new AnimationLib();

        return instance;

    }


    public enum State {

        HIDE ,
        SHOW
    }


    public void circular(View targetView , long duration
                          , State state) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            boolean isShow = state == State.SHOW;

            Animator animator = ViewAnimationUtils.createCircularReveal(targetView
                    ,   targetView.getWidth() / 2 ,
                    targetView.getHeight()/2
                    , isShow ? 0 :  targetView.getWidth()/2  ,
                    isShow ? targetView.getWidth()/2 : 0 );
            animator.setDuration(duration);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if(isShow)
                       targetView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if(!isShow)
                        targetView.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            animator.start();

        }else {

            switch (state) {

                case HIDE:
                    targetView.setVisibility(View.GONE);
                    break;


                case SHOW:
                    targetView.setVisibility(View.VISIBLE);
                    break;
            }

        }
    }



    public void translateAnim(View view , long duration , State state) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                boolean isShow = state == State.SHOW;
                TranslateAnimation transAnim = new TranslateAnimation(1, 1 ,
                        isShow ?  - view.getHeight() : 0 , isShow ? 0 : - view.getHeight());
                transAnim.setDuration(duration);
                transAnim.setInterpolator(new BounceInterpolator());
                view.startAnimation(transAnim);
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }



}
