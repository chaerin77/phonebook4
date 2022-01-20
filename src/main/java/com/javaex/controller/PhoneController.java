package com.javaex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaex.dao.PhoneDao;
import com.javaex.vo.PersonVo;

@Controller
@RequestMapping(value="/phone")
public class PhoneController {

	//필드
	@Autowired
	private PhoneDao phoneDao; 
	
	//생성자
	
	//메소드 g/s
	
	//메소드 일반
	
	/////요청하면 list   주소창에 list 치면
	@RequestMapping(value="/list", method= {RequestMethod.GET, RequestMethod.POST})
	public String list(Model model) {
		System.out.println("PhoneController>list()");
		
		//다오에서 리스트를 가져온다
		List<PersonVo> personList = phoneDao.getPersonList();
		System.out.println(personList.toString());

		//컨트롤러가 DS한테 데이터를 보내야 함.(model)
		model.addAttribute("personList", personList);  //"" 안이 별명
		
		//jsp정보를 리턴한다(view)
		return "list";
	}
	
	
	
	// /phone/writeForm
	@RequestMapping(value="/writeForm", method= {RequestMethod.GET, RequestMethod.POST})//주소 미리 준비
	public String writeForm() {
		System.out.println("PhoneController>writeForm()");
		
		
		return "writeForm";
	}
	
	
	
	//ds에서 파라미터 값을 꺼내 정보를 담은 주소와 같은 이름을 써주면됨
	@RequestMapping(value="/write", method= {RequestMethod.GET, RequestMethod.POST})
	public String write(@ModelAttribute PersonVo personVo) {
		System.out.println("PhoneController>write()");
		
		//저장 
		phoneDao.personInsert(personVo);
		
		return "redirect:/phone/list";
	}
	
	/*섞어쓰기 가능
	@RequestMapping(value="/write", method= {RequestMethod.GET, RequestMethod.POST})
	public String write(@ModelAttribute PersonVo personVo,
				        @RequestParam("company") String company) {
		
		System.out.println("PhoneController>write()");
		
		System.out.println(personVo);
		System.out.println(company);
		
		//저장
		return "";
	}*/

	
	//phonebook 수정, 삭제 만들기

	/*
	//수정폼
	@RequestMapping(value="/updateForm", method= {RequestMethod.GET, RequestMethod.POST})
	public String updateForm(Model model, @RequestParam("no") int no) { //콤마!
		
		PersonVo pvo = phoneDao.getPerson(no);
		
		model.addAttribute("pVo", pvo);
		
		return "updateForm";
	}
	*/
	
	/*
	//수정
	@RequestMapping(value="/update", method= {RequestMethod.GET, RequestMethod.POST})
	public String update(@ModelAttribute PersonVo personVo) {
		
		int i = phoneDao.personUpdate(personVo);
		System.out.println(i);
		
		return "redirect:/phone/list"; //리다이렉트는 뷰 리졸브 필요없음 jsp파일 찾을필요 없으니까 
	}
	*/
	
	
	//삭제
	@RequestMapping(value="/delete", method= {RequestMethod.GET, RequestMethod.POST})//handler mapping이되도록 주소체계 갖고있어야
	public String delete(@RequestParam("personId") int personId) {
		System.out.println("PhoneController>delete()");
		
		phoneDao.personDelete(personId);
		
		return "redirect:/phone/list";
	}
	
	/*
	//넘겨줄 값이 아예 없는 경우
	@RequestMapping(value="/test", method = {RequestMethod.GET, RequestMethod.POST})
	public String test(@RequestParam(value="name") String name,
					   @RequestParam(value="age", required =false, defaultValue = "-1") int age ) {	
			//http://localhost:8088/phonebook3/phone/test?name=dd
			System.out.println(name);
			System.out.println(age);
		
		return "writeForm";
	}

	//파라미터 값으로 가져오기
	@RequestMapping(value="/view", method = {RequestMethod.GET, RequestMethod.POST})
	public String view(@RequestParam(value="no") int no) {
		System.out.println("@RequestParam");
		System.out.println(no + "번글 가져오기");
		
		return "writeForm";
	}
	
	
	@RequestMapping(value="/view/12", method = {RequestMethod.GET, RequestMethod.POST})
	public String view12() {
	
		System.out.println("12번글 가져오기");
		
		return "writeForm";
	}
	
	//위랑 기술적으로 다름 주소를 변수로
	@RequestMapping(value="/view/{no}", method = {RequestMethod.GET, RequestMethod.POST})
	public String view11(@PathVariable("no") int no) {
		System.out.println("PathVariable");
		System.out.println(no + "번글 가져오기");
		
		return "writeForm";
	}
	
	//회원가입시 이런 방식을 쓰면 안됨 - 비번 노출
	@RequestMapping(value="/{no}/{num}/view", method = {RequestMethod.GET, RequestMethod.POST})
	public String view13(@PathVariable("no") int no, @PathVariable("num") int num) {
		System.out.println("PathVariable");
		System.out.println(no + "번글 가져오기");
		
		return "writeForm";
	}
	
	
	//aa라는 회원의 블로그글 가져오기   
	//localhost:8088/phonebook3/phone/aaa (PathVariable 방식) --주소창에 /본인아이디 치면 
	//localhost:8088/phonebook3/phone?id=aaa (기존 파라미터 방식)
	@RequestMapping(value="/{id}", method = {RequestMethod.GET, RequestMethod.POST})
	public String blog(@PathVariable("id") String id) {
		System.out.println("PathVariable");
		System.out.println( id+"의 블로그입니다.");
		
		return "writeForm";
	}*/
}
