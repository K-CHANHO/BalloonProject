package org.zerock.balloon.entity.qna;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="qna_board_image")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "qnaBoard")
public class QnaBoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qbimgno;

    private String qbimgname;

    private String qbpath;

    private String qbuuid;

    @ManyToOne(fetch = FetchType.LAZY)
    private QnaBoard qnaBoard;
}
