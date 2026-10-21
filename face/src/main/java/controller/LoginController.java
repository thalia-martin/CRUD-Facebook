package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ModelException;
import model.User;
import model.dao.DAOFactory;
import model.dao.UserDAO;
import model.utils.PasswordEncryptor;

@WebServlet(urlPatterns = {"/login", "/logout"})
public class LoginController extends HttpServlet {

	
	@Override
	protected void doGet(HttpServletRequest req, 
			HttpServletResponse resp) throws ServletException, IOException {
		
		HttpSession session = req.getSession(false);
		if (session != null) session.invalidate();
		
		resp.sendRedirect("/face/login.jsp");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String userEmail = req.getParameter("user_login");
		String userPW = req.getParameter("user_pw");

		if (userEmail.isBlank() || userPW.isBlank()) {
			resp.sendRedirect("/face/login.jsp?erro=true");
		
		return;
		}
		
		UserDAO dao = DAOFactory.createDAO(UserDAO.class);
		User userToLogin = null;
		try {
			userToLogin = dao.findByEmail(userEmail);
			System.out.println("Utilizador encontrado: " + (userToLogin != null ? userToLogin.getEmail() : "NULO"));

		} catch (ModelException e) {
			e.printStackTrace();
		}
		
		if (userToLogin == null) {
			resp.sendRedirect("/face/login.jsp?erro=true");
			return;
		}
		

		if(PasswordEncryptor.checkPassword(userPW, userToLogin.getPasswordHash()) == true) {
			req.getSession().setAttribute("usuer_logged", userToLogin);
			resp.sendRedirect("/face/");
			return;			
		} else {
			resp.sendRedirect("/face/login.jsp?erro=true");
			return;
		}
		
	}
}
