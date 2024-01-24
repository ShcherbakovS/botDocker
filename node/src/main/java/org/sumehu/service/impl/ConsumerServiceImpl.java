package org.sumehu.service.impl;

import lombok.extern.log4j.Log4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.sumehu.service.ConsumerService;
import org.sumehu.service.MainService;
import org.sumehu.service.ProducerService;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.sumehu.model.RabbitQueue.*;

@Service
@Log4j
public class ConsumerServiceImpl implements ConsumerService {
    private final MainService mainService;
    private final ProducerService producerService;

    public ConsumerServiceImpl(MainService mainService, ProducerService producerService) {
        this.mainService = mainService;
        this.producerService = producerService;
    }

    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void ConsumeTextMessageUpdates(Update update) {
        log.debug("NODE: Text message is received");
        mainService.processTextMessage(update);
    }
    @Override
    @RabbitListener(queues = DOC_MESSAGE_UPDATE)
    public void ConsumeDocMessageUpdates(Update update) {
        log.debug("NODE: Doc message is received");
    }
    @Override
    @RabbitListener(queues = PHOTO_MESSAGE_UPDATE)
    public void ConsumePhotoMessageUpdates(Update update) {
        log.debug("NODE: Photo message is received");
    }
}
