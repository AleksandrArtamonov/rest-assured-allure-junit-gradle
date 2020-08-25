package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pagination {
    private Integer total;
    private Integer pages;
    private Integer page;
    private Integer limit;
}
