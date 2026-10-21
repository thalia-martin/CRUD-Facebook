package controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ModelException;
import model.Comment;
import model.User;
import model.Post; 
import model.dao.DAOFactory;
import model.dao.CommentDAO;
import model.dao.UserDAO;
import model.dao.PostDAO; 

@WebServlet(urlPatterns = {"/comments", 
                           "/comments/save", 
                           "/comments/update",
                           "/comments/delete",
                           "/comments/form"})
public class CommentController extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		String action = req.getRequestURI();
		System.out.println("URL: " + action);
		
		switch (action) {
		case "/face/comments": {
			loadComments(req);
			
			RequestDispatcher rd = req.getRequestDispatcher("/comments.jsp");
			rd.forward(req, resp);
			break;
		}
		case "/face/comments/form": {
			loadFormData(req);
			
			RequestDispatcher rd = req.getRequestDispatcher("/comments_form.jsp");
			rd.forward(req, resp);
			break;
		}
		case "/face/comments/save": {
			saveOrUpdateComment(req);
			
			resp.sendRedirect("/face/comments");
			break;
		}
		case "/face/comments/update": {
			loadComment(req);
			loadFormData(req); 
			
			RequestDispatcher rd = req.getRequestDispatcher("/comments_form.jsp");
			rd.forward(req, resp);
			break;
		}
		
		case "/face/comments/delete": {
			deleteComment(req);
			resp.sendRedirect("/face/comments");
			break;
		}
		default:
			String message = String.format("URL não suportada: '%s'.", action);
			
			System.out.println(message);
			throw new IllegalArgumentException(message);
		}
	}
	
	private void loadFormData(HttpServletRequest req) {
		UserDAO userDAO = DAOFactory.createDAO(UserDAO.class);
		PostDAO postDAO = DAOFactory.createDAO(PostDAO.class);
		
		try {
			List<User> users = userDAO.listAll();
			List<Post> posts = postDAO.listAll();
			
			req.setAttribute("users_list", users);
			req.setAttribute("posts_list", posts);
		} catch (ModelException e) {
			e.printStackTrace();
		}
	}
	
	private void deleteComment(HttpServletRequest req) {
		String commentIdStr = req.getParameter("commentId");
		
		int commentId = Integer.parseInt(commentIdStr);
		Comment commentToBeDeleted = new Comment(commentId);
		
		CommentDAO dao = DAOFactory.createDAO(CommentDAO.class);
		try {
			dao.delete(commentToBeDeleted);
		} catch (ModelException e) {

			e.printStackTrace();
		}
	}
	
	private void loadComment(HttpServletRequest req) {
		String commentIdStr = req.getParameter("commentId");
		int commentId = Integer.parseInt(commentIdStr);
	
		CommentDAO dao = DAOFactory.createDAO(CommentDAO.class);
		
		Comment commentToBeUpdated = null;
		try {
			commentToBeUpdated = dao.findById(commentId);
		} catch (ModelException e) {

			e.printStackTrace();
		}
		
		req.setAttribute("comment", commentToBeUpdated);
	}
	
	private void saveOrUpdateComment(HttpServletRequest req) {
	    String commentIdStr = req.getParameter("comment_id");
	    boolean isUpdate = false;
	    
	    Comment comment = null;
	    if ((commentIdStr == null) || commentIdStr.isBlank())
	        comment = new Comment();
	    else { 
	        comment = new Comment(Integer.parseInt(commentIdStr));
	        isUpdate = true;
	    }
	    
	    int userId = Integer.parseInt(req.getParameter("user_id"));
	    int postId = Integer.parseInt(req.getParameter("post_id"));
	    String commentText = req.getParameter("comment_text");
	    
	    comment.setUserId(userId);
	    comment.setPostId(postId);
	    comment.setComment(commentText);
	    
	    CommentDAO dao = DAOFactory.createDAO(CommentDAO.class);
	    try {
	        if (isUpdate) dao.update(comment);
	        else dao.save(comment);
	    } catch (ModelException e) {
	        e.printStackTrace();
	    }
	}
	
	private void loadComments(HttpServletRequest req) {
		CommentDAO dao = DAOFactory.createDAO(CommentDAO.class);
		
		List<Comment> comments = List.of();
		try {
			comments = dao.listAll();
		} catch (ModelException me) {
			me.printStackTrace();
		}
		
		req.setAttribute("comment_list", comments);
	}
}