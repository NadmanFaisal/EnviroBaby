package org.example.envirobaby;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataProcessor {

    public double extractDouble(String message) { //  locate double within a String
        Pattern doublePattern = Pattern.compile("([0-9]{1,13}(\\.[0-9]*)?)"); //setup regex pattern
        Matcher doubleMatcher = doublePattern.matcher(message); // search for pattern in string
        double extracted = 0;

        if(doubleMatcher.find()) {
            extracted = Double.parseDouble(doubleMatcher.group()); //if found, parse into double
        }
        return extracted;
    }

    public int extractNumber(String message) {
        Pattern numberPattern = Pattern.compile("\\d+");
        Matcher numberMatcher = numberPattern.matcher(message);
        int extracted = 0;

        if (numberMatcher.find()) {
            extracted = Integer.parseInt(numberMatcher.group());
        }
        return extracted;
    }
}
