package ru.angelich.marketapp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.angelich.marketapp.models.Orders;
import ru.angelich.marketapp.models.OrderDto;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "items.description", ignore = true)
    @Mapping(target = "items.imgPath", ignore = true)
    @Mapping(target = "items.count", ignore = true)
    OrderDto toDto(Orders orders);
}
