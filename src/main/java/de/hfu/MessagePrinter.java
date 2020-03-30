package de.hfu;

import de.hfu.messages.domain.model.Message;
import de.hfu.messages.domain.service.MessageService;
import de.hfu.messages.domain.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MessagePrinter {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SecurityService securityService;

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public List<Message> outputMessages() {
        return new ArrayList<>(messageService.findAllMessages());
    }
}
