package com.hdda.dto.request;

import com.hdda.enums.SortField;
import com.hdda.enums.SortOrder;
import lombok.Data;

@Data
public class SearchRouteRequestDTO {
    private String from;
    private String to;
    private SortField sortBy;  // TIME | DISTANCE | TRANSFERS (nullable)
    private SortOrder order;   // ASC | DESC (nullable)
}
