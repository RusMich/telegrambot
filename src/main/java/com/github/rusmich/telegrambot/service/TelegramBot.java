package com.github.rusmich.telegrambot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rusmich.telegrambot.buttons.ButtonHelpTextInfo;
import com.github.rusmich.telegrambot.buttons.InlineButtons;
import com.github.rusmich.telegrambot.buttons.ReplyButtons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendLocation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
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


    ReplyButtons replyButtons = new ReplyButtons();
    InlineButtons inlineButtons = new InlineButtons();

    @Override
    //Определить какое поведение нужно совершить когда бот получает сообщение
    public void onUpdateReceived(Update update) {
        new Thread(() -> {
            executeUpdate(update);
        }).start();
        executeButtonsUpdate(update);

        coinRandom(update);
    }

    public void executeUpdate(Update update) {
        if(update.hasCallbackQuery()){
            if(update.getCallbackQuery().getData().equals("callBack_1")){
                Long chatIdFromCallBack = update.getCallbackQuery().getFrom().getId().longValue();
               sendMessage(ButtonHelpTextInfo.text,chatIdFromCallBack);
            }
        }
        if (update != null) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();

            String userName = message.getFrom().getUserName();
            //делаем поступающее сообщение стрингой, и убираем в начале и конце пробелы и переводим в нижний регистр и убираем двойные пробелы
            String stroka = message.getText().trim().toLowerCase().replace("-", " ").replaceAll("[\\s]{2,}", " ");
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
                    newEda = newEda.replaceAll("[,.?!]", "");
                    String[] finalEda = newEda.split(" ");
                    for (int i = 0; i < finalEda.length; i++) {
                        if (i % 2 == 0 && i != finalEda.length - 1) {
                            FinNewEda += OOnEndConvert.stem(finalEda[i]) + "-";
                        } else {
                            FinNewEda += AdjectiveConvert.stem(finalEda[i]) + " ";
                        }
                    }


                    String finStrEda = FinNewEda;
                    sendMessage("Из этих ингредиентов ты можешь приготовить замечательный: " +
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
    public void executeButtonsUpdate(Update update) {
        if (update != null) {
            Message message = update.getMessage();
            Long chatId = message.getChatId();
            if (message.getText().equals("/start")) {  //вызов кнопок при первом запуске бота или команде /start
                sendButtonsWithMessage(replyButtons.keyboardMarkup(), chatId, "Приветствуем вас");
                sendInlineButtonsWithMessage(inlineButtons.keyboardMarkup(), chatId, "Hello");
            }
        }

    }
    public synchronized void sendButtonsWithMessage(ReplyKeyboardMarkup replyKeyboardMarkup,Long chatId, String text){ //метод вызова кнопок
SendMessage sendMessage = new SendMessage();
sendMessage.setChatId(chatId);
sendMessage.setText(text);
sendMessage.setReplyMarkup(replyKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Exception:" + e.toString());
        }

    }
    public synchronized void sendInlineButtonsWithMessage(InlineKeyboardMarkup inlineKeyboardMarkup, Long chatId, String text){ //метод вызова кнопок
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Exception:" + e.toString());
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
