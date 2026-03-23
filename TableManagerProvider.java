package eu.unicredit.xframe.qrh.admin.rs.service;

import eu.unicredit.xframe.qrh.admin.rs.api.validation.ITableManager;
import eu.unicredit.xframe.qrh.admin.rs.dto.BaseTableRequestDTO;
import eu.unicredit.xframe.qrh.admin.rs.utility.HandleTableName;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provider finale delle strategie (manager specifici).
 * Pattern identico a RatingValidatorManagerProvider:
 *
 * tableName -> Manager
 */
@Component
public class TableManagerProvider {


    private final Map<String, ITableManager<? extends BaseTableRequestDTO>> managers = new HashMap<>();

    public TableManagerProvider(List<ITableManager<? extends BaseTableRequestDTO>> managerList) {

        for (ITableManager<? extends BaseTableRequestDTO> manager : managerList) {

            HandleTableName ann = manager.getClass().getAnnotation(HandleTableName.class);
            if (ann == null)
                throw new IllegalStateException("Missing @HandleTableName on " + manager.getClass());

            managers.put(ann.value().toUpperCase(), manager);
        }
    }

    /**
     * Restituisce il manager corretto in base al tableName.
     */

    public ITableManager<? extends BaseTableRequestDTO> resolve(String tableName) {
        ITableManager<? extends BaseTableRequestDTO> mgr =
                managers.get(tableName.toUpperCase());

        if (mgr == null)
            throw new IllegalArgumentException("No manager for table " + tableName);

        return mgr;
    }
}