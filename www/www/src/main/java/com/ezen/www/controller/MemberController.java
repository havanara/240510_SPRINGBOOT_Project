package com.ezen.www.controller;

import com.ezen.www.domain.MemberVO;
import com.ezen.www.repository.MemberMapper;
import com.ezen.www.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member/*")
@Slf4j
public class MemberController {
    private final MemberService msv;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public void join(){
    }

    @PostMapping("/register")
    public String register(MemberVO mvo){
        log.info(">> mvo >> {}", mvo);
        mvo.setPwd(passwordEncoder.encode(mvo.getPwd()));
        int isOK = msv.insert(mvo);
        return "/index";
    }

    @GetMapping("/login")
    public void login(){
    }

    @GetMapping("/list")
    public void list(Model m){
        m.addAttribute("list", msv.getList());
    }

    @GetMapping("/modify")
    public void modify(){
    }

    @PostMapping("/modify")
    public String modify(MemberVO mvo, HttpServletRequest request, HttpServletResponse response){
        msv.modify(mvo);
        logout(request,response);
        return "redirect:/member/logout";
    }

    @GetMapping("/remove")
    public String remove(Principal pr, HttpServletRequest request, HttpServletResponse response){
        String email = pr.getName();
        msv.remove(email);
        logout(request,response);
        return "redirect:/member/login";
    }

    private  void logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, authentication);
    }
}
