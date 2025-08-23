package com.parttime.job.Application.common.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileSizeUtil {
    public static final long ONE_MB = 1024L;
    public static final int NUMBER_OF_SUBSCRIPTIONS = 2;

    public static long parseSize(String size){
        long multiplier;
        int numSub = NUMBER_OF_SUBSCRIPTIONS;
        size = size.toUpperCase();
        if(size.endsWith("KB")){
            multiplier = ONE_MB;
        } else if (size.endsWith("MB")) {
            multiplier = ONE_MB*ONE_MB;
        } else if (size.endsWith("GB")) {
            multiplier = ONE_MB*ONE_MB*ONE_MB;
        } else {
            log.error("File size is incorrect.");
            return 0;
        }
        size = size.substring(0, size.length() - numSub);
        return Long.parseLong(size.trim())* multiplier;
    }
}
