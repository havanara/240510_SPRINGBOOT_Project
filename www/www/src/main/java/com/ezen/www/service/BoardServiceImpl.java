package com.ezen.www.service;

import com.ezen.www.domain.BoardDTO;
import com.ezen.www.domain.FileVO;
import com.ezen.www.domain.PagingVO;
import com.ezen.www.repository.BoardMapper;
import com.ezen.www.domain.BoardVO;
import com.ezen.www.repository.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService{
    private final BoardMapper boardMapper;
    private final FileMapper fileMapper;

//    @Override
//    public int register(BoardVO bvo) {
//        return boardMapper.insert(bvo);
//    }

    @Transactional
    @Override
    public int register(BoardDTO bdto) {
        int isOK = boardMapper.insert(bdto.getBvo());
        if(isOK > 0 && bdto.getFlist().size() > 0){
            long bno = boardMapper.getBno();
            for(FileVO fvo : bdto.getFlist()){
                fvo.setBno(bno);
                isOK *= fileMapper.insertFile(fvo);
            }
        }
        return isOK;
    }

//    @Override
//    public List<BoardVO> getList() {
//        return boardMapper.getList();
//    }

    @Override
    public List<BoardVO> getList(PagingVO pgvo) {
        return boardMapper.getList(pgvo);
    }

//    @Override
//    public BoardVO getDetail(long bno) {
//        return boardMapper.getDetail(bno);
//    }
    @Override
    public BoardDTO getDetail(long bno) {
        BoardDTO bdto = new BoardDTO(
                boardMapper.getDetail(bno),
                fileMapper.getFileList(bno));
        return bdto;
    }

    @Override
    public void modify(BoardVO bvo) {
        boardMapper.modify(bvo);
    }

    @Override
    public void remove(long bno) {
        boardMapper.remove(bno);
    }

    @Override
    public int getTotalCount(PagingVO pgvo) {
        return boardMapper.getTotalCount(pgvo);
    }

}