package sec03.brd01;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@WebServlet("/board/*")
public class BoardController extends HttpServlet{
	BoardService boardService;
	ArticleVO articleVO;
	
	public void init() throws ServletException{
		boardService = new BoardService();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doHandle(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doHandle(request, response);
	}
	
	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");
		String action = request.getPathInfo(); //요청명을 가져옴
		System.out.println("action : "+action);
		String nextPage ="";
		try {
			List<ArticleVO> articlesList = new ArrayList<ArticleVO>();
			if(action == null) {
				articlesList = boardService.listArticles();
				request.setAttribute("articlesList", articlesList);
				nextPage = "/board01/listArticles.jsp";
			}else if(action.equals("/listArticles.do")) { //action 값이 /listArticles.do이면 전체 글을 조회
				articlesList = boardService.listArticles();
				request.setAttribute("articlesList", articlesList); //조회된 글 목록을 articlesList로 바인딩한 후 listArticles.jsp로 포워딩
				nextPage = "/board01/listArticles.jsp";
			}
			RequestDispatcher dispatch = request.getRequestDispatcher(nextPage);
			dispatch.forward(request, response);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
