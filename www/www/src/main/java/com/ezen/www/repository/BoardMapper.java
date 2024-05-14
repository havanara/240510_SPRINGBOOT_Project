package com.ezen.www.repository;

import java.util.List;
import com.ezen.www.domain.BoardVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {
    int insert(BoardVO bvo);

    List<BoardVO> getList();

    BoardVO getDetail(long bno);

    void modify(BoardVO bvo);

    void remove(long bno);
}
