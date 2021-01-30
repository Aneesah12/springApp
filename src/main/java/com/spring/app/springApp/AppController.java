package com.spring.app.springApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class AppController {

    private final TransactionRepository transactionRepository;

    @Autowired
    public AppController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @PostMapping(value = "/payments", consumes = {"application/json"})
    public ResponseEntity<Transactions> payments(@RequestBody Transactions savedTransactions) {
        Transactions save = transactionRepository.save(savedTransactions);
        return new ResponseEntity<>(save, HttpStatus.CREATED);
    }

    @PutMapping(value = "/updatePayment/{id}", consumes = ("application/json"))
    public ResponseEntity<Transactions> updatePayments(@PathVariable("id") Long id,
                                                       @RequestBody Transactions transaction) {
        Optional<Transactions> transactionOptional = transactionRepository.findById(id);

        if (transactionOptional.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        transaction.setId(id);

        Transactions updatedTransaction = transactionRepository.save(transaction);
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }

    @GetMapping(value = "/allPayments", produces = {"application/json"})
    public ResponseEntity<Iterable<Transactions>> getAllPayments() {
        Iterable<Transactions> allTransactions = transactionRepository.findAll();
        return new ResponseEntity<>(allTransactions, HttpStatus.OK);
    }

    @GetMapping(value = "/payment/{id}", produces = {"application/json"})
    public ResponseEntity<Optional<Transactions>> getPaymentById(@PathVariable("id") Long id) {
        Optional<Transactions> transaction = transactionRepository.findById(id);

        if(transaction.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @DeleteMapping(value = "/deleteAllPayments")
    public ResponseEntity<Transactions> deleteAllPayments() {
        transactionRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/deletePayment/{id}")
    public ResponseEntity<Transactions> deletePaymentById(@PathVariable("id") Long id) {
        transactionRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
