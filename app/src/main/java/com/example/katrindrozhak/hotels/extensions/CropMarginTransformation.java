package com.example.katrindrozhak.hotels.extensions;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

public class CropMarginTransformation implements Transformation {
    /**
     * This method removes 1px around image
     *
     * @param source is our view component which we want to transform.
     * @return transformed view component
     */
    @Override
    public Bitmap transform(Bitmap source) {
        Bitmap result = Bitmap.createBitmap(source, 1, 1, source.getWidth() - 2, source.getHeight() - 2);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return "square()";
    }
}
