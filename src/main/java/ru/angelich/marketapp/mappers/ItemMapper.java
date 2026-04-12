package ru.angelich.marketapp.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.angelich.marketapp.models.Item;
import ru.angelich.marketapp.models.ItemDto;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    // @Mapping(target = "count", defaultValue = "0")
    ItemDto toDto(Item item);
}
