package org.zerock.balloon.entity.recom;

import lombok.*;
import org.zerock.balloon.entity.BaseEntity;

import javax.persistence.*;
@Entity
@Table(name="recom_board_reply")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "recomBoard")
public class RecomBoardReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private String nickname;

    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    private RecomBoard recomBoard;
}
