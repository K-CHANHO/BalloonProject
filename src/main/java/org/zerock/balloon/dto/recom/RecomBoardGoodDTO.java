package org.zerock.balloon.dto.recom;

import lombok.*;
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecomBoardGoodDTO {
    private Long gno;
    private int count;
    private String id;
    private Long bno;
    private Long goodCount;
}
