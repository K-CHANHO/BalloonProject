package org.zerock.balloon.repository.qna.search;

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
import org.zerock.balloon.entity.qna.QQnaBoard;
import org.zerock.balloon.entity.qna.QQnaReply;
import org.zerock.balloon.entity.qna.QnaBoard;


import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class QnaSearchBoardRepositoryImpl extends QuerydslRepositorySupport implements QnaSearchBoardRepository {

    public QnaSearchBoardRepositoryImpl() {
        super(QnaBoard.class);
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("searchPage.............................");

        QQnaBoard qnaBoard = QQnaBoard.qnaBoard;
        QQnaReply qnaReply = QQnaReply.qnaReply;
        QMember member = QMember.member;

        JPQLQuery<QnaBoard> jpqlQuery = from(qnaBoard);
        jpqlQuery.leftJoin(member).on(qnaBoard.member.eq(member));
        jpqlQuery.leftJoin(qnaReply).on(qnaReply.qnaBoard.eq(qnaBoard));

        //SELECT b, w, count(r) FROM Board b
        //LEFT JOIN b.writer w LEFT JOIN Reply r ON r.board = b
        JPQLQuery<Tuple> tuple = jpqlQuery.select(qnaBoard, member, qnaReply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = qnaBoard.qbno.gt(0L);

        booleanBuilder.and(expression);

        if(type != null){
            String[] typeArr = type.split("");
            //검색 조건을 작성하기
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t:typeArr) {
                switch (t){
                    case "t":
                        conditionBuilder.or(qnaBoard.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.nickname.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(qnaBoard.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        //order by
        Sort sort = pageable.getSort();

        //tuple.orderBy(board.bno.desc());

        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC: Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(QnaBoard.class, "qnaBoard");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));

        });
        tuple.groupBy(qnaBoard);

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
