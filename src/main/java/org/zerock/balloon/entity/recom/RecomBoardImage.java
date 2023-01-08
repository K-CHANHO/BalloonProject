package org.zerock.balloon.entity.recom;

import lombok.*;

import javax.persistence.*;
@Entity
@Table(name="recom_board_image")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "recomBoard")
public class RecomBoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgno;

    private String imgname;

    private String path;

    private String uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    private RecomBoard recomBoard;
}
