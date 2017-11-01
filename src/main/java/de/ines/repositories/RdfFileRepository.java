package de.ines.repositories;

import de.ines.domain.RdfFile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RdfFileRepository extends CrudRepository<RdfFile, Long> {

    //RdfFile findByFileNameAndType(@Param("fileName")String fileName, @Param("type")String type);

}
