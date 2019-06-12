package uk.ac.bbsrc.tgac.miso.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.bbsrc.tgac.miso.core.data.LibraryDesignCode;
import uk.ac.bbsrc.tgac.miso.core.store.DeletionStore;
import uk.ac.bbsrc.tgac.miso.core.store.LibraryDesignCodeDao;
import uk.ac.bbsrc.tgac.miso.core.store.SaveDao;
import uk.ac.bbsrc.tgac.miso.service.AbstractSaveService;
import uk.ac.bbsrc.tgac.miso.service.LibraryDesignCodeService;
import uk.ac.bbsrc.tgac.miso.service.exception.ValidationError;
import uk.ac.bbsrc.tgac.miso.service.exception.ValidationResult;
import uk.ac.bbsrc.tgac.miso.service.security.AuthorizationManager;

@Transactional(rollbackFor = Exception.class)
@Service
public class DefaultLibraryDesignCodeService extends AbstractSaveService<LibraryDesignCode> implements LibraryDesignCodeService {

  @Autowired
  private LibraryDesignCodeDao libraryDesignCodeDao;

  @Autowired
  private DeletionStore deletionStore;

  @Autowired
  private AuthorizationManager authorizationManager;

  @Override
  public DeletionStore getDeletionStore() {
    return deletionStore;
  }

  @Override
  public AuthorizationManager getAuthorizationManager() {
    return authorizationManager;
  }

  @Override
  public SaveDao<LibraryDesignCode> getDao() {
    return libraryDesignCodeDao;
  }

  public void setLibraryDesignCodeDao(LibraryDesignCodeDao libraryDesignCodeDao) {
    this.libraryDesignCodeDao = libraryDesignCodeDao;
  }

  @Override
  public List<LibraryDesignCode> list() throws IOException {
    return libraryDesignCodeDao.list();
  }

  @Override
  protected void authorizeSave(LibraryDesignCode object) throws IOException {
    authorizationManager.throwIfNonAdmin();
  }

  @Override
  protected void collectValidationErrors(LibraryDesignCode object, LibraryDesignCode beforeChange, List<ValidationError> errors)
      throws IOException {
    if (ValidationUtils.isSetAndChanged(LibraryDesignCode::getCode, object, beforeChange)
        && libraryDesignCodeDao.getByCode(object.getCode()) != null) {
      errors.add(new ValidationError("code", "There is already a library design code with this code"));
    }
  }

  @Override
  protected void applyChanges(LibraryDesignCode to, LibraryDesignCode from) {
    to.setCode(from.getCode());
    to.setDescription(from.getDescription());
    to.setTargetedSequencingRequired(from.isTargetedSequencingRequired());
  }

  @Override
  public ValidationResult validateDeletion(LibraryDesignCode object) throws IOException {
    ValidationResult result = new ValidationResult();
    long libUsage = libraryDesignCodeDao.getUsageByLibraries(object);
    if (libUsage > 0L) {
      result.addError(
          new ValidationError(
              "Library design code '" + object.getCode() + "' is used by " + libUsage + " librar" + (libUsage > 1L ? "ies" : "y")));
    }
    long designUsage = libraryDesignCodeDao.getUsageByLibraryDesigns(object);
    if (libUsage > 0L) {
      result.addError(
          new ValidationError(
              "Library design code '" + object.getCode() + "' is used by " + designUsage + " library design"
                  + (designUsage > 1L ? "s" : "")));
    }
    return result;
  }

}
