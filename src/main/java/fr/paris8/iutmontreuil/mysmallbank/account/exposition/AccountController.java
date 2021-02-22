package fr.paris8.iutmontreuil.mysmallbank.account.exposition;

import fr.paris8.iutmontreuil.mysmallbank.account.AccountMapper;
import fr.paris8.iutmontreuil.mysmallbank.account.HolderMapper;
import fr.paris8.iutmontreuil.mysmallbank.account.domain.AccountService;
import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.Account;
import fr.paris8.iutmontreuil.mysmallbank.account.exposition.dto.AccountDTO;
import fr.paris8.iutmontreuil.mysmallbank.account.exposition.dto.HolderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<AccountDTO> listAllAccounts() {
        List<Account> accounts = accountService.listAllAccount();
        return AccountMapper.toDTOs(accounts);
    }

    @GetMapping("/{uid}")
    public AccountDTO getAccount(@PathVariable("uid") String uid) {
        Account account = accountService.getAccount(uid);
        return AccountMapper.toDTO(account);
    }


    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) throws URISyntaxException {
        Account createdAccount = accountService.createAccount(AccountMapper.toAccount(accountDTO));
        return ResponseEntity.created(new URI("/accounts/" + createdAccount.getUid())).body(accountDTO);
    }

    @PostMapping("/batch")
    public List<AccountDTO> createAccount(@RequestBody List<AccountDTO> accountDTOS) {
        List<AccountDTO> accountDTOList = new ArrayList<>();
        for (AccountDTO accountDTO:
                accountDTOS) {
            Account account = accountService.createAccount(AccountMapper.toAccount(accountDTO));
            accountDTOList.add(AccountMapper.toDTO(account));
        }
        return accountDTOList;
    }

    @DeleteMapping("/{uid}/delete")
    public ResponseEntity<AccountDTO> deleteAccount(@PathVariable("uid") String uid) throws URISyntaxException{
        Account account = accountService.getAccount(uid);
        Account accountDeleted = accountService.delete(account);
        return ResponseEntity.created(new URI("/holder/"+account.getUid())).body(AccountMapper.toDTO(accountDeleted));
    }

}
