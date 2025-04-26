package ru.sovcomcheck.back_end.checkservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.sovcomcheck.back_end.checkservice.documents.CheckDocument;
import ru.sovcomcheck.back_end.checkservice.dtos.CheckAnalyticsDTO;
import ru.sovcomcheck.back_end.checkservice.dtos.Item;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CheckMapper {
    CheckMapper INSTANCE = Mappers.getMapper(CheckMapper.class);

    @Mapping(target = "dateTime", source = "checkData.data.json.dateTime")
    @Mapping(target = "organization", source = "checkData.data.json.user")
    @Mapping(target = "address", source = "checkData.data.json.retailPlaceAddress")
    @Mapping(target = "inn", source = "checkData.data.json.userInn")
    @Mapping(target = "operationType", source = "checkData.data.json.operationType")
    @Mapping(target = "items", source = "checkData.data.json.items", qualifiedByName = "mapItems")
    @Mapping(target = "totalSum", source = "checkData.data.json.totalSum")
    @Mapping(target = "cashTotalSum", source = "checkData.data.json.cashTotalSum")
    @Mapping(target = "ecashTotalSum", source = "checkData.data.json.ecashTotalSum")
    @Mapping(target = "taxationType", source = "checkData.data.json.taxationType")
    CheckAnalyticsDTO toAnalyticsDTO(CheckDocument checkDocument);

    @Named("mapItems")
    default List<Item> mapItems(List<Item> items) {
        return items.stream().map(item -> Item.builder()
                        .name(item.getName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .sum(item.getSum())
                        .ndsRate(getNdsRate(item))
                        .build())
                .collect(Collectors.toList());
    }

    private Integer getNdsRate(Item item) {
        if (item.getNdsRate() != null && item.getNdsRate() > 0) {
            return item.getNdsRate() == 1 ? 20 : 10;
        }
        return 0;
    }
}
