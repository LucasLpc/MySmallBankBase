package fr.paris8.iutmontreuil.mysmallbank.transfer;

import fr.paris8.iutmontreuil.mysmallbank.account.infrastructure.HolderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferDAO extends JpaRepository<TransferEntity, String> { }