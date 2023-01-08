package org.zerock.balloon.repository.community.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.balloon.entity.QMember;
import org.zerock.balloon.entity.community.CommunityBoard;
import org.zerock.balloon.entity.community.QCommunityBoard;
import org.zerock.balloon.entity.community.QCommunityBoardGood;
import org.zerock.balloon.entity.community.QCommunityBoardReply;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class CommunitySearchBoardRepositoryImpl extends QuerydslRepositorySupport implements CommunitySearchBoardRepository {

    public CommunitySearchBoardRepositoryImpl() {
        super(CommunityBoard.class);
    }

    @Override
    public Page<Object[]> searchPage(String subject, String type, String keyword, Pageable pageable) {
        log.info("searchPage.............................");

        QCommunityBoard communityBoard = QCommunityBoard.communityBoard;
        QCommunityBoardReply communityBoardReply = QCommunityBoardReply.communityBoardReply;
        QMember member = QMember.member;
        QCommunityBoardGood communityBoardGood = QCommunityBoardGood.communityBoardGood;

        JPQLQuery<CommunityBoard> jpqlQuery = from(communityBoard);
        jpqlQuery.leftJoin(member).on(communityBoard.member.eq(member));
        jpqlQuery.leftJoin(communityBoardReply).on(communityBoardReply.communityBoard.eq(communityBoard));

        //SELECT b, w, count(r) FROM Board b
        //LEFT JOIN b.writer w LEFT JOIN Reply r ON r.board = b
        JPQLQuery<Tuple> tuple = jpqlQuery.select(communityBoard, member, communityBoardReply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = communityBoard.bno.gt(0L);
        booleanBuilder.and(expression);

        // 좋아요 상위 조건을 작성하기


        if(subject != null){
            // subject 조건을 작성하기
            BooleanBuilder conditionBuilder1 = new BooleanBuilder();

            switch (subject){
                case "bike":
                    conditionBuilder1.or(communityBoard.subject.contains("자전거"));
                    break;
                case "road":
                    conditionBuilder1.or(communityBoard.subject.contains("도보"));
                    break;
                case "dog":
                    conditionBuilder1.or(communityBoard.subject.contains("반려동물"));
                    break;
            }

            booleanBuilder.and(conditionBuilder1);
        }

        if(type != null){
            String[] typeArr = type.split("");

            // 검색 조건을 작성하기
            BooleanBuilder conditionBuilder2 = new BooleanBuilder();
            for (String t:typeArr) {
                switch (t){
                    case "t":
                        conditionBuilder2.or(communityBoard.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder2.or(member.nickname.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder2.or(communityBoard.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder2);
        }

        tuple.where(booleanBuilder);

        //order by
        Sort sort = pageable.getSort();

        //tuple.orderBy(board.bno.desc());

        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC: Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(CommunityBoard.class, "communityBoard");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));

        });
        tuple.groupBy(communityBoard);

        //page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        log.info(result);

        long count = tuple.fetchCount();

        log.info("COUNT: " +count);

        return new PageImpl<Object[]>(
                result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable,
                count);
    }


}