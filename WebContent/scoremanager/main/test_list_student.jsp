<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>得点管理システム</h1>

<p><strong>${loginUser.name}</strong> 様　<a href="Logout.action">ログアウト</a></p>

<h2>成績一覧（科目）</h2>

<!-- 検索フォーム -->
<form action="TestList.action" method="get">
  <fieldset>
    <legend>科目情報</legend>
    入学年度:
    <select name="f1">
      <c:forEach var="year" items="${entYearSet}">
        <option value="${year}" <c:if test="${year == f1}">selected</c:if>>${year}</option>
      </c:forEach>
    </select>

    クラス:
    <select name="f2">
      <c:forEach var="classNum" items="${cNumList}">
        <option value="${classNum}" <c:if test="${classNum == f2}">selected</c:if>>${classNum}</option>
      </c:forEach>
    </select>

    科目:
    <select name="f3">
      <c:forEach var="subject" items="${list}">
        <option value="${subject.cd}" <c:if test="${subject.cd == f3}">selected</c:if>>
          ${subject.name}
        </option>
      </c:forEach>
    </select>

    <input type="submit" value="検索">
  </fieldset>

  <fieldset>
    <legend>学生情報</legend>
    学生番号:
    <input type="text" name="f4" value="${f4}" placeholder="学生番号を入力してください">
    <input type="submit" value="検索">
  </fieldset>
</form>

<!-- 選択された科目名表示 -->
<p>氏名：${student.name}(${f4})</p>

<!-- 成績一覧テーブル -->
<c:if test="${not empty testlist}">
  <table border="1">
    <thead>
      <tr>
        <th>入学年度</th>
        <th>クラス</th>
        <th>学生番号</th>
        <th>氏名</th>
        <th>1回</th>
        <th>2回</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="student" items="${testlist}">
        <tr>
          <td><c:out value="${test.entYear}" /></td>
          <td><c:out value="${test.classNum}" /></td>
          <td><c:out value="${test.studentNo}" /></td>
          <td><c:out value="${test.studentName}" /></td>
          <td><c:out value="${test.point[1]}" /></td>
          <td><c:out value="${test.point[2]}" /></td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
</c:if>

