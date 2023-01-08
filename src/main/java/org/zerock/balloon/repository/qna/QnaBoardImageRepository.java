package org.zerock.balloon.repository.qna;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.balloon.entity.qna.QnaBoardImage;

public interface QnaBoardImageRepository extends JpaRepository<QnaBoardImage, Long> {
}
