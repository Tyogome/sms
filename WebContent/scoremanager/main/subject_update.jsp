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
			<h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">科目情報変更</h2>
			<form action="SubjectUpdateExecute.action" method="get">
				<div>
				    <label class="mx-auto py-2" for="ent_cd">科目コード</label><br>
				    <input class="border border-0 ps-3" type="text" id="ent_cd" name="cd"
				    		value="${cd}"required placeholder="科目コードを入力してください" readonly />
				</div>

					<div class="mt-2 text-warning">${errors.get("1") }</div>
				<div>
				    <label class="mx-auto py-2" for="ent_name">科目名</label><br>
				    <input class="border border-0 ps-3" type="text" id="ent_name" name="name"
				           value="${name}" required placeholder="科目名を入力してください" required maxlength="20" />
				</div>
					<div class="mx-auto py-2">
						<input class="btn btn-primary" type="submit" name="login" value="変更"/>
					</div>
			</form>
			<a href="SubjectList.action">戻る</a>
		</section>
	</c:param>
</c:import>