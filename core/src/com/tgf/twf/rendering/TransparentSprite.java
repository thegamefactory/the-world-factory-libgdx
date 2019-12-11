package com.tgf.twf.rendering;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.BitSet;

/**
 * {@link Texture} maintaining a {@link Mask} which can be queried to define if the image is opaque or not at a certain pixel.
 */
public class TransparentSprite {
    @Getter
    private final Sprite sprite;
    @Getter
    private final Mask mask;

    public TransparentSprite(final Sprite sprite) {
        if (null == sprite) {
            throw new IllegalStateException(
                    "Attempting to create null transparent sprite. Did you forget to repack the textures?"
                            + " Run './gradlew texturePacker' to pack the textures."
            );
        }

        this.sprite = sprite;
        this.mask = fromTexture(sprite.getTexture());
    }

    public interface Mask {
        boolean isOpaque(final int x, final int y);
    }

    public static Mask fromTexture(final Texture texture) {
        final TextureData textureData = texture.getTextureData();
        textureData.prepare();
        final Pixmap pixmap = textureData.consumePixmap();
        final int width = pixmap.getWidth();
        final int height = pixmap.getHeight();
        final MaskImpl mask = new MaskImpl(width, height);
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (pixmap.getPixel(x, y) >> 24 != 0x00) {
                    mask.setOpaque(x, y);
                }
            }
        }
        return mask;
    }

    @RequiredArgsConstructor
    private static class MaskImpl implements Mask {
        private final BitSet bitSet;
        private final int height;

        MaskImpl(final int width, final int height) {
            this.bitSet = new BitSet(width * height);
            this.height = height;
        }

        @Override
        public boolean isOpaque(final int x, final int y) {
            return bitSet.get(x * height + y);
        }

        void setOpaque(final int x, final int y) {
            bitSet.set(x * height + y);
        }
    }

    public static class Component extends com.tgf.twf.core.ecs.Component {
        @Getter
        @Setter
        private TransparentSprite transparentSprite;

        public Component(final TransparentSprite transparentSprite) {
            this.transparentSprite = transparentSprite;
        }

        public Sprite getSprite() {
            return transparentSprite.getSprite();
        }
    }
}
