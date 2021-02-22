package fr.paris8.iutmontreuil.mysmallbank.account;

import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.Account;
import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.Address;
import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.Holder;
import fr.paris8.iutmontreuil.mysmallbank.account.exposition.dto.AccountDTO;
import fr.paris8.iutmontreuil.mysmallbank.account.exposition.dto.AddressDTO;
import fr.paris8.iutmontreuil.mysmallbank.account.exposition.dto.HolderDTO;
import fr.paris8.iutmontreuil.mysmallbank.account.infrastructure.AccountEntity;
import fr.paris8.iutmontreuil.mysmallbank.account.infrastructure.HolderEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HolderMapper {

    private HolderMapper() { }

    public static Holder toHolder(HolderEntity entity) {
        return new Holder(entity.getId(), entity.getLastName(), entity.getFirstName(), new Address(entity.getStreet(), entity.getZipCode(), entity.getCity(), entity.getCountry()), entity.getBirthDate(), toAccount(entity.getAccounts()));
    }

    public static HolderEntity toEntity(Holder holder) {
        HolderEntity entity = new HolderEntity();
        entity.setBirthDate(holder.getBirthDate());
        entity.setFirstName(holder.getFirstName());
        entity.setLastName(holder.getLastName());
        entity.setId(holder.getId());
        Address address = holder.getAddress();
        entity.setZipCode(address.getZipCode());
        entity.setStreet(address.getStreet());
        entity.setCity(address.getCity());
        entity.setCountry(address.getCountry());
        entity.setAccounts(AccountMapper.toEntities(holder.getAccounts()));

        return entity;
    }

    public static HolderDTO toDTO(Holder holder) {
        HolderDTO holderDTO = new HolderDTO();
        holderDTO.setId(holder.getId());
        holderDTO.setLastName(holder.getLastName());
        holderDTO.setFirstName(holder.getFirstName());
        holderDTO.setAddress(HolderMapper.toDTO(holder.getAddress()));
        holderDTO.setBirthDate(holder.getBirthDate());
        holderDTO.setAccounts(toAccounts(holder.getAccounts()));
        return holderDTO;
    }

    public static List<HolderDTO> toDto(List<Holder> holders) {
        return holders.stream()
                .map(HolderMapper::toDTO)
                .collect(Collectors.toList());
    }

    private static Account toAccount(AccountEntity entity) {
        return new Account(entity.getUid(), null, entity.getAccountType(), entity.getBalance());
    }

    private static AccountDTO toAccount(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setType(account.getType());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setUid(account.getUid());
        return accountDTO;
    }

    public static Address toAddress(AddressDTO addressDTO) {
        if (addressDTO == null) return null;
        return new Address(addressDTO.getStreet(), addressDTO.getZipCode(), addressDTO.getCity(), addressDTO.getCountry());
    }

    public static AddressDTO toDTO(Address address){
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCountry(address.getCountry());
        addressDTO.setCity(address.getCity());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setZipCode(address.getZipCode());
        return addressDTO;
    }

    public static Holder toHolder(HolderDTO holderDTO) {
        return new Holder(null, holderDTO.getLastName(), holderDTO.getFirstName(), toAddress(holderDTO.getAddress()),
                holderDTO.getBirthDate(), Collections.emptyList());
    }

    private static List<AccountDTO> toAccounts(List<Account> accounts) {
        if(accounts == null) {
            return Collections.emptyList();
        }
        return accounts.stream()
                .map(HolderMapper::toAccount)
                .collect(Collectors.toList());
    }

    private static List<Account> toAccount(List<AccountEntity> entities) {
        if(entities == null) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(HolderMapper::toAccount)
                .collect(Collectors.toList());
    }

}
