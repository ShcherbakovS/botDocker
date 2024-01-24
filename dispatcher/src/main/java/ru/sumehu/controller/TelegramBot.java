package ru.sumehu.controller;

import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import javax.annotation.PostConstruct;


@Component
@Log4j
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
//    private static final Logger log = Logger.getLogger(TelegramBot.class);
    private UpdateController updateController;

    public TelegramBot(UpdateController updateController) {
        this.updateController = updateController;
    }
    // внедряем контроллер, передаем ссылку на бот внутроь контролера
    @PostConstruct
    public void init() {
        updateController.registerBot(this);
    }
    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        var originalMessage = update.getMessage();
        updateController.processUpdate(update);

    }
    public void sendAnswer(SendMessage sendMessage) {
        if(sendMessage != null) {
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                log.debug(e);
            }
        }
    }
}
