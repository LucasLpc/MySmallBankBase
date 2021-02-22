package fr.paris8.iutmontreuil.mysmallbank.transfer;

import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.Account;
import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.AccountType;
import fr.paris8.iutmontreuil.mysmallbank.account.infrastructure.AccountRepository;
import fr.paris8.iutmontreuil.mysmallbank.common.ValidationError;
import fr.paris8.iutmontreuil.mysmallbank.common.exception.ValidationErrorException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    public TransferService(TransferRepository transferRepository, AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
    }

    public List<Transfer> listAllByASC() {
        return transferRepository.listAllByASC();
    }

    public List<Transfer> listAllByDESC() {
        return transferRepository.listAllByDESC();
    }

    public Transfer save(Transfer transfer) {
        Account accountFrom = accountRepository.getAccount(transfer.getAccountIdFrom());
        Account accountTo = accountRepository.getAccount(transfer.getAccountIdTo());
        List<ValidationError> validationErrors = validateTransfer(transfer, accountFrom);
        if (!validationErrors.isEmpty()) {
            throw new ValidationErrorException(validationErrors);
        }
        Account accountFromUpdated = new Account(accountFrom.getUid(), accountFrom.getHolder(), accountFrom.getType(), (accountFrom.getBalance() - transfer.getAmount()));
        accountRepository.create(accountFromUpdated);
        Account accountToUpdated = new Account(accountTo.getUid(), accountTo.getHolder(), accountTo.getType(), (accountTo.getBalance() + transfer.getAmount()));
        accountRepository.create(accountToUpdated);
        return transferRepository.save(transfer);
    }

    private List<ValidationError> validateTransfer(Transfer transfer, Account accountFrom) {
        List<ValidationError> errors = new ArrayList<>();
        if (validateStringAttribute(transfer.getAccountIdFrom(), "AccountIdFrom") != null)
            errors.add(validateStringAttribute(transfer.getAccountIdFrom(), "AccountIdFrom"));
        if (validateStringAttribute(transfer.getAccountIdTo(), "AccountIdTO") != null)
            errors.add(validateStringAttribute(transfer.getAccountIdTo(), "AccountIdTO"));
        List<ValidationError> dateErrors = validateExecutionDate(transfer.getExecutionDate());
        if (!dateErrors.isEmpty())
            for (ValidationError err :
                    dateErrors) {
                errors.add(err);
            }
        List<ValidationError> amountErrors = validationAmount(transfer.getAmount(), accountFrom);
        if (!amountErrors.isEmpty())
            for (ValidationError err :
                    amountErrors) {
                errors.add(err);
            }
        return errors;
    }

    private ValidationError validateStringAttribute(String value, String attribute) {
        if (value == null || value.isEmpty() || value.equals("")) {
            return new ValidationError(attribute + " null or empty.");
        }
        return null;
    }

    private List<ValidationError> validateExecutionDate(LocalDateTime executionDate) {
        List<ValidationError> validationErrorList = new ArrayList<>();
        if (executionDate == null)
            validationErrorList.add(new ValidationError("ExecutionDate null"));
        if (executionDate.isAfter(LocalDateTime.now()) || executionDate.isBefore(LocalDateTime.now().minusYears(100)))
            validationErrorList.add(new ValidationError("ExecutionDate is wrong / impossible"));
        return validationErrorList;
    }

    private List<ValidationError> validationAmount(double amount, Account accountFrom) {
        List<ValidationError> validationErrorList = new ArrayList<>();
        if (amount < 1.0)
            validationErrorList.add(new ValidationError("Transfer is too small"));
        if (amount > 999999999)
            validationErrorList.add(new ValidationError("Transfer is too high"));
        if (accountFrom.getType() == AccountType.PEL && accountFrom.getBalance() - amount < AccountType.PEL.getMinimumBalance()) {
            validationErrorList.add(new ValidationError("Impossible transfer, " + accountFrom.getType()));
        }
        if (accountFrom.getType() == AccountType.SAVINGS && accountFrom.getBalance() - amount < AccountType.SAVINGS.getMinimumBalance()) {
            validationErrorList.add(new ValidationError("Impossible transfer, " + accountFrom.getType()));
        }
        if (accountFrom.getType() == AccountType.TRANSACTION && accountFrom.getBalance() - amount < AccountType.TRANSACTION.getMinimumBalance()) {
            validationErrorList.add(new ValidationError("Impossible transfer, " + accountFrom.getType()));
        }

        return validationErrorList;
    }
}
