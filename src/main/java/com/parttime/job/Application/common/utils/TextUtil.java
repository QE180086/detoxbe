package com.parttime.job.Application.common.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class TextUtil {
    public static String normalize(String input) {
        if (input == null) return "";
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").toLowerCase().trim();
    }
}
