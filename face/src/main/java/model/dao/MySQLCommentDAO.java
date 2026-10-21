package model.dao;

import java.util.ArrayList;
import java.util.List;

import model.ModelException;
import model.Comment;

public class MySQLCommentDAO implements CommentDAO {

	@Override
	public boolean save(Comment comment) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO comments (id, comment, user_id, post_id) VALUES (DEFAULT, ?, ?, ?);";
		
		db.prepareStatement(sqlInsert);
		db.setString(1, comment.getComment());
		db.setInt(2, comment.getUserId());
		db.setInt(3, comment.getPostId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean update(Comment comment) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlUpdate = "UPDATE comments "
				+ "SET comment = ?, "
				+ "user_id = ?, "
				+ "post_id = ? "
				+ "WHERE id = ?";

		db.prepareStatement(sqlUpdate);
		
		db.setString(1, comment.getComment());
		db.setInt(2, comment.getUserId());
		db.setInt(3, comment.getPostId());
		db.setInt(4, comment.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Comment comment) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlDelete = "DELETE FROM comments WHERE id = ?;";

		db.prepareStatement(sqlDelete);		
		db.setInt(1, comment.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public List<Comment> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		
		List<Comment> comments = new ArrayList<Comment>();
			
		String sqlQuery = "SELECT c.id, c.comment, c.user_id, c.post_id, " +
		                  "u.nome AS user_name, p.content AS post_content " +
		                  "FROM comments c " +
		                  "JOIN users u ON c.user_id = u.id " +
		                  "JOIN posts p ON c.post_id = p.id " +
		                  "ORDER BY c.id";
		
		db.createStatement();
		db.executeQuery(sqlQuery);

		while (db.next()) {
			Comment c = createCommentWithJoin(db);
			comments.add(c);
		}
		
		return comments;
	}

	@Override
	public Comment findById(int id) throws ModelException {
		DBHandler db = new DBHandler();
				
		String sql = "SELECT c.id, c.comment, c.user_id, c.post_id, " +
		             "u.nome AS user_name, p.content AS post_content " +
		             "FROM comments c " +
		             "JOIN users u ON c.user_id = u.id " +
		             "JOIN posts p ON c.post_id = p.id " +
		             "WHERE c.id = ?";
		
		db.prepareStatement(sql);
		db.setInt(1, id);
		db.executeQuery();
		
		Comment c = null;
		while (db.next()) {
			c = createCommentWithJoin(db);
			break;
		}
		
		return c;
	}
	
	private Comment createCommentWithJoin(DBHandler db) throws ModelException {
		Comment c = new Comment(db.getInt("id"));
		c.setComment(db.getString("comment"));
		c.setUserId(db.getInt("user_id"));
		c.setPostId(db.getInt("post_id"));
		
		try {
			c.setUserName(db.getString("user_name"));
		} catch (Exception e) {
			c.setUserName("");
		}
		
		try {
			c.setPostContent(db.getString("post_content"));
		} catch (Exception e) {
			c.setPostContent("");
		}
		
		return c;
	}
	
	@Override
	public Comment findByComment(String comment) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sql = "SELECT * FROM comments WHERE comment = ?";
		
		db.prepareStatement(sql);
		db.setString(1, comment);
		db.executeQuery();
		
		Comment c = null;
		while (db.next()) {
			c = new Comment(db.getInt("id"));
			c.setComment(db.getString("comment"));
			c.setUserId(db.getInt("user_id"));
			c.setPostId(db.getInt("post_id"));
			break;
		}
		
		return c;
	}
}