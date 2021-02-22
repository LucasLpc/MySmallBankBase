package fr.paris8.iutmontreuil.mysmallbank.transfer;

import fr.paris8.iutmontreuil.mysmallbank.account.AccountMapper;
import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.Account;
import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.Holder;
import fr.paris8.iutmontreuil.mysmallbank.account.infrastructure.AccountEntity;
import fr.paris8.iutmontreuil.mysmallbank.account.infrastructure.HolderEntity;

import java.util.List;
import java.util.stream.Collectors;

public class TransferMapper {

    private TransferMapper() { }

    public static TransferDTO toDTO(Transfer transfer) {
        TransferDTO transferDTO = new TransferDTO();
        transferDTO.setId(transfer.getId());
        transferDTO.setFrom(transferDTO.getFrom());
        transferDTO.setTo(transfer.getAccountIdTo());
        transferDTO.setAmount(transferDTO.getAmount());
        transferDTO.setExecutionDate(transfer.getExecutionDate());
        return transferDTO;
    }

    public static Transfer toTransfer(TransferDTO transferDTO) {
        return new Transfer(transferDTO.getId(), transferDTO.getFrom(),transferDTO.getTo(),transferDTO.getAmount());
    }

    public static Transfer toTransfer(TransferEntity transferEntity) {
        return new Transfer(transferEntity.getId(), transferEntity.getFrom().getUid(),transferEntity.getTo().getUid(),transferEntity.getAmount());
    }

    public static TransferEntity toEntity(Transfer transfer){
        TransferEntity transferEntity = new TransferEntity();
        transferEntity.setId(transfer.getId());
        AccountEntity accountFrom = new AccountEntity();
        accountFrom.setUid(transfer.getAccountIdFrom());
        transferEntity.setFrom(accountFrom);
        AccountEntity accountTo = new AccountEntity();
        accountTo.setUid(transfer.getAccountIdTo());
        transferEntity.setTo(accountTo);
        transferEntity.setAmount(transfer.getAmount());
        transferEntity.setExecutionDate(transfer.getExecutionDate());
        return transferEntity;
    }

    public static Account toAccount(AccountEntity accountEntity) {
        return AccountMapper.toAccount(accountEntity);
    }

    public static AccountEntity toAccountEntity(Account account) {
        return AccountMapper.toEntity(account);
    }

    public static HolderEntity toHolderEntity(Holder holder) {
        HolderEntity holderEntity = new HolderEntity();
        holderEntity.setLastName(holder.getLastName());
        holderEntity.setFirstName(holder.getFirstName());
        return holderEntity;
    }

    public static Holder toHolder(HolderEntity entity) {
        return new Holder(null, entity.getLastName(), entity.getFirstName(), null, null, null);
    }

    public static List<AccountEntity> toAccountEntities(List<Account> accounts) {
        return accounts.stream()
                .map(TransferMapper::toAccountEntity)
                .collect(Collectors.toList());
    }


}
