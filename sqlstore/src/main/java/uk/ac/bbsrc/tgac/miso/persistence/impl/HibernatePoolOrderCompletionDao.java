package uk.ac.bbsrc.tgac.miso.persistence.impl;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import uk.ac.bbsrc.tgac.miso.core.data.PoolOrderCompletion;
import uk.ac.bbsrc.tgac.miso.core.data.type.PlatformType;
import uk.ac.bbsrc.tgac.miso.persistence.PoolOrderCompletionDao;

@Transactional(rollbackFor = Exception.class)
@Repository
public class HibernatePoolOrderCompletionDao implements PoolOrderCompletionDao, HibernatePaginatedDataSource<PoolOrderCompletion> {
  private static final String[] SEARCH_PROPERTIES = new String[] { "pool.alias", "pool.name", "pool.identificationBarcode",
      "pool.description" };
  private static final List<String> STANDARD_ALIASES = Arrays.asList("pool", "parameters");

  @Autowired
  private SessionFactory sessionFactory;

  @Override
  public Session currentSession() {
    return sessionFactory.getCurrentSession();
  }

  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  @Override
  public String getProjectColumn() {
    throw new IllegalArgumentException();
  }

  @Override
  public Class<? extends PoolOrderCompletion> getRealClass() {
    return PoolOrderCompletion.class;
  }

  @Override
  public String[] getSearchProperties() {
    return SEARCH_PROPERTIES;
  }

  @Override
  public Iterable<String> listAliases() {
    return STANDARD_ALIASES;
  }

  @Override
  public String propertyForSortColumn(String original) {
    return original;
  }

  @Override
  public void setFulfilled(Criteria criteria, boolean isFulfilled) {
    criteria.add(isFulfilled ? Restrictions.le("remaining", 0) : Restrictions.gt("remaining", 0));
  }

  @Override
  public void setPlatformType(Criteria criteria, PlatformType platformType) {
    criteria.add(Restrictions.eq("pool.platformType", platformType));
  }

  @Override
  public void setPoolId(Criteria criteria, long poolId) {
    criteria.add(Restrictions.eq("pool.id", poolId));
  }

}
