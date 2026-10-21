package model.dao;

import java.util.List;

import model.ModelException;
import model.Comment;

public interface CommentDAO {
	boolean save(Comment comment) throws ModelException;
	boolean update(Comment comment) throws ModelException;
	boolean delete(Comment comment) throws ModelException;
	List<Comment> listAll() throws ModelException;
	Comment findById(int id) throws ModelException;
	Comment findByComment(String comment) throws ModelException;
}