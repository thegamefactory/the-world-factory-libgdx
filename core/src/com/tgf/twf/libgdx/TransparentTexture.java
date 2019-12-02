package com.tgf.twf.libgdx;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.BitSet;

public class TransparentTexture extends Texture {
    @Getter
    private final Mask mask;

    public TransparentTexture(final String path) {
        super(path);
        this.mask = fromTexture(this);
    }

    public interface Mask {
        boolean isSet(final int x, final int y);
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
                if (pixmap.getPixel(x, y) != 0) {
                    mask.set(x, y);
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
        public boolean isSet(final int x, final int y) {
            return bitSet.get(x * height + y);
        }

        void set(final int x, final int y) {
            bitSet.set(x * height + y);
        }
    }
}
