package com.parttime.job.Application.common.utils;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.DateFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Slf4j
public class DateTimeUtil {
    private DateTimeUtil() {

    }

    /**
     * Convert String To Date
     *
     * @param format
     * @param strDate
     * @return
     */
    public static Date convertStringToDate(String format, String strDate) {
        if (StringUtils.hasText(strDate)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                return simpleDateFormat.parse(strDate);
            } catch (ParseException e) {
                log.error("Exception when parse String to Date. Input {} to format {} ", strDate, format);
                throw new DateFormatException(MessageCodeConstant.M025_FORMAT_FAIL,
                        String.format("Format from string input = %s to date output = %s fail.", strDate, format), e);
            }
        } else {
            return null;
        }
    }

    /**
     * Convert Date To String
     *
     * @param date
     * @param format
     * @param timeZone
     * @return
     */
    public static String convertDateToString(Date date, String format, TimeZone timeZone) {
        if (date != null) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                simpleDateFormat.setTimeZone(timeZone);
                return simpleDateFormat.format(date);
            } catch (Exception e) {
                log.error("Exception when parse Date to String. Input {} to format {} ", date, format);
                throw new DateFormatException(MessageCodeConstant.M025_FORMAT_FAIL,
                        String.format("Format from Date input = %s to String output = %s fail.", date, format), e);
            }
        } else {
            return "";
        }
    }
}
