package com.tgf.twf.libgdx;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class TransparentHittableImage extends Image {
    private final TransparentTexture.Mask mask;

    public TransparentHittableImage(final TransparentTexture transparentTexture) {
        super(transparentTexture);
        this.mask = transparentTexture.getMask();
    }

    @Override
    public Actor hit(final float x, final float y, final boolean touchable) {
        final Actor result = super.hit(x, y, touchable);
        if (result != null && mask.isSet((int) x, (int) y)) {
            return result;
        } else {
            return null;
        }
    }
}
