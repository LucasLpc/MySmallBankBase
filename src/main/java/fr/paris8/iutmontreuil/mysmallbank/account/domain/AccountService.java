package fr.paris8.iutmontreuil.mysmallbank.account.domain;

import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.Account;
import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.AccountType;
import fr.paris8.iutmontreuil.mysmallbank.common.ValidationError;
import fr.paris8.iutmontreuil.mysmallbank.account.infrastructure.AccountRepository;
import fr.paris8.iutmontreuil.mysmallbank.common.exception.ValidationErrorException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> listAllAccount() { return accountRepository.listAccount(); }

    public Account getAccount(String accountUid) { return accountRepository.getAccount(accountUid); }

    public Account createAccount(Account account) {
        List<ValidationError> validationErrors = validateAccounts(account);
        if (!validationErrors.isEmpty()) {
            throw new ValidationErrorException(validationErrors);
        }
        return accountRepository.create(account);
    }

    public Account delete(Account account) {
        Account accountDeleted = accountRepository.delete(account);
        return accountDeleted;
    }

    private List<ValidationError> validateAccounts(Account account) {
        List<ValidationError> accountVerified = new ArrayList<ValidationError>();
        ValidationError validationErrorHolder = validateHolder(account.getHolder().getId());
        if (validationErrorHolder != null)
            accountVerified.add(validationErrorHolder);
        ValidationError validationErrorBalance = validateBalance(account.getType(), account.getBalance());
        if (validationErrorBalance != null)
            accountVerified.add(validationErrorBalance);
        return accountVerified;
    }

    private ValidationError validateHolder(String id) {
        if (id == null || id.equals("") || id.isEmpty())
            return new ValidationError("Holder ID is null or empty");
        if (accountRepository.getHolder(id) == null)
            return new ValidationError("Holder doesn't exists");
        return null;
    }

    private ValidationError validateBalance(AccountType accountType, double balance){
        if (accountType == AccountType.PEL && balance < AccountType.PEL.getMinimumBalance()){
            return new ValidationError("Balance is not correct for" + accountType.name());
        }
        if (accountType == AccountType.SAVINGS && balance < AccountType.SAVINGS.getMinimumBalance()){
            return new ValidationError("Balance is not correct for" + accountType.name());
        }
        if (accountType == AccountType.TRANSACTION && balance < AccountType.TRANSACTION.getMinimumBalance()){
            return new ValidationError("Balance is not correct for" + accountType.name());
        }
        return null;
    }


}
