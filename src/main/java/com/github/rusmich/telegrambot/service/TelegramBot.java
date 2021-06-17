package com.github.rusmich.telegrambot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

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
    @Autowired
    TimerService timerService;

    @Override
    //Определить какое поведение нужно совершить когда бот получает сообщение
    public void onUpdateReceived(Update update) {
        new Thread(() -> {
            executeUpdate(update);
        }).start();

        coinRandom(update);
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
                if (message.getText().equalsIgnoreCase("дошик")) {
                    sendMessage("Таймер активирован", chatId);
                    try {
                        Thread.sleep(20000L);   //тестовое время 20 секунд, проблема способа, в том что нету мультипоточности
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendMessage("Дошик заварился " + "@" + userName, chatId);
                }
            } else if (words[0].equals("куда") && words[1].equals("поехать") && words[2].equals("отдохнуть")) {
                sendLocation(chatId);
            }
          
            //метод по таймеру
            timerService.timer(words, chatId, userName);
              
            if (words.length > 3) {
                if (words[0].equals("что") && words[1].equals("приготовить") && words[2].equals("из")) {
                    BeginEda finEda = new BeginEda();
                    EndEda posEda = new EndEda();
                    String[] eda = words;
                    String newEda = "";
                    String FinNewEda = "";

                    for (int i = 3; i < eda.length; i++) {
                        String clearwords = eda[i];
                        if (clearwords.length() > 2) {
                            newEda += clearwords + " ";
                        }
                    }
                    newEda = newEda.replaceAll("[,.]", "");
                    String[] finalEda = newEda.split(" ");
                    for (int i = 0; i < finalEda.length; i++) {
                        if (i % 2 == 0 && i != finalEda.length - 1) {
                            FinNewEda += OOnEndConvert.stem(finalEda[i]) + "-";
                        } else {
                            FinNewEda += AdjectiveConvert.stem(finalEda[i]) + " ";
                        }
                    }


                    String finStrEda = FinNewEda;
                    sendMessage("Из этих ингридиентов ты можешь приготовить замечательный: " +
                            finEda.getAnswer() + " " + finStrEda.trim() + " " + posEda.getAnswer(), chatId);

                }

            }
        }
    }

    public synchronized void sendLocation(Long chatId) {
        CoordinteGen coordinteGen = new CoordinteGen();
        Float latitude = Float.parseFloat(coordinteGen.getLat());
        Float longitude = Float.parseFloat(coordinteGen.getLon());
        SendLocation sendLocation = new SendLocation();
        sendLocation.setLatitude(latitude);
        sendLocation.setLongitude(longitude);
        sendLocation.setChatId(chatId);
        try {
            execute(sendLocation);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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
        if (update != null) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            RandomCoin randomCoin = new RandomCoin();
            if (message.getText().equalsIgnoreCase("монетка") || message.getText().
                    equalsIgnoreCase("подкинуть")) {
                sendMessage(randomCoin.getAnswer(), chatId);

            }
        }

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
