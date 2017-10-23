package com.example.katrindrozhak.hotels.extensions;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;

public class SpannableText {

    /**
     * This method sets a bold typeface for a piece of string
     *
     * @param str   is a string which we want set typeface
     * @param start is a parameter for start symbol in string
     * @param end   is a parameter of number of selected symbols
     * @return resulting text
     */
    public static Spannable setupBoldText(String str, int start, int end) {
        Spannable text = new SpannableString(str);
        text.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return text;
    }
}
