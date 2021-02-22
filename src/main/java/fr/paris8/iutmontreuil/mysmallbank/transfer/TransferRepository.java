package fr.paris8.iutmontreuil.mysmallbank.transfer;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TransferRepository {

    private final TransferDAO transferDAO;

    public TransferRepository(TransferDAO transferDAO) {
        this.transferDAO = transferDAO;
    }

    public Transfer save(Transfer transfer) {
        TransferEntity transferSaved = transferDAO.save(TransferMapper.toEntity(transfer));
        return TransferMapper.toTransfer(transferSaved);
    }

    public List<Transfer> listAllByASC() {
        List<TransferEntity> allTransfers = transferDAO.findAll(Sort.by(Sort.Direction.ASC, "executionDate"));
        return allTransfers.stream()
                .map(TransferMapper::toTransfer)
                .collect(Collectors.toList());
    }

    public List<Transfer> listAllByDESC() {
        List<TransferEntity> allTransfers = transferDAO.findAll(Sort.by(Sort.Direction.DESC, "executionDate"));
        return allTransfers.stream()
                .map(TransferMapper::toTransfer)
                .collect(Collectors.toList());
    }
}
