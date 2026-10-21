<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Gestão de Comentários</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-icons.css">
</head>
<body>
	<div class="container">
		<div class="row mt-3">
			<div class="col-md-2"></div>
			
			<div class="col-md-8">
				<h1>Novo Comentário</h1>
			
				<form action="${pageContext.request.contextPath}/comments/save" method="GET">
				      
				      <input type="hidden" id="comment_id" name="comment_id" value="${comment.getId()}">
				      
				      <input type="hidden" name="user_id" value="1"> 
				      
				      <div class="mb-3">
				      	<label for="comments_comment_id" class="form-label">Comentário</label>
				      	<input id="comments_comment_id" type="text" name="comment_text" class="form-control" value="${comment.getComment()}">	
				      </div>
				      
				      <div class="mb-3">
				      	<label for="comment_post_id" class="form-label">Selecione o Post</label>
				      	<select id="comment_post_id" name="post_id" class="form-select">
				      		<option value="">Selecione um post</option>
				      		<c:forEach var="p" items="${posts_list}">
				      			<option value="${p.getId()}" 
				      				${comment.getPostId() == p.getId() ? 'selected' : ''}>
				      				${p.getContent()}
				      			</option>
				      		</c:forEach>
				      	</select>
				      </div>
				      
					  <button type="submit" class="btn btn-primary">Salvar Comentário</button>
				</form>
			</div>
			<div class="col-md-2"></div>
		</div>
	</div>
</body>
</html>