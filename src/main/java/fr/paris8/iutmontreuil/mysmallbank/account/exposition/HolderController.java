package fr.paris8.iutmontreuil.mysmallbank.account.exposition;

import fr.paris8.iutmontreuil.mysmallbank.account.HolderMapper;
import fr.paris8.iutmontreuil.mysmallbank.account.domain.HolderService;
import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.Holder;
import fr.paris8.iutmontreuil.mysmallbank.account.exposition.dto.AddressDTO;
import fr.paris8.iutmontreuil.mysmallbank.account.exposition.dto.HolderDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/holders")
public class HolderController {

    private final HolderService holderService;

    public HolderController(HolderService holderService) {
        this.holderService = holderService;
    }

    @GetMapping
    public List<HolderDTO> findAll() {
        List<Holder> allHolders = holderService.listHolders();
        return HolderMapper.toDto(allHolders);
    }

    @GetMapping("/{id}")
    public HolderDTO getOne(@PathVariable("id") String id) {
        Holder holder = holderService.getHolder(id);
        return HolderMapper.toDTO(holder);
    }

    @PostMapping
    public HolderDTO create(@RequestBody HolderDTO holderDTO) {
        Holder holder = holderService.save(HolderMapper.toHolder(holderDTO));
        return HolderMapper.toDTO(holder);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HolderDTO> update(@PathVariable("id") String id, @RequestBody HolderDTO holderDTO) throws URISyntaxException {
        Holder holder= holderService.update(id, HolderMapper.toHolder(holderDTO));
        return ResponseEntity.ok(HolderMapper.toDTO(holder));
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<HolderDTO> updateAdress(@PathVariable("id") String id, @RequestBody AddressDTO addressDTO) throws URISyntaxException {
        Holder holder = holderService.getHolder(id).merge(HolderMapper.toAddress(addressDTO));
        Holder holderUpdated = holderService.save(holder);
        return ResponseEntity.badRequest().body(HolderMapper.toDTO(holderUpdated));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<HolderDTO> delete(@PathVariable("id") String id) throws URISyntaxException{
        Holder holder = holderService.getHolder(id);
        Holder holderDeleted = holderService.delete(holder);
        if (holderDeleted == null)
            return ResponseEntity.badRequest().body(HolderMapper.toDTO(holder));
        return ResponseEntity.ok(HolderMapper.toDTO(holderDeleted));
    }
}
