package org.zerock.balloon.entity.qna;

import lombok.*;
import org.zerock.balloon.entity.BaseEntity;
import org.zerock.balloon.entity.Member;

import javax.persistence.*;

@Entity
@Table(name="qna_board_reply")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"qnaBoard"})
public class QnaReply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qbrno;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private String id;

    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    private QnaBoard qnaBoard;
}
