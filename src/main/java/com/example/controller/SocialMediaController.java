package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@Controller
public class SocialMediaController {

    private MessageService messageService;
    private AccountService accountService;

    private AccountRepository accountRepository;
    private MessageRepository messageRepository;

    @Autowired
    public SocialMediaController(MessageService messageService,AccountService accountService,AccountRepository accountRepository, MessageRepository messageRepository){
        this.messageService = messageService;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.messageRepository = messageRepository;
    }

        

    @PostMapping("/register")
    public @ResponseBody ResponseEntity<Account> registerAccount (@RequestBody Account account){
        //Account a = accountService.registerAccount(account);
        
        try {
            Account registeredAccount = accountService.registerAccount(account);
            return ResponseEntity.ok(registeredAccount);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
    }

    @PostMapping("/login")
    public @ResponseBody ResponseEntity<Account> loginAccount(@RequestBody Account account){
        
        try {
            Account a = accountService.loginAccount(account.getUsername(), account.getPassword());
            return ResponseEntity.ok(a);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
    }

    @PostMapping("/messages")
    public @ResponseBody ResponseEntity<Message> createMessageText(@RequestBody Message message){
        try {
            Message newMessage = messageService.createMessage(message);
            return ResponseEntity.ok(newMessage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
    }

    @GetMapping("/messages")
    public @ResponseBody ResponseEntity<List<Message>>getAllMessages(){
      List<Message> ms =  messageService.getAllMessages();
       // if(messageService.getAllMessages().equals(null)){
       //     return null;
        //}
        return ResponseEntity.status(200).body(ms);
    }

    @GetMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<Message> getMessageById(@PathVariable int messageId){
       Message ms=  messageService.getMessageById(messageId);
        return ResponseEntity.status(200).body(ms);
    }

    @DeleteMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<Integer> deleteMessageById(@PathVariable int messageId){
        int md = messageService.deleteMessageById(messageId);
        if(md == 0){
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.status(200).body(md);
    }

    @PatchMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<Integer> updateMessageById(@PathVariable int messageId, @RequestBody String messageText){
        try {
            int updatedRows = messageService.updateMessageById(messageId, messageText);
            if(updatedRows == 0){
                return ResponseEntity.status(400).build();
            }
            return ResponseEntity.ok(updatedRows);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        
    }

    @GetMapping("/accounts/{accountId}/messages")
    public @ResponseBody ResponseEntity<List<Message>> getAllMessagesByPostedBy(@PathVariable int accountId){
        List<Message> ma = messageService.getAllMessagesByPostedBy(accountId);
        return ResponseEntity.status(200).body(ma);
    }
}
