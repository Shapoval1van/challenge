package com.dws.challenge.repository;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.AccountAlreadyChangedException;
import com.dws.challenge.exception.DuplicateAccountIdException;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void createAccount(Account account) throws DuplicateAccountIdException {
        Account previousAccount = accounts.putIfAbsent(account.getAccountId(), new Account(account));
        if (previousAccount != null) {
            throw new DuplicateAccountIdException(
                    "Account id " + account.getAccountId() + " already exists!");
        }
    }

    @Override
    public Optional<Account> getAccount(String accountId) {
        return Optional.ofNullable(accounts.get(accountId)).map(Account::new);
    }

    @Override
    public void updateAccount(String accountId, Account account) {
        Account accountsToSave = new Account(account);
        accounts.computeIfPresent(accountId, (id, existing) -> {
            if(existing.getVersion() >= accountsToSave.getVersion()) {
                throw new AccountAlreadyChangedException(accountId);
            }
            return accountsToSave;
        });
    }

    @Override
    public void clearAccounts() {
        accounts.clear();
    }

}
