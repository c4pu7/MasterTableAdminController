package eu.unicredit.xframe.qrh.admin.rs.repository;

import eu.unicredit.qrh.admin.model.entity.MasterTableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository(MasterTableRepository.REPOSITORY_NAME)
public interface MasterTableRepository  extends JpaRepository<MasterTableEntity, String> {
    String REPOSITORY_NAME="MasterTableRepository";

    Page<MasterTableEntity> findByTableSectionOrderByTableName(String tableSection, Pageable pageable);

    Page<MasterTableEntity> findByTableName(String tableName, Pageable pageable);

}
