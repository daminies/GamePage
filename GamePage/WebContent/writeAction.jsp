<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="gamepage.GamepageDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8");%>
<jsp:useBean id="gamepage" class="gamepage.Gamepage" scope="page" />
<jsp:setProperty name="gamepage" property="gamepageTitle" />
<jsp:setProperty name="gamepage" property="gamepageContent" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>GAIN</title>
</head>
<body>
	<%
	
	String userID = null;
	if(session.getAttribute("userID") != null){
		userID = (String) session.getAttribute("userID");
	}
	if(userID == null){
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('로그인을 하세요.')");
		script.println("location.href='Login.jsp'");
		script.println("</script>");
	} else {
		
		if (gamepage.getGamepageTitle() == null || gamepage.getGamepageContent() == null){
			PrintWriter script = response.getWriter();
			script.println("<script>");
			script.println("alert('입력이 안 된 사항이 있습니다.')");
			script.println("history.back()");
			script.println("</script>");		
		} else {
			GamepageDAO gamepageDAO = new GamepageDAO();
			int result = gamepageDAO.write(gamepage.getGamepageTitle(), userID, gamepage.getGamepageContent());
			if (result == -1){
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("alert('글쓰기에 실패했습니다.')");
				script.println("history.back()");
				script.println("</script>");
			} 		
			else {
				PrintWriter script = response.getWriter();
				script.println("<script>");
				script.println("location.href = 'gamepage.jsp'");
				script.println("</script>");
			}
		}
		}


	%>


</body>
</html>