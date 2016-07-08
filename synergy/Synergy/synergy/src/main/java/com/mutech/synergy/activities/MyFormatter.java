package com.mutech.synergy.activities;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by nonstop on 8/7/16.
 */

    public class MyFormatter implements ValueFormatter {
        private static final String[] SUFFIX = new String[]{"", "k", "m", "b", "t"};
        private static final int MAX_LENGTH = 4;
        private DecimalFormat mFormat;
        private String mText;

        public MyFormatter() {
            this.mText = "";
            this.mFormat = new DecimalFormat("###E0");
        }

        public MyFormatter(String appendix) {
            this();
            this.mText = appendix;
        }

        public String getFormattedValue(float value) {
            String i=Math.round(value)+"";
            return i ;
//        return this.makePretty((double)value) + this.mText;
        }

        private String makePretty(double number) {
            String r = this.mFormat.format(number);

            for(r = r.replaceAll("E[0-9]", SUFFIX[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]); r.length() > 4 || r.matches("[0-9]+\\.[a-z]"); r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1)) {
                ;
            }

            return r;
        }
    }


