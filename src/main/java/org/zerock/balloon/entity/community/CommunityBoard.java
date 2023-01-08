package org.zerock.balloon.entity.community;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.zerock.balloon.entity.BaseEntity;
import org.zerock.balloon.entity.Member;

import javax.persistence.*;
import java.sql.Clob;

@DynamicInsert
@Entity
@Table(name="community_board")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "member")
public class CommunityBoard extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno; //글번호

    private String subject; // 말머리

    private String title; //제목

    @Column(columnDefinition = "LONGTEXT")
    private String content; //콘텐츠, 게시글

    @ColumnDefault("0")
    private Long hits; //조회수

    @ColumnDefault("0")
    private Long good; // 좋아요 수

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public void changeTitle(String title){
        this.title=title;
    }
    public void changeContent(String content){
        this.content=content;
    }
    public void changeSubject(String subject){
        this.subject = subject;}
}
