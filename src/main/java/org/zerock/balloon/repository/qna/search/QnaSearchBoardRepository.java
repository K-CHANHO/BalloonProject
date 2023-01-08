package org.zerock.balloon.repository.qna.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QnaSearchBoardRepository {
    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}
