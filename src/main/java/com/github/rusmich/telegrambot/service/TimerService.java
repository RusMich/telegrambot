package com.github.rusmich.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimerService {


    @Autowired
    TelegramBot telegramBot;

    public void timer(String[] words, Long chatId, String userName) {

        String firstWords = words[0];
        String secondSymbol = words[1];
        String trueSecondSymbol = secondSymbol.replaceAll("[^0-9.\\s]", "");
        var userTimer = Long.parseLong(trueSecondSymbol);
        var i = Integer.parseInt(new String(words[1].toCharArray()));
        var b = ((i % 10) == 2 || (i % 10) == 3 || (i % 10) == 4);
        var d = ((i % 10) == 1 && ((i / 10) % 10) != 1);

        if (firstWords.equalsIgnoreCase("таймер")) {

            if (words.length == 2 || words.length == 3 &&
                    words[2].equals("сек") ||
                    words[2].equals("секунд") ||
                    words[2].equals("секунды") ||
                    words[2].equals("секунда")) {

                long time = userTimer * 1000L;

                if (words.length == 2 || words.length == 3) {

                    if (d) {
                        returnTime(chatId, userName, userTimer, time, " секунду", " секунду окончен");
                    } else if (i > 11 && i < 15) {
                        returnTime(chatId, userName, userTimer, time, " секунд", " секунд окончен");
                    } else if (b) {
                        returnTime(chatId, userName, userTimer, time, " секунды", " секунды окончен");
                    } else {
                        returnTime(chatId, userName, userTimer, time, " секунд", " секунд окончен");
                    }
                }
            } else if (words.length == 3 &&
                    words[2].equals("мин") ||
                    words[2].equals("минут") ||
                    words[2].equals("минуты") ||
                    words[2].equals("минута")) {

                long time = userTimer * 600_00L;

                if (d) {
                    returnTime(chatId, userName, userTimer, time, " минуту", " минуту окончен");
                } else if (i > 11 && i < 15) {
                    returnTime(chatId, userName, userTimer, time, " минут", " минут окончен");
                } else if (b) {
                    returnTime(chatId, userName, userTimer, time, " минуты", " минуты окончен");
                } else {
                    returnTime(chatId, userName, userTimer, time, " минут", " минут окончен");
                }
            }
        }
    }

    private void getMessageStart(Long chatId, long userTimer, String s) {
        telegramBot.sendMessage("Таймер установлен на " + userTimer + s, chatId);
    }

    private void getMessageEnd(Long chatId, String userName, long userTimer, String s) {
        telegramBot.sendMessage("@" + userName + " Таймер на " + userTimer + s, chatId);
    }

    private void returnTime(Long chatId, String userName, long userTimer, long time, String s, String s2) {
        getMessageStart(chatId, userTimer, s);
        sleep(time);
        getMessageEnd(chatId, userName, userTimer, s2);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
           e.printStackTrace();
        }
    }
}
