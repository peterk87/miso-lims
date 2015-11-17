package uk.ac.bbsrc.tgac.miso.core.service.naming;

import static uk.ac.bbsrc.tgac.miso.core.util.LimsUtils.isStringEmptyOrNull;

import net.sourceforge.fluxion.spi.ServiceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.bbsrc.tgac.miso.core.data.Nameable;
import uk.ac.bbsrc.tgac.miso.core.exception.MisoNamingException;

/**
 * uk.ac.bbsrc.tgac.miso.core.service.naming
 * <p/>
 * Info
 * 
 * @author Rob Davey
 * @date 27/09/12
 * @since 0.1.8
 */
@ServiceProvider
public class AllowAnythingEntityNamingScheme<T extends Nameable> implements MisoNamingScheme<T> {
  protected static final Logger log = LoggerFactory.getLogger(AllowAnythingEntityNamingScheme.class);

  private Class<T> type;

  public AllowAnythingEntityNamingScheme() {
    try {
      type = (Class<T>) Class.forName("uk.ac.bbsrc.tgac.miso.core.data.Nameable");
    } catch (ClassNotFoundException e) {
      log.error("constructor", e);
    }
  }

  public AllowAnythingEntityNamingScheme(Class<T> type) {
    this.type = type;
  }

  @Override
  public void setValidationRegex(String fieldName, String regex) throws MisoNamingException {
  }

  @Override
  public Class<T> namingSchemeFor() {
    return type;
  }

  public void setNamingSchemeFor(Class<T> type) {
    this.type = type;
  }

  @Override
  public String getSchemeName() {
    return "AllowAnythingEntityNamingScheme";
  }

  @Override
  public String generateNameFor(String fieldName, T o) throws MisoNamingException {
    return o.getClass().getSimpleName() + o.getId();
  }

  @Override
  public String getValidationRegex(String fieldName) throws MisoNamingException {
    return ".*";
  }

  @Override
  public boolean validateField(String fieldName, String entityName) throws MisoNamingException {
    return !isStringEmptyOrNull(entityName) && fieldCheck(fieldName);
  }

  @Override
  public void registerCustomNameGenerator(String fieldName, NameGenerator<T> nameGenerator) {
  }

  @Override
  public void unregisterCustomNameGenerator(String fieldName) {
  }

  @Override
  public boolean allowDuplicateEntityNameFor(String fieldName) {
    return true;
  }

  @Override
  public void setAllowDuplicateEntityName(String fieldName, boolean allow) {
  }

  private boolean fieldCheck(String fieldName) {
        return true;
  }
}
