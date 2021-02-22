package fr.paris8.iutmontreuil.mysmallbank.transfer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public TransferDTO create(@RequestBody TransferDTO transferDTO){
        Transfer transfer = transferService.save(TransferMapper.toTransfer(transferDTO));
        return TransferMapper.toDTO(transfer);
    }

    @GetMapping
    public ResponseEntity<List<TransferDTO>> listAllTransfers(@RequestParam("sort") String param){
        if (param.equals("asc")){
             List<Transfer> transfersASC = transferService.listAllByASC();
             return ResponseEntity.ok(transfersASC.stream()
                     .map(TransferMapper::toDTO)
                     .collect(Collectors.toList()));
        }else if (param.equals("desc")){
            List<Transfer> transfersDESC = transferService.listAllByDESC();
            return ResponseEntity.ok(transfersDESC.stream()
                    .map(TransferMapper::toDTO)
                    .collect(Collectors.toList()));
        }
        return ResponseEntity.badRequest().build();
    }
}
