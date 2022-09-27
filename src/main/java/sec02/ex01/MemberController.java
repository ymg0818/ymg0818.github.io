package sec02.ex01;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/*")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    MemberDAO memberDAO;
    
	public void init() throws ServletException {
		memberDAO = new MemberDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		String nextPage = null;
		String action = request.getPathInfo(); //URL에서 요청명을 가져옴
		System.out.println("action : "+action);
		
		if(action == null || action.equals("/listMembers.do")) {
			List<MemberVO> membersList = memberDAO.listMembers();
			request.setAttribute("membersList", membersList);
			nextPage = "/test02/listMembers.jsp"; //test02 폴더의 listMembers.jsp로 포워딩함
		}else if(action.equals("/addMember.do")) { //회원 정보를 가져와서 테이블에 추가
			String id = request.getParameter("id");
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			MemberVO memberVO = new MemberVO(id, pwd, name, email);
			memberDAO.addMember(memberVO);
			nextPage = "/member/listMembers.do"; //회원 등록 후 다시 회원 목록을 출력함
		}else if(action.equals("/memberForm.do")) { //회원 가입창을 화면에 출력
			nextPage = "/test02/memberForm.jsp";
		}else if(action.equals("/modMemberForm.do")){ //회원 수정창 요청 시 ID로 회원 정보를 조회한 후 수정창으로 포워딩
			String id = request.getParameter("id");
			MemberVO memInfo = memberDAO.findMember(id); //회원 정보 수정창을 요청하면서 전송된 ID를 이용해 수정 전 회원 정보를 조회
			request.setAttribute("memInfo", memInfo); //request에 바인딩하여 회원 정보 수정창에 수정하기 전 회원 정보를 전달함
			nextPage = "/test02/modMemberForm.jsp";
		}else if(action.equals("/modMember.do")) { //테이블의 회원 정보를 수정함
			String id = request.getParameter("id"); //수정창에서 전송된 수정 회원 정보를 가져온 후
			String pwd = request.getParameter("pwd");
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			MemberVO memberVO = new MemberVO(id, pwd, name, email); //MemberVO 객체 속성에 설정
			memberDAO.modMember(memberVO);
			request.setAttribute("msg", "modified"); //회원 목록창으로 수정 작업 완료 메시지를 전달
			nextPage="/member/listMembers.do";
		}else if(action.equals("/delMember.do")) { //회원 ID를 SQL문으로 전달해 테이블의 회원 정보를 삭제
			String id = request.getParameter("id"); //삭제할 회원 ID를 받아옴
			memberDAO.delMember(id); 
			request.setAttribute("msg", "deleted"); //회원 목록창으로 삭제 작업 완료 메시지를 전달
			nextPage = "/member/listMembers.do";
		}else {
			List<MemberVO> membersList = memberDAO.listMembers();
			request.setAttribute("membersList", membersList);
			nextPage="/test02/listMembers.jsp";
		}
		RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
		dispatch.forward(request, response);
	}
}
