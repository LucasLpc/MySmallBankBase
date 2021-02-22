package fr.paris8.iutmontreuil.mysmallbank.account.domain;

import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.Address;
import fr.paris8.iutmontreuil.mysmallbank.common.exception.ValidationErrorException;
import fr.paris8.iutmontreuil.mysmallbank.account.domain.model.Holder;
import fr.paris8.iutmontreuil.mysmallbank.common.ValidationError;
import fr.paris8.iutmontreuil.mysmallbank.account.infrastructure.HolderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class HolderService {

    private final HolderRepository holderRepository;

    public HolderService(HolderRepository holderRepository) {
        this.holderRepository = holderRepository;
    }

    public Holder getHolder(String id) {
        return holderRepository.getHolder(id);
    }

    public List<Holder> listHolders() {
        return holderRepository.listHolders();
    }

    public Holder save(Holder holder) {
        List<ValidationError> validationErrors = validateHolder(holder);
        if (!validationErrors.isEmpty()) {
            throw new ValidationErrorException(validationErrors);
        }

        return holderRepository.save(holder);

    }

    public Holder update(String id, Holder holder) {
        Holder holderToUpdate = holderRepository.getHolder(id).merge(holder);
        return holderRepository.save(holderToUpdate);
    }

    public Holder delete(Holder holder) {
        Holder holderToDelete = holderRepository.delete(holder);
        return holderToDelete;
    }

    private List<ValidationError> validateHolder(Holder holder) {
        List<ValidationError> validationErrorList = new ArrayList<>();
        if (validateStringAttribute(holder.getFirstName(), "Firstname") != null)
            validationErrorList.add(validateStringAttribute(holder.getFirstName(), "Firstname"));
        if (validateStringAttribute(holder.getLastName(), "Lastname") != null)
            validationErrorList.add(validateStringAttribute(holder.getLastName(), "Lastname"));
        if (!validateBirthDate(holder.getBirthDate()).isEmpty())
            for (ValidationError err :
                    validateBirthDate(holder.getBirthDate())) {
                validationErrorList.add(err);
            }
        if (!validateAddress(holder.getAddress()).isEmpty())
            for (ValidationError err :
                    validateAddress(holder.getAddress())) {
                validationErrorList.add(err);
            }
        return validationErrorList;
    }

    private List<ValidationError> validateAddress(Address address) {
        List<ValidationError> validationErrorList = new ArrayList<>();
        if (address == null) {
            validationErrorList.add(new ValidationError("Address null"));
        } else {
            if (validateStringAttribute("Country", address.getCountry()) != null) {
                validationErrorList.add(validateStringAttribute("Country", address.getCountry()));
            }
            if (validateStringAttribute("City", address.getCity()) != null) {
                validationErrorList.add(validateStringAttribute("City", address.getCity()));
            }
            if (validateStringAttribute("Street", address.getStreet()) != null) {
                validationErrorList.add(validateStringAttribute("Street", address.getStreet()));
            }
            if (validateStringAttribute("ZipCode", address.getZipCode()) != null) {
                validationErrorList.add(validateStringAttribute("ZipCode", address.getZipCode()));
            }
        }
        return validationErrorList;
    }

    private List<ValidationError> validateBirthDate(LocalDate birthdate) {
        List<ValidationError> validationErrorList = new ArrayList<>();
        if (birthdate == null) {
            validationErrorList.add(new ValidationError("Birthdate null"));
        } else if (birthdate.isAfter(LocalDate.now().minusYears(18)) || birthdate.isBefore(LocalDate.now().minusYears(100))) {
            validationErrorList.add(new ValidationError("Birthdate is wrong / impossible"));
        }
        return validationErrorList;
    }

    private ValidationError validateStringAttribute(String value, String attribute) {
        if (value == null || value.isEmpty() || value.equals("")) {
            return new ValidationError(attribute + " null or empty.");
        }
        return null;
    }
}
