package ru.sovcomcheck.back_end.checkservice.dtos.classifier;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckMl {

    private List<String> products = new ArrayList<>();
}
