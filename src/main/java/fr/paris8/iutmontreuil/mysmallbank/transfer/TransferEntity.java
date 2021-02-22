package fr.paris8.iutmontreuil.mysmallbank.transfer;

import fr.paris8.iutmontreuil.mysmallbank.account.infrastructure.AccountEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table( name = "transfer" )
public class TransferEntity {

    @Id
    @GeneratedValue( generator = "uuid2" )
    @GenericGenerator( name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator" )
    private String id;

    @ManyToOne
    @JoinColumn(name = "from_account_id", nullable = false)
    private AccountEntity from;

    @ManyToOne
    @JoinColumn(name = "to_account_id", nullable = false)
    private AccountEntity to;

    @Column(name = "amount")
    private double amount;

    @Column(name = "execution_date")
    private LocalDateTime executionDate;

    /*-------------------------------------------------------------------------*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AccountEntity getFrom() {
        return from;
    }

    public void setFrom(AccountEntity from) {
        this.from = from;
    }

    public AccountEntity getTo() {
        return to;
    }

    public void setTo(AccountEntity to) {
        this.to = to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(LocalDateTime executionDate) {
        this.executionDate = executionDate;
    }
}
