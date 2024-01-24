package org.sumehu.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {
    void ConsumeTextMessageUpdates(Update update);
    void ConsumeDocMessageUpdates(Update update);
    void ConsumePhotoMessageUpdates(Update update);
}
