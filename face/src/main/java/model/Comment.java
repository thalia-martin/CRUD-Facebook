package model;

public class Comment {
	private int id;
	private int userId;
	private int postId;
	private String comment;
	
	// Atributos para exibição na tabela (via JOIN)
	private String userName;
	private String postContent;
	
	public Comment() {
		this(0);
	}
	
	public Comment(int id) {
		this.id = id;
		setUserId(0);
		setPostId(0);
		setComment("");
		setUserName("");
		setPostContent("");
	}

	public int getId() { 
		return id; 
		}
	
	public void setId(int id) { 
		this.id = id; 
		}
	
	public int getUserId() { 
		return userId; 
		
	}
	public void setUserId(int userId) { 
		this.userId = userId; 
		}
	
	public int getPostId() { 
		return postId; 
		}
	
	public void setPostId(int postId) { 
		this.postId = postId; 
		}
	
	public String getComment() { 
		return comment; 
		}
	
	public void setComment(String comment) { 
		this.comment = comment; 
		}
	
	public String getUserName() { 
		return userName; 
		}
	
	public void setUserName(String userName) { 
		this.userName = userName; 
		}
	
	public String getPostContent() { 
		return postContent; 
		}
	
	public void setPostContent(String postContent) { 
		this.postContent = postContent; 
		}
}