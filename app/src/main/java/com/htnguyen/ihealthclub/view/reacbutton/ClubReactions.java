package com.htnguyen.ihealthclub.view.reacbutton;

import com.htnguyen.ihealthclub.R;
import com.htnguyen.ihealthclub.model.TypeAction;

public final class ClubReactions {

    public static Reaction defaultReact = new Reaction(
            TypeAction.NO,
            ReactConstants.LIKE,
            ReactConstants.DEFAULT,
            ReactConstants.GRAY,
            R.drawable.ic_unlike);

    public static Reaction[] reactions = {
            new Reaction(TypeAction.LIKE, ReactConstants.LIKE, ReactConstants.LIKE, ReactConstants.BLUE, R.drawable.ic_like),
            new Reaction(TypeAction.LOVE, ReactConstants.LOVE, ReactConstants.LOVE,ReactConstants.RED_LOVE, R.drawable.ic_heart),
            new Reaction(TypeAction.SMILE, ReactConstants.SMILE, ReactConstants.SMILE,ReactConstants.YELLOW_WOW, R.drawable.ic_happy),
            new Reaction(TypeAction.WOW, ReactConstants.WOW, ReactConstants.WOW,ReactConstants.YELLOW_WOW, R.drawable.ic_surprise),
            new Reaction(TypeAction.SAD, ReactConstants.SAD, ReactConstants.SAD,ReactConstants.YELLOW_HAHA, R.drawable.ic_sad),
            new Reaction(TypeAction.ANGRY, ReactConstants.ANGRY, ReactConstants.ANGRY,ReactConstants.RED_ANGRY, R.drawable.ic_angry),
    };

    public String getTextAction() {
        return textAction;
    }

    public void setTextAction(String textAction) {
        this.textAction = textAction;
    }

    private String textAction;

    public Reaction getReaction(TypeAction typeAction) {
        for (int index = 0; index < reactions.length; index++) {
            if (reactions[index].getReactTypeAction() == typeAction) return reactions[index];
        }
        return defaultReact;
    }

}
