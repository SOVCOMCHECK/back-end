package ru.sovcomcheck.back_end.checkservice.dtos.classifier.mapping;

import ru.sovcomcheck.back_end.checkservice.dtos.Check;
import ru.sovcomcheck.back_end.checkservice.dtos.Item;
import ru.sovcomcheck.back_end.checkservice.dtos.classifier.CheckMl;
import java.util.List;

public class CheckMapping {

    public static CheckMl toMl(Check check) {
        List<String> products = check
                .getData()
                .getJson()
                .getItems()
                .stream().map(Item::getName).toList();
        return CheckMl.builder().products(products).build();
    }
}
