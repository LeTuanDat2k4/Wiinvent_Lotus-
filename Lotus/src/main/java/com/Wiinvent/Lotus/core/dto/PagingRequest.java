package com.Wiinvent.Lotus.core.dto;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
public class PagingRequest {
    private int page = 0;
    private int size = 10;
    private List<String> sort = List.of("id,desc");

    public Sort toSort() {
        return Sort.by(
                sort.stream()
                        .map(s -> {
                            String[] arr = s.split(",");
                            return new Sort.Order(
                                    arr.length == 2 && arr[1].equalsIgnoreCase("asc")
                                            ? Sort.Direction.ASC
                                            : Sort.Direction.DESC,
                                    arr[0]
                            );
                        })
                        .toList()
        );
    }

    public Pageable getPageable() {
        return PageRequest.of(page, size, toSort());
    }
}
