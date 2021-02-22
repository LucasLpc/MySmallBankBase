package fr.paris8.iutmontreuil.mysmallbank.account.infrastructure;

import fr.paris8.iutmontreuil.mysmallbank.account.AccountMapper;
import fr.paris8.iutmontreuil.mysmallbank.account.HolderMapper;
import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.Account;
import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.Holder;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class HolderRepository {

    private final HolderDAO holderDAO;
    private final AccountDAO accountDAO;

    public HolderRepository(HolderDAO holderDAO, AccountDAO accountDAO) {
        this.holderDAO = holderDAO;
        this.accountDAO = accountDAO;
    }

    public Holder getHolder(String id) {
        HolderEntity holderEntity = holderDAO.getOne(id);
        Holder holder = HolderMapper.toHolder(holderEntity);
        List<AccountEntity> allAccounts = accountDAO.findAll();
        List<Account> accounts = allAccounts.stream()
                .filter(x->x.getHolder().getId() == holderEntity.getId())
                .map(AccountMapper::toAccount)
                .collect(Collectors.toList());
        return new Holder(holder.getId(), holder.getLastName(), holder.getFirstName(), holder.getAddress(), holder.getBirthDate(), accounts);
    }

    public List<Holder> listHolders() {
        List<HolderEntity> holdersEntities = holderDAO.findAll();
        List<Holder> allHolders = holdersEntities.stream()
                .map(HolderMapper::toHolder)
                .collect(Collectors.toList());
        List<AccountEntity> allAccounts = accountDAO.findAll();
        List<Holder> holders = new ArrayList<>();
        for (Holder holder :
                allHolders) {
            List<Account> accounts = allAccounts.stream()
                    .filter(x->x.getHolder().getId() == holder.getId())
                    .map(AccountMapper::toAccount)
                    .collect(Collectors.toList());
            holders.add(new Holder(holder.getId(), holder.getLastName(), holder.getFirstName(), holder.getAddress(), holder.getBirthDate(), accounts));
        }
        return holders;
    }

    public Holder save(Holder holder) {
        HolderEntity entityToSave = HolderMapper.toEntity(holder);
        HolderEntity createdHolder = holderDAO.save(entityToSave);
        return HolderMapper.toHolder(createdHolder);
    }


    public Holder delete(Holder holder) {
        Holder holderToDelete = getHolder(holder.getId());
        if (holderToDelete.getAccounts().isEmpty()) {
            holderDAO.delete(HolderMapper.toEntity(holder));
            return holderToDelete;
        }
        return null;
    }
}
