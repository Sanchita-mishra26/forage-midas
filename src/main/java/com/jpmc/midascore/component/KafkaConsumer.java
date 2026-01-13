package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KafkaConsumer {
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final RestTemplate restTemplate;

    public KafkaConsumer(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository, RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.restTemplate = restTemplate;
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    public void listen(Transaction transaction) {
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {

            // 1. Call the Incentive API
            String url = "http://localhost:8080/incentive";
            Incentive incentiveResponse = restTemplate.postForObject(url, transaction, Incentive.class);
            float incentiveAmount = (incentiveResponse != null) ? incentiveResponse.getAmount() : 0f;

            // 2. Update balances
            // Sender: Subtract only the transaction amount
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            // Recipient: Add transaction amount PLUS the incentive
            recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

            // 3. Save Users
            userRepository.save(sender);
            userRepository.save(recipient);

            // 4. Save Transaction Record with the incentive amount
            TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount());
            record.setIncentive(incentiveAmount); // Make sure you added this field to TransactionRecord
            transactionRecordRepository.save(record);
        }
    }
}