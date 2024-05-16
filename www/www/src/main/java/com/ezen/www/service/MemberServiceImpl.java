package com.ezen.www.service;

import com.ezen.www.domain.MemberVO;
import com.ezen.www.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public int insert(MemberVO mvo) {
        int isOK = memberMapper.insert(mvo);
        return (isOK > 0 ? memberMapper.insertAuth(mvo.getEmail()) : 0);
    }

    public List<MemberVO> getList(){
        List<MemberVO> list = memberMapper.getList();
        for(MemberVO mvo : list){
            mvo.setAuthList(memberMapper.selectAuths(mvo.getEmail()));
        }
        return list;
    }

    @Override
    public void modify(MemberVO mvo) {
        log.info(">>mvo>>{}",mvo);
        if(mvo.getPwd() == null || mvo.getPwd().length() == 0){
            mvo.setPwd(memberMapper.getPwd(mvo.getEmail()));
        }else{
            String setPwd = passwordEncoder.encode(mvo.getPwd());
            mvo.setPwd(setPwd);
        }
        memberMapper.updateModify(mvo);
    }

    @Override
    public void remove(String email) {
        memberMapper.authRemove(email);
        memberMapper.remove(email);
    }
}
