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
import model.Post;
import model.User;
import model.dao.DAOFactory;
import model.dao.PostDAO;

@WebServlet(urlPatterns = {"/posts",
		"/posts/save",
		"/posts/update",
		"/posts/delete"})
public class PostsController extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		String action = req.getRequestURI();
		System.out.println("URL: " + action);
		
		switch (action) {
		case "/face/posts": {
			loadPosts(req);
			
			RequestDispatcher rd = req.getRequestDispatcher("/posts.jsp");
			rd.forward(req, resp);
			break;
		}
		case "/face/posts/save": {
			saveOrUpdatePost(req);
			
			resp.sendRedirect("/face/posts");
			break;
		}
		case "/face/posts/update": {
			loadPost(req);
			
			RequestDispatcher rd = req.getRequestDispatcher("/posts_form.jsp");
			rd.forward(req, resp);
			break;
		}
		
		case "/face/posts/delete": {
			deletePost(req);
			resp.sendRedirect("/face/posts");
			break;
		}
		default:
			String message = String.format("URL não suportada: '%s'.", action);
			
			System.out.println(message);
			throw new IllegalArgumentException(message);
		}
	}
	
	private void deletePost(HttpServletRequest req) {
		String postIdStr = req.getParameter("postsId");
		 
		int postId  = Integer.parseInt(postIdStr);
		Post postToBeDeleted = new Post(postId);
		
		PostDAO dao = DAOFactory.createDAO(PostDAO.class);
		try {
			dao.delete(postToBeDeleted);
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
	}
	
	
	
	private void loadPost(HttpServletRequest req) {
		String postIdStr = req.getParameter("postsId");
		int postId = Integer.parseInt(postIdStr);
	
		PostDAO dao = DAOFactory.createDAO(PostDAO.class);
		
		Post postToBeUpdated = null;
		try {
			postToBeUpdated = dao.findById(postId);
		} catch (ModelException e) {
			// Log no servidor
			e.printStackTrace();
		}
		
		req.setAttribute("post", postToBeUpdated);
	}
	
	private void saveOrUpdatePost(HttpServletRequest req) {
		String postIdStr = req.getParameter("post_id");
		boolean isUpdate = false;
		
		Post post = null;
		if ((postIdStr == null) || postIdStr.isBlank())
			post = new Post();
		else { 
			post = new Post(Integer.parseInt(postIdStr));
			isUpdate = true;
		}
		
		String postContent = req.getParameter("post_content");
		System.out.println("valor" + req.getParameter("post_content"));
		
		post.setContent(postContent);
		
		User user = (User) req.getSession().getAttribute("usuer_logged");
		System.out.println("usndo" + user);
		post.setUser(user);
		
		PostDAO dao = DAOFactory.createDAO(PostDAO.class);
		
		try {
			if (isUpdate) dao.update(post);
			else dao.save(post);
		} catch (ModelException e) {
			e.printStackTrace();
		}
	}
	
	private void loadPosts(HttpServletRequest req) {
		PostDAO dao = DAOFactory.createDAO(PostDAO.class);
		
		List<Post> posts = List.of();
		try {
			posts = dao.listAll();
		} catch (ModelException me) {
			me.printStackTrace();
		}
		
		req.setAttribute("posts_list", posts);
	}
}
