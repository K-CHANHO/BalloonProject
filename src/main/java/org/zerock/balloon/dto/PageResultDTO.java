package org.zerock.balloon.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// Generic 과 ,Function 을 이용해서 확장성 용이하게 구현
// Entity, DTO, 함수를 미리 정의 하지 않고, PageResultDTO를 사용할 때 결정.
@Data
public class PageResultDTO<DTO,EN>{
    private List<DTO> dtoList; //글목록
    private int totalPage; // 전체페이지수. Pageable에서 구해줌.
    private int page; //현재페이지번호. 게시판목록에 페이지번호를 누를 때 구해짐
    private int size; //페이지당 글수. 기본값 10
    private int start, end; // 구간에서 시작페이지번호, 끝페이지번호
    private boolean prev, next; // 이전, 다음 페이지 존재 여부.Flag
    private List<Integer> pageList; // 페이지 구간을 저장하는 list. 1.2.3.4.5.6.7.8.9.10
    public PageResultDTO(Page<EN> result, Function<EN,DTO> fn){
        dtoList=result.stream().map(fn).collect(Collectors.toList());
        totalPage=result.getTotalPages();//전체페이지수
        makePageList(result.getPageable());//페이지구간을 구하는 함수 호출
    }
    private void makePageList(Pageable pageable){
        this.page=pageable.getPageNumber()+1; //현재페이지번호
        this.size=pageable.getPageSize(); // 페이지당글수

        int tempEnd=(int)(Math.ceil(page/10.0))*10; //현재페이지번호가 속한 구간의 끝번호
        start=tempEnd-9; //현재페이지번호가 속한 구간의 시작번호
        prev=start>1; //현재페이지번호가 1보다 커야 prev가 존재(true)
        end=totalPage>tempEnd?tempEnd:totalPage; //계산해서 구해놓은 끝번호(tempEnd)보다 실제 마지막페이지번호가 작으면, end페이지를 실제 마지막페이지로 교체
        next=totalPage>tempEnd; // 계산해서 구해놓은 끝번호(tempEnd)보다 실제 마지막페이지번호가 커야 next가 존재(true)
        pageList= IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList()); // 페이지번호를 start부터 end까지 하나씩 증가시켜서 list에 저장
    }
}
