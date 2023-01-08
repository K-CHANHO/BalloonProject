package org.zerock.balloon.dto.free;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FreeBoardGoodDTO {
    private Long gno;
    private Long count;
    private String id;
    private Long bno;
    private Long goodCount;
}
