<%-- 科目情報変更JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="/common/base.jsp" >
	<c:param name="title">
		科目管理システム
	</c:param>

	<c:param name="scripts"></c:param>

	<c:param name="content">
		<section>
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">科目情報削除</h2>
			<form action="StudentUpdateExecute.action" method="get">
			<div>
			    <p>${subject_name}(${subject_cd})削除してもよろしいでしょうか</p>
			    <input type="hidden" name="subject_name"
			    		value=${ent_cd}>
			<div>
			    <label class="mx-auto py-2" for="ent_name">科目名</label><br>
			    <input class="border border-0 ps-3" type="text" id="ent_name" name="ent_name"
			           value="${ent_name}" required placeholder="科目名を入力してください" required maxlength="10" />
			</div>
				<div class="mx-auto py-2">
					<input class="btn btn-primary" type="submit" name="login" value="変更"/>
				</div>
			</form>
			<a href="SubjectList.action">戻る</a>
		</section>
	</c:param>
</c:import>