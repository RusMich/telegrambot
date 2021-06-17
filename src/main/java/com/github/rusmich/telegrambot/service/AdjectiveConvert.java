package com.github.rusmich.telegrambot.service;
//класс который отвечает за преобразование в прилагательное

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AdjectiveConvert {
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
//нахождение корня слова
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

            if (str4.equals("йц") || str4.equals("ик") || str4.equals("иц") && !word.equals("куриц")) {
                word = removeLastCharTwo(word);
                word = word + "ичный";
            } else if (str4.equals("ов") && !word.equals("лавров") && !word.equals("овсянк") || str4.equals("еб") || str4.equals("ор")
                    && !word.equals("оливков") || str4.equals("он") && !word.equals("дайкон") || str4.equals("яж") || str4.equals("ыб")
                    || str4.equals("оп") || word.equals("капуст") || word.equals("виноград") || str4.equals("вн")
                    || str4.equals("рс") || word.equals("витамин") || str4.equals("ят") || str4.equals("ом")) {
                word = word + "ный";
            } else if (word.equals("картошк") || word.equals("картофел") || word.equals("картох") || word.equals("потат") || word.equals("бульб")) {
                word = "картофельный";
            } else if (word.equals("ванил")) {
                word = "ванильный";
            } else if (word.equals("говядин") || word.equals("говяж")) {
                word = "говяжий";
            } else if (word.equals("овсянк")) {
                word = removeLastCharOne(word);
                word = word + "ый";
            } else if (word.equals("лавров") || str4.equals("яв") || str4.equals("сн")) {
                word = word + "ый";
            } else if (str4.equals("нк") || str4.equals("дк") || str4.equals("ск") && !word.equals("боварск")) {
                word = removeLastCharOne(word);
                word = word + "очный";
            } else if (str4.equals("ок") || str4.equals("вш")) {
                word = removeLastCharTwo(word);
                word = word + "очный";
            } else if (str4.equals("ес")) {
                word = word + "очный";
            } else if (word.equals("куриц") || str4.equals("чк")) {
                word = removeLastCharOne(word);
                word = word + "ный";
            } else if (str4.equals("ир") || str4.equals("ыр") || str4.equals("ар")
                    || str4.equals("уз") || str4.equals("ер") || str4.equals("од")
                    || str4.equals("ын")) {
                word = word + "ный";
            } else if (str4.equals("ур")) {
                word = word + "иный";
            } else if (str4.equals("ог")) {
                word = removeLastCharTwo(word);
                word = word + "ожный";
            } else if (str4.equals("та") || word.equals("вин")) {
                word = word + "нный";
            } else if (str4.equals("ук")) {
                word = removeLastCharOne(word);
                word = word + "чной";
            } else if (str4.equals("жж") || str4.equals("ил") || str4.equals("ел") || str4.equals("бл") || str4.equals("чн")
                    || str4.equals("рр") || str4.equals("ол") && !word.equals("гранол") || str4.equals("лт") || str4.equals("рц")
                    || str4.equals("уш") || str4.equals("зд") || str4.equals("рш") || word.equals("женьшен") || str4.equals("рщ")) {
                word = word + "евый";
            } else if (str4.equals("шн")) {
                word = word + "ёвый";
            } else if (word.equals("сол")) {
                word = word + "ёный";
            } else if (str4.equals("яс") && !word.equals("девяс") || str4.equals("иб")) {
                word = word + "ной";
            } else if (str4.equals("ец") && !word.equals("спец")) {
                word = removeLastCharTwo(word);
                word = word + "чёный";
            } else if (str4.equals("юм") || str4.equals("аг") || str4.equals("ин")
                    && !word.equals("свинин") || word.equals("гранол") || word.equals("дайкон")
                    || str4.equals("ул") || str4.equals("йв") || str4.equals("ыч") || str4.equals("ут") || str4.equals("тк")
                    || str4.equals("ас") || str4.equals("ус") || str4.equals("рб") || str4.equals("их") || str4.equals("эт")
                    || str4.equals("ст") || str4.equals("уп") || str4.equals("шк") || str4.equals("ан") //питаниевый
                    || str4.equals("ос") || str4.equals("нт") || str4.equals("ох") || str4.equals("ыш")
                    || str4.equals("ад") || str4.equals("гл") || str4.equals("от") || str4.equals("рт") || str4.equals("бк")
                    || str4.equals("пш") || str4.equals("об") && !word.equals("звероб") || word.equals("гранат")
                    || str4.equals("ис") || str4.equals("ех") || str4.equals("кт") || str4.equals("ка") || str4.equals("ив")
                    || str4.equals("вр") || str4.equals("йк") || str4.equals("сл") && !word.equals("черносл")) {
                word = word + "овый";
            } else if (str4.equals("га")) {
                word = removeLastCharOne(word);
                word = word + "овый";
            } else if (str4.equals("рн") || str4.equals("ьн") || word.equals("оливков") || str4.equals("ат")) {
                word = word + "о";
            } else if (word.equals("черносл")) {
                word = word + "ивовый";
            } else if (word.equals("свинин")) {
                word = removeLastCharTwo(word);
                word = word + "ой";
            } else if (word.equals("спец")) {
                word = "со специями";
            } else if (str4.equals("ча") || str4.equals("ва")) {
                word = word + "йный";
            } else if (word.equals("звероб")) {
                word = word + "ойный";
            } else if (str4.equals("оф")) {
                word = word + "ейный";
            } else if (str4.equals("ра") || str4.equals("жа") || str4.equals("на") || str4.equals("ба")
                    || str4.equals("иа") || str4.equals("ла") || str4.equals("ма")) {
                word = word + "новый";
            } else if (str4.equals("ав")) {
                word = word + "ановый";
            } else if (str4.equals("ум") || word.equals("девяс") || word.equals("жирунд") || word.equals("жырунд")) {
                word = word + "иновый";
            } else if (str4.equals("нд") || str4.equals("ен")) {
                word = word + "и";
            } else if (str4.equals("кв") || str4.equals("тв")) {
                word = word + "енный";
            } else if (str4.equals("ек")) {
                word = removeLastCharTwo(word);
                word = word + "ьковый";
            } else if (str4.equals("ык")) {
                word = removeLastCharOne(word);
                word = word + "чный";
            } else if (str4.equals("гу")) {
                word = word + "синый";
            } else if (str4.equals("кл")) {
                word = word + "яной";
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