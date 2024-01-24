package ru.sumehu.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.sumehu.service.UpdateProducer;
import ru.sumehu.utils.MessageUtils;

import static org.sumehu.model.RabbitQueue.*;

@Component
@Log4j
public class UpdateController {
    private TelegramBot telegramBot;
    private MessageUtils messageUtils;
    private UpdateProducer updateProducer;

    public UpdateController(MessageUtils messageUtils, UpdateProducer updateProducer) {
        this.messageUtils = messageUtils;
        this.updateProducer = updateProducer;
    }
    // связываем бот и контроллер
    public void registerBot (TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }
    // первичная валидация данных
    public void processUpdate(Update update) {
        if (update == null) {
            log.error("Update is null");
            return;
        }
        if(update.getMessage() != null) {
            distributeMessageByType(update);
        } else {
            log.error("unsupported message type" + update);
        }
    }

    private void distributeMessageByType(Update update) {
        var message = update.getMessage();

        if(message.getText() != null) {
            processText(update);
        }
        else if(message.getDocument() != null) {
            processDocument(update);
        }
        else if(message.getPhoto() != null) {
            processPhoto(update);
        } else {
            setUnsupportedMessageView(update);
        }
    }

    private void setUnsupportedMessageView(Update update) {
        var sendMessage = messageUtils.generateAnswerMessage(update,
                "Неподдерживаемый формат сообщения!");

        setView(sendMessage);
    }
    private void setFileIsReceivedView(Update update) {
        var sendMessage = messageUtils.generateAnswerMessage(update,
                "Файл получен! Обрабатывается...");
        setView(sendMessage);
    }
    public void setView(SendMessage sendMessage) {
        telegramBot.sendAnswer(sendMessage);
    }

    private void processPhoto(Update update) {
        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }
        private void processDocument(Update update) {
        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }

    private void processText(Update update) {
        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
        setFileIsReceivedView(update);
    }
}
