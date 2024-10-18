package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
@Transactional
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isEmpty() || message.getMessageText().length() > 255) {
            throw new IllegalArgumentException("Invalid message text");
        }
        if (!accountRepository.existsById(message.getPostedBy())) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        return messageRepository.save(message);
    }
    

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageById(int messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }
    
    public int deleteMessageById(int messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return 1;
        }
       
        return 0;
    }
    public int updateMessageById(int messageId, String messageText) {
        if (messageText == null || messageText.isEmpty() || messageText.length() > 255) {
            //throw new IllegalArgumentException("Invalid message text");
            return 0;
        }
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message != null) {
            message.setMessageText(messageText);
            messageRepository.save(message);
            return 1;
        }
        return 0;
    }
    public List<Message> getAllMessagesByPostedBy(int postedBy) {
        if (!accountRepository.existsById(postedBy)) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        return messageRepository.findByPostedBy(postedBy);
    }
    
}
