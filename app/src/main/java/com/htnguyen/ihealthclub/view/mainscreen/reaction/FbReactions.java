package com.htnguyen.ihealthclub.view.mainscreen.reaction;

import com.htnguyen.ihealthclub.R;
import com.htnguyen.ihealthclub.view.reacbutton.Reaction;

public final class FbReactions {

    public static Reaction defaultReact = new Reaction(
            ReactConstants.LIKE,
            ReactConstants.DEFAULT,
            ReactConstants.GRAY,
            R.drawable.ic_like);

    public static Reaction[] reactions = {
            new Reaction(ReactConstants.LIKE, ReactConstants.BLUE, R.drawable.ic_like),
            new Reaction(ReactConstants.LOVE, ReactConstants.RED_LOVE, R.drawable.ic_heart),
            new Reaction(ReactConstants.SMILE, ReactConstants.YELLOW_WOW, R.drawable.ic_happy),
            new Reaction(ReactConstants.WOW, ReactConstants.YELLOW_WOW, R.drawable.ic_surprise),
            new Reaction(ReactConstants.SAD, ReactConstants.YELLOW_HAHA, R.drawable.ic_sad),
            new Reaction(ReactConstants.ANGRY, ReactConstants.RED_ANGRY, R.drawable.ic_angry),
    };

    public static Reaction[] reactionSecond = {
            new Reaction(ReactConstants.LIKE, ReactConstants.BLUE, R.drawable.ic_like),
            new Reaction(ReactConstants.LOVE, ReactConstants.RED_LOVE, R.drawable.ic_heart),
            new Reaction(ReactConstants.SMILE, ReactConstants.YELLOW_WOW, R.drawable.ic_happy),
            new Reaction(ReactConstants.WOW, ReactConstants.YELLOW_WOW, R.drawable.ic_surprise),
            new Reaction(ReactConstants.SAD, ReactConstants.YELLOW_HAHA, R.drawable.ic_sad),
            new Reaction(ReactConstants.ANGRY, ReactConstants.RED_ANGRY, R.drawable.ic_angry),
    };

    public String getTextAction() {
        return textAction;
    }

    public void setTextAction(String textAction) {
        this.textAction = textAction;
    }

    private String textAction;


}
