package com.cezarykluczynski.stapi.etl.template.util;

import java.util.regex.Pattern;

public class PatternDictionary {

	public static final String YEAR_GROUP = "(\\d{4})";
	public static final String MONTH_GROUP = "(January|February|March|April|May|June|July|August|September|October|November|December)";
	public static final String DAY_GROUP = "(\\d{1,2})((st|nd|rd|th)?)";

	public static final Pattern YEAR = Pattern.compile("^" + YEAR_GROUP + "$");

}
