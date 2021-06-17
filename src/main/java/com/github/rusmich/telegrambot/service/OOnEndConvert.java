package com.github.rusmich.telegrambot.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OOnEndConvert {
    private static final Pattern PERFECTIVEGROUND = Pattern.compile("((ив|ивши|ившись|ыв|ывши|ывшись)|((?<=[ая])(в|вши|вшись)))$");
    private static final Pattern REFLEXIVE = Pattern.compile("(с[яь])$");
    private static final Pattern ADJECTIVE = Pattern.compile("(ее|ие|ые|ое|ими|ыми|ей|ий|ый|ой|ем|им|ым|ом|его|ого|ему|ому|их|ых|ую|юю|ая|яя|ою|ею)$");
    private static final Pattern PARTICIPLE = Pattern.compile("((ивш|ывш|ующ)|((?<=[ая])(ем|нн|вш|ющ|щ)))$");
    private static final Pattern VERB = Pattern.compile("((ила|ыла|ена|ейте|уйте|ите|или|ыли|ей|уй|ил|ыл|им|ым|ен|ило|ыло|ено|ят|ует|уют|ит|ыт|ены|ить|ыть|ишь|ую|ю)|((?<=[ая])(ла|на|ете|йте|ли|й|л|ем|н|ло|но|ет|ют|ны|ть|ешь|нно)))$");
    private static final Pattern NOUN = Pattern.compile("(а|ев|ов|ие|ье|е|иями|ями|ами|еи|ии|и|ией|ей|ой|ий|й|иям|ям|ием|ем|ам|ом|о|у|ах|иях|ях|ы|ь|ию|ью|ю|ия|ья|я)$");
    private static final Pattern RVRE = Pattern.compile("^(.*?[аеиоуыэюя])(.*)$");
    private static final Pattern DERIVATIONAL = Pattern.compile(".*[^аеиоуыэюя]+[аеиоуыэюя].*ость?$");
    private static final Pattern DER = Pattern.compile("ость?$");
    private static final Pattern SUPERLATIVE = Pattern.compile("(ейше|ейш)$");

    private static final Pattern I = Pattern.compile("и$");
    private static final Pattern P = Pattern.compile("ь$");
    private static final Pattern NN = Pattern.compile("нн$");

    public static String stem(String word) {
        word = word.toLowerCase();
        word = word.replace('ё', 'е');
        Matcher m = RVRE.matcher(word);
        if (m.matches()) {
            String pre = m.group(1);
            String rv = m.group(2);
            String temp = PERFECTIVEGROUND.matcher(rv).replaceFirst("");
            if (temp.equals(rv)) {
                rv = REFLEXIVE.matcher(rv).replaceFirst("");
                temp = ADJECTIVE.matcher(rv).replaceFirst("");
                if (!temp.equals(rv)) {
                    rv = temp;
                    rv = PARTICIPLE.matcher(rv).replaceFirst("");
                } else {
                    temp = VERB.matcher(rv).replaceFirst("");
                    if (temp.equals(rv)) {
                        rv = NOUN.matcher(rv).replaceFirst("");
                    } else {
                        rv = temp;
                    }
                }

            } else {
                rv = temp;
            }

            rv = I.matcher(rv).replaceFirst("");

            if (DERIVATIONAL.matcher(rv).matches()) {
                rv = DER.matcher(rv).replaceFirst("");
            }

            temp = P.matcher(rv).replaceFirst("");
            if (temp.equals(rv)) {
                rv = SUPERLATIVE.matcher(rv).replaceFirst("");
                rv = NN.matcher(rv).replaceFirst("н");
            } else {
                rv = temp;
            }

            word = pre + rv;
//нахождение прилагательного
            String str = word;
            char[] result = str.toCharArray();
            char result2 = result[result.length - 2];
            char result3 = result[result.length - 1];
            String str2 = String.valueOf(result2);
            String str3 = String.valueOf(result3);
            String str4 = str2 + str3;

            if (str4.equals("од") || str4.equals("ир") || str4.equals("ыр") || str4.equals("он")
                    || str4.equals("ар") || str4.equals("ов") || str4.equals("кт") || str4.equals("ос") || str4.equals("яж") || str4.equals("ав")
                    || str4.equals("нт") || str4.equals("ад") || str4.equals("ус") || str4.equals("уз") || str4.equals("уш") && !word.equals("груш")
                    || str4.equals("жа") || str4.equals("на") || str4.equals("иа") || str4.equals("ын") || word.equals("вин") || word.equals("витамин")
                    || str4.equals("еб") || str4.equals("ят") || str4.equals("ла") || str4.equals("рт") || str4.equals("ма") || str4.equals("ба")
                    || str4.equals("рс") || str4.equals("ом") || str4.equals("ор")) {
                word = word + "но";
            } else if (str4.equals("бк")) {
                word = removeLastCharOne(word);
                word = word + "но";
            } else if (word.equals("говядин")) {
                word = "говяже";
            } else if (word.equals("почк")) {
                word = "почечно";
            } else if (str4.equals("ин") || str4.equals("чк") || str4.equals("ьн") || str4.equals("ул") || str4.equals("ур")
                    || str4.equals("ех") || str4.equals("ат") || str4.equals("юм") || str4.equals("тн") || str4.equals("бл")
                    || str4.equals("ас") || str4.equals("ск") || str4.equals("гл") || str4.equals("от") || str4.equals("ер")
                    || str4.equals("цк") || str4.equals("их") || str4.equals("ев") || str4.equals("йк") || str4.equals("тв")
                    || str4.equals("рг") || str4.equals("ес") || str4.equals("вн") || str4.equals("чн") || str4.equals("яв")
                    || str4.equals("вр") || str4.equals("ив") || str4.equals("сн") || str4.equals("кл") || str4.equals("ыб")
                    || str4.equals("рн") || str4.equals("тк") || str4.equals("эт")) {
                word = word + "о";
            } else if (str4.equals("нк") && !word.equals("манк") || str4.equals("вк") || str4.equals("шк")) {
                word = removeLastCharOne(word);
                word = word + "о";
            } else if (str4.equals("ыч") || str4.equals("рщ") || str4.equals("рц")) {
                word = word + "е";
            } else if (str4.equals("ык")) {
                word = word + "чно";
            } else if (str4.equals("ок") || str4.equals("ук") && !word.equals("лук") || str4.equals("ец") || str4.equals("ек")) {
                word = removeLastCharOne(word);
                word = word + "чно";
            } else if (str4.equals("ен")) {
                word = word + "очно";
            } else if (str4.equals("дк")) {
                word = removeLastCharOne(word);
                word = word + "очно";
            } else if (str4.equals("нд") || str4.equals("кв")) {
                word = word + "ично";
            } else if (str4.equals("йц") || str4.equals("ик") || word.equals("горчиц")) {
                word = removeLastCharTwo(word);
                word = word + "ично";
            } else if (str4.equals("ог") || str4.equals("aг")) {
                word = removeLastCharOne(word);
                word = word + "жно";
            } else if (str4.equals("та")) {
                word = removeLastCharOne(word);
                word = word + "анно";
            } else if (str4.equals("ил")) {
                word = word + "ьно";
            } else if (str4.equals("ра") || word.equals("капуст")) {
                word = word + "но";
            } else if (str4.equals("нк")) {
                word = removeLastCharOne(word);
                word = word + "но";
            } else if (str4.equals("жж") || str4.equals("ел") || str4.equals("рр") || str4.equals("шн") || word.equals("груш")) {
                word = word + "ево";
            } else if (str4.equals("ол")) {
                word = word + "ёно";
            } else if (str4.equals("рб")) {
                word = word + "ено";
            } else if (str4.equals("сл")) {
                word = word + "яно";
            } else if (word.equals("лук")) {
                word = "луково";
            } else if (str4.equals("яс")) {
                word = word + "ной";
            } else if (str4.equals("иц") || str4.equals("ич") || str4.equals("зд") || str4.equals("рш")
                    || str4.equals("ыш")) {
                word = word + "е";
            } else if (str4.equals("ст") || str4.equals("ис") || str4.equals("йв") || str4.equals("пш")
                    || str4.equals("об") || str4.equals("ох") || str4.equals("ут")
                    || str4.equals("ка")) {
                word = word + "ово";
            } else if (str4.equals("лт")) {
                word = word + "еево";
            } else if (str4.equals("ан")) {
                word = word + "иево";
            } else if (str4.equals("га")) {
                word = word + "ло";
            } else if (str4.equals("ум")) {
                word = word + "ино";
            } else if (str4.equals("ча") || str4.equals("ва")) {
                word = word + "йно";
            } else if (str4.equals("оф")) {
                word = word + "ейно";
            }

        }
        return word;


    }

    public static String removeLastCharTwo(String str) {
        return removeLastChars(str, 2);
    }

    public static String removeLastCharOne(String str) {
        return removeLastChars(str, 1);
    }

    public static String removeLastChars(String str, int chars) {
        return str.substring(0, str.length() - chars);
    }


}