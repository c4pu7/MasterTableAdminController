

@Mapper(componentModel = "spring", imports = {LocalDateTime.class})
public interface MasterTableMapper {

    MasterTableResponseDTO toResponseDto(MasterTableEntity entity);

    void updateEntityFromRequest(UpdateRequestDTO requestDTO, @MappingTarget MasterTableEntity entity);

}
