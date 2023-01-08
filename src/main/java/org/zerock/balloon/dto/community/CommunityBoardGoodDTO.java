package org.zerock.balloon.dto.community;

import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommunityBoardGoodDTO {
    private Long gno;
    private Long count;
    private String id;
    private Long bno;
    private Long goodCount;
}
