package gov.goias.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created by lucas-mp on 13/01/15.
 */
public class StringSimilarity {

    private static final Logger log = Logger.getLogger(StringSimilarity.class);

    public static double razaoDeSimilaridade(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
        return (longerLength - StringUtils.getLevenshteinDistance(longer, shorter)) / (double) longerLength;
    }

    public static void main(String[] args) {
        System.out.println(razaoDeSimilaridade("deise santos silva", "deize santtos silva")); //0.8947368421052632
    }
}
