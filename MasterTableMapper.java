package eu.unicredit.xframe.qrh.admin.rs.mapper;

import eu.unicredit.qrh.admin.model.entity.MasterTableEntity;
import eu.unicredit.qrh.admin.dto.MasterTableResponseDTO;
import eu.unicredit.qrh.admin.dto.UpdateRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface MasterTableMapper {

    MasterTableResponseDTO toResponseDto(MasterTableEntity entity);

    void updateEntityFromRequest(UpdateRequestDTO requestDTO, @MappingTarget MasterTableEntity entity);

}
