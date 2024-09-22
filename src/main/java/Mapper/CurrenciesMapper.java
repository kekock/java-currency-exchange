package Mapper;

import DTO.CurrenciesDTO;
import Entity.Currencies;

import java.util.List;
import java.util.stream.Collectors;

public class CurrenciesMapper {

    public static CurrenciesDTO toDTO(Currencies currency) {
        CurrenciesDTO dto = new CurrenciesDTO();
        dto.setId(currency.getId());
        dto.setName(currency.getFullName());
        dto.setCode(currency.getCode());
        dto.setSign(currency.getSign());
        return dto;
    }

    public static List<CurrenciesDTO> toDTOList(List<Currencies> currencies) {
        return currencies.stream()
                .map(CurrenciesMapper::toDTO)
                .collect(Collectors.toList());
    }
}