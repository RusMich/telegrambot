package com.github.rusmich.telegrambot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Dictionary;
import java.util.Hashtable;

@Component
@PropertySource("application.properties")
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.username}")
    private String botUsername;
    @Value("${bot.token}")
    private String botToken;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MessageService messageService;

    @Override
    //Определить какое поведение нужно совершить когда бот получает сообщение
    public void onUpdateReceived(Update update) {
        executeUpdate(update);
        //saveJson(update);
    }

    public void executeUpdate(Update update) {
        if (update != null) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            String userName = message.getFrom().getUserName();
            //делаем поступающее сообщение стрингой, и убираем в начале и конце пробелы и переводим в нижний регистр и убираем двойные пробелы
            String stroka = String.valueOf(message.getText().trim().toLowerCase().replaceAll("[\\s]{2,}", " "));
            String[] words = stroka.split(" ");
            if (words.length == 1) {
                if (message.getText().equals("дошик")) {
                    sendMessage("Таймер активирован", chatId);
                    try {
                        Thread.sleep(20000L);   //тестовое время 20 секунд, проблема способа, в том что нету мультипоточности
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendMessage("Дошик заварился " + "@" + userName, chatId);
                } else if (message.getText().equals("монетка") || message.getText().equals("подкинуть")) {
                    coinRandom(update);
                }
            }
            if (words.length > 1) {
                String firstWords = words[0];
                String secondSymbol = words[1];
                String trueSecondSymbol = secondSymbol.replaceAll("[^0-9.\\s]", "");
                if (firstWords.equals("таймер")) {
                    long userTimer = Long.parseLong(trueSecondSymbol);
                    if (words.length == 2 || words.length == 3 && words[2].equals("сек") || words[2].equals("секунд") || words[2].equals("секунды")) {
                        long time = userTimer * 1000L;
                        sendMessage("Таймер установлен на " + userTimer + " секунд", chatId);
                        try {
                            Thread.sleep(time);   //тестовое время 20 секунд, проблема способа, в том что нету мультипоточности
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sendMessage("@" + userName + " Таймер на " + userTimer + " секунд окончен", chatId);
                    } else if (words.length == 3 && words[2].equals("мин") || words[2].equals("минут") || words[2].equals("минуты")) {
                        long time = userTimer * 600_00L;
                        sendMessage("Таймер установлен на " + userTimer + " минут", chatId);
                        try {
                            Thread.sleep(time);   //тестовое время 20 секунд, проблема способа, в том что нету мультипоточности
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sendMessage("@" + userName + " Таймер на " + userTimer + " минут окончен", chatId);
                    }
                }
            }
            if (words.length > 3) {
                if (words[0].equals("что") && words[1].equals("приготовить") && words[2].equals("из")) {
                    Dictionary firstSlovo = new Hashtable();//словарь первых слов
                    Dictionary latterSlovo = new Hashtable();//словарь последующих слов
                    //словари не плохо было бы вынести куда либо
                    BeginEda finEda = new BeginEda();
                    EndEda posEda = new EndEda();
                    String[] eda = words;
                    firstSlovo.put("моркови", "морковно");
                    firstSlovo.put("воды", "водно");
                    latterSlovo.put("моркови", "морковный");
                    latterSlovo.put("воды", "водный");
                    String neweda = "";
                    //начинаем анализ с 4 слово
                    for (int i = 3; i < eda.length; i++) {
                        neweda += latterSlovo.get(eda[i]) + " ";
                    }
                    String fineda = (firstSlovo.get(eda[3]) + "-" + neweda).replace("null", "");
                    sendMessage("Из этих ингридиентов ты можешь приготовить замечательный: " +
                            finEda.getAnswer() + " " + fineda.trim() + " " + posEda.getAnswer(), chatId);

                }

            }
        }
    }


    public synchronized void sendMessage(String text, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

/*    private void saveJson(Update update) {
        try {
            objectMapper.writeValue(new File("src/test/resources/update.json"), update);//непонятно почему красным подчеркивает
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public void coinRandom(Update update) { //метод по генерации выпадения монетки
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        RandomCoin randomCoin = new RandomCoin();
        sendMessage(randomCoin.getAnswer(), chatId);

    }


    @Override
    //Имя бота
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    //Токен бота
    public String getBotToken() {
        return botToken;
    }
}
