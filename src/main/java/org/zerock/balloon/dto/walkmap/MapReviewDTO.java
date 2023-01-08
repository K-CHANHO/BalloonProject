package org.zerock.balloon.dto.walkmap;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MapReviewDTO {
    private Long rno;

    private String id;

    private String nickname;

    private String content;

    private Long mno;

    private LocalDateTime regDate, modDate;
}
