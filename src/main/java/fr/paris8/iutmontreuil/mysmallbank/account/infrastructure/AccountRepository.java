package fr.paris8.iutmontreuil.mysmallbank.account.infrastructure;

import fr.paris8.iutmontreuil.mysmallbank.account.AccountMapper;
import fr.paris8.iutmontreuil.mysmallbank.account.HolderMapper;
import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.Account;
import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.Holder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AccountRepository {

    private final AccountDAO accountDAO;
    private final HolderDAO holderDAO;

    public AccountRepository(AccountDAO accountDAO, HolderDAO holderDAO) {
        this.accountDAO = accountDAO;
        this.holderDAO = holderDAO;
    }

    public Holder getHolder(String id) {
        HolderEntity holder = holderDAO.getOne(id);
        return HolderMapper.toHolder(holder);
    }

    public Account getAccount(String id) {
        AccountEntity account = accountDAO.getOne(id);
        return AccountMapper.toAccount(account);
    }

    public List<Account> listAccount() {
        List<AccountEntity> accountEntities = accountDAO.findAll();
        List<Account> accounts = accountEntities.stream()
                .map(AccountMapper::toAccount)
                .collect(Collectors.toList());
        return accounts;
    }

    public Account create(Account account) {
        AccountEntity entityToSave = AccountMapper.toEntity(account);
        AccountEntity createdAccount = accountDAO.save(entityToSave);
        return AccountMapper.toAccount(createdAccount);
    }

    public Account delete(Account account) {
        AccountEntity accountEntity = AccountMapper.toEntity(account);
        accountDAO.delete(accountEntity);
        return AccountMapper.toAccount(accountEntity);
    }


    public void save(Account accountFrom) {

    }
}
