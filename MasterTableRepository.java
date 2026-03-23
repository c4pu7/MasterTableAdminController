

@Repository(MasterTableRepository.REPOSITORY_NAME)
public interface MasterTableRepository  extends JpaRepository<MasterTableEntity, String> {
    String REPOSITORY_NAME="MasterTableRepository";

    Page<MasterTableEntity> findByTableSectionOrderByTableName(String tableSection, Pageable pageable);

    Page<MasterTableEntity> findByTableName(String tableName, Pageable pageable);

}
