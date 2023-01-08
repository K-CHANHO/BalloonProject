package org.zerock.balloon.entity.qna;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.zerock.balloon.entity.BaseEntity;
import org.zerock.balloon.entity.Member;

import javax.persistence.*;

@DynamicInsert
@Entity
@Table(name="qna_board")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "member")
public class QnaBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qbno; //글번호

//    private String subject; => 추천, 산책로 게시판에 추가

    private String title; //제목

    @Column(columnDefinition = "LONGTEXT")
    private String content; //콘텐츠, 게시글
    @ColumnDefault("0")
    private Long hits; //조회수

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void changeTitle(String title){
        this.title=title;
    }
    public void changeContent(String content){
        this.content=content;
    }
}
