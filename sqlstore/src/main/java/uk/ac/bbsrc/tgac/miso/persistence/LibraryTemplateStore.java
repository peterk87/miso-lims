package uk.ac.bbsrc.tgac.miso.persistence;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import uk.ac.bbsrc.tgac.miso.core.data.impl.LibraryTemplate;
import uk.ac.bbsrc.tgac.miso.core.util.PaginatedDataSource;

public interface LibraryTemplateStore extends PaginatedDataSource<LibraryTemplate> {

  LibraryTemplate get(long id) throws IOException;

  long create(LibraryTemplate libraryTemplate) throws IOException;

  void update(LibraryTemplate libraryTemplate) throws IOException;

  List<LibraryTemplate> list() throws IOException;

  List<LibraryTemplate> listLibraryTemplatesForProject(long projectId);

  Collection<LibraryTemplate> listByIdList(List<Long> idList) throws IOException;

}
