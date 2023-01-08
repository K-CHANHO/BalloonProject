package org.zerock.balloon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
    private int page;
    private int size;
    private String subject; // 도보, 자전거, 애견
    private String type; //검색컬럼
    private String keyword;//검색어

    public PageRequestDTO(){
        this.page=1; // 기본page 1page
        this.size=10; // 기본size 10.  10개/page

    }
    public Pageable getPageable(Sort sort){
        return PageRequest.of(page-1,size,sort);
    }
}
