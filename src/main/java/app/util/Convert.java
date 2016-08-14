package app.util;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

public final class Convert {

    public static View toView(HttpServletRequest request) {
	View view = new MappingJackson2JsonView();
	return view;
    }

    public static Date toDate(Long timestamp) {
	if (timestamp == null || timestamp == 0) {
	    return null;
	}
	Calendar c = Calendar.getInstance();
	c.setTimeInMillis(timestamp * 1000);
	return c.getTime();
    }

    public static Long toSeconds(Object date) {
	if (date instanceof java.util.Date) {
	    return toSeconds((Date) date);
	}
	return null;
    }

    public static Long toSeconds(Date date) {
	return date.getTime() / 1000;
    }

    public static int toInt(Object value, int defaultValue) {
	if (value instanceof java.lang.Integer) {
	    return (Integer) value;
	}
	return defaultValue;
    }
    
}
