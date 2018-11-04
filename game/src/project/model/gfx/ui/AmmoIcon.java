/*
 * Copyright (c) 2011 Morgan Stanley & Co. Incorporated, All Rights Reserved
 *
 * Unpublished copyright.  All rights reserved.  This material contains
 * proprietary information that shall be used or copied only within 
 * Morgan Stanley, except with written permission of Morgan Stanley.
 */
package project.model.gfx.ui;

import java.awt.Image;
import project.model.gfx.core.Sprite;
import project.model.util.Global;

/**
 * TODO: type comment.
 * 
 * @version $Revision:$, submitted by $Author:$
 * @author chavenic: Aug 26, 2011, 8:32:37 AM
 */
public class AmmoIcon extends Sprite {

    public enum State {
        FULL, EMPTY
    }

    public static Image full;

    public static Image empty;

    public State state;

    public AmmoIcon() {
        if (full == null && empty == null) {
            full = Global.depot.ui.get("ammoicon");
            empty = Global.depot.ui.get("emptyammoicon");
        }
        fill();
        hud = true;
    }

    public void fill() {
        animation = null;
        singleImg = full;
        state = State.FULL;
    }

    public void empty() {
        animation = null;
        singleImg = empty;
        state = State.EMPTY;
    }
}
