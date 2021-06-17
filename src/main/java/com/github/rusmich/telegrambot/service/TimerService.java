package com.github.rusmich.telegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimerService {

    @Autowired
    TelegramBot telegramBot;


    public void timerCorrectWords(String[] words, Long chatId, String userName) {

        if (words.length > 1) {
            String firstWords = words[0];
            String secondSymbol = words[1];
            String trueSecondSymbol = secondSymbol.replaceAll("[^0-9.\\s]", "");

            if (firstWords.equalsIgnoreCase("таймер")) {
                long userTimer = Long.parseLong(trueSecondSymbol);

                if (words.length == 2 || words.length == 3 && words[2].equalsIgnoreCase("сек") ||
                        words[2].equalsIgnoreCase("секунд") || words[2].equalsIgnoreCase("секунды")) {
                    long time = userTimer * 1000L;
                    //Склонение секунд.
                    if (words.length == 2 || words.length == 3) {
                        if ((Integer.parseInt(new String(words[1].toCharArray())) % 10) == 1 && ((((Integer.parseInt(new String(words[1].toCharArray())) / 10)) % 10) != 1)) {
                            telegramBot.sendMessage("Таймер установлен на " + userTimer + " секунду", chatId);
                            try {
                                Thread.sleep(time);   //тестовое время 20 секунд, проблема способа, в том что нету мультипоточности
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            telegramBot.sendMessage("@" + userName + " Таймер на " + userTimer + " секунду окончен", chatId);
                        } else if (Integer.parseInt(new String(words[1].toCharArray())) > 11 &&
                                Integer.parseInt(new String(words[1].toCharArray())) < 15) {
                            telegramBot.sendMessage("Таймер установлен на " + userTimer + " секунд", chatId);
                            try {
                                Thread.sleep(time);   //тестовое время 20 секунд, проблема способа, в том что нету мультипоточности
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            telegramBot.sendMessage("@" + userName + " Таймер на " + userTimer + " секунд окончен", chatId);
                        } else if ((Integer.parseInt(new String(words[1].toCharArray())) % 10) == 2 ||
                                (Integer.parseInt(new String(words[1].toCharArray())) % 10) == 3 ||
                                (Integer.parseInt(new String(words[1].toCharArray())) % 10) == 4) {
                            telegramBot.sendMessage("Таймер установлен на " + userTimer + " секунды", chatId);
                            try {
                                Thread.sleep(time);   //тестовое время 20 секунд, проблема способа, в том что нету мультипоточности
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            telegramBot.sendMessage("@" + userName + " Таймер на " + userTimer + " секунды окончен", chatId);
                        } else {
                            telegramBot.sendMessage("Таймер установлен на " + userTimer + " секунд", chatId);
                            try {
                                Thread.sleep(time);   //тестовое время 20 секунд, проблема способа, в том что нету мультипоточности
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            telegramBot.sendMessage("@" + userName + " Таймер на " + userTimer + " секунд окончен", chatId);
                        }
                    }
                } else if (words.length == 3 && words[2].equals("мин") || words[2].equals("минут") || words[2].equals("минуты")) {
                    long time = userTimer * 600_00L;
                    //склонение минут
                    if ((Integer.parseInt(new String(words[1].toCharArray())) % 10) == 1 && ((((Integer.parseInt(new String(words[1].toCharArray())) / 10)) % 10) != 1)) {
                        telegramBot.sendMessage("Таймер установлен на " + userTimer + " минуту", chatId);
                        try {
                            Thread.sleep(time);   //тестовое время 20 секунд, проблема способа, в том что нету мультипоточности
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        telegramBot.sendMessage("@" + userName + " Таймер на " + userTimer + " минуту окончен", chatId);
                    } else if (Integer.parseInt(new String(words[1].toCharArray())) > 11 &&
                            Integer.parseInt(new String(words[1].toCharArray())) < 15) {
                        telegramBot.sendMessage("Таймер установлен на " + userTimer + " минут", chatId);
                        try {
                            Thread.sleep(time);   //тестовое время 20 секунд, проблема способа, в том что нету мультипоточности
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        telegramBot.sendMessage("@" + userName + " Таймер на " + userTimer + " минут окончен", chatId);
                    } else if ((Integer.parseInt(new String(words[1].toCharArray())) % 10) == 2 ||
                            (Integer.parseInt(new String(words[1].toCharArray())) % 10) == 3 ||
                            (Integer.parseInt(new String(words[1].toCharArray())) % 10) == 4) {
                        telegramBot.sendMessage("Таймер установлен на " + userTimer + " минуты", chatId);
                        try {
                            Thread.sleep(time);   //тестовое время 20 секунд, проблема способа, в том что нету мультипоточности
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        telegramBot.sendMessage("@" + userName + " Таймер на " + userTimer + " минуты окончен", chatId);
                    } else {
                        telegramBot.sendMessage("Таймер установлен на " + userTimer + " минут", chatId);
                        try {
                            Thread.sleep(time);   //тестовое время 20 секунд, проблема способа, в том что нету мультипоточности
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        telegramBot.sendMessage("@" + userName + " Таймер на " + userTimer + " минут окончен", chatId);
                    }
                }
            }
        }
    }
}
