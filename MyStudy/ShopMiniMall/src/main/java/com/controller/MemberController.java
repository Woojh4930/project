package com.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dto.goods.GoodsDTO;
import com.dto.member.MemberDTO;
import com.service.goods.GoodsService;
import com.service.member.MemberService;

@Controller
public class MemberController {

	@Autowired
	MemberService memberService;
	
	@RequestMapping(value = "/memberUI", method = RequestMethod.GET)
	public String memberUI() throws Exception {
		return "memberForm";
	}
	
	@PostMapping(path = "/memberAdd")
	public String memberAdd(MemberDTO dto) throws Exception {
		
		int num = memberService.memberAdd(dto);
		return "member/memberAddSuccess";
	}
	
	
	@RequestMapping(value = "/memberIdCheck", method = RequestMethod.GET, produces = "application/text; charset=UTF-8")
	public @ResponseBody String memberIdCheck(@RequestParam String userid) throws Exception{
		MemberDTO dto = memberService.idDuplicateCheck(userid);
		String mesg = "";
		if(dto==null) {
			mesg = "아이디 사용 가능";
		}else{
			mesg = "아이디 사용 불가능";
		}
		
		return mesg;
	}
	
	@GetMapping(path = "/loginUI")
	public String loginUI() throws Exception {
		
			return "loginForm";
	}
	
	@PostMapping(path = "/login")
	public String login(@RequestParam HashMap<String, String> map, HttpSession session) throws Exception {
		MemberDTO dto = memberService.login(map);
		
		if(dto==null) {
			return "member/loginFail";
		}else {
			
		session.setAttribute("login", dto);
		return "redirect:main";
		}
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "member/logoutSuccess";
	}
	//////////////////////
	@GetMapping("/mypage")
	public String mypage() throws Exception {
		
		return "mypage";
	}
	
	@PostMapping("/memberUpdate")
	public String memberUpdate(MemberDTO dto,HttpSession session) throws Exception {
		memberService.memberUpdate(dto);
		
		session.setAttribute("login", dto);
		return "redirect:mypage";
	}
	//////////////////////
	
	
	@ExceptionHandler({Exception.class})
	public String errorPage() {
		return "error/error";
	}
	
}
