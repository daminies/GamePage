package gamepage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GamepageDAO {

	private Connection conn;
	private ResultSet rs;

	public GamepageDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/GamePage?serverTimezone=UTC";
			String dbID = "root";
			String dbPassword = "1362pkw";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getDate() {
		String SQL = "SELECT NOW()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return ""; // 데이터베이스 오류
	}
	
	public int getNext() {
		String SQL = "SELECT gamepageID FROM GamePage ORDER BY gamepageID DESC";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; //현재가 첫번째 게시물인 경우
		} catch (Exception e){
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public int write(String gamepageTitle, String userID, String gamepageContent) {
		String SQL = "INSERT INTO GamePage VALUES (?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, gamepageTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, gamepageContent);
			pstmt.setInt(6, 1);
			return pstmt.executeUpdate();
		} catch (Exception e){
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public ArrayList<Gamepage> getList(int pageNumber) {
		String SQL = "SELECT * FROM GamePage where gamepageID < ? AND gamepageAvailable = 1 ORDER BY gamepageID DESC LIMIT 10";
		ArrayList<Gamepage> list = new ArrayList<Gamepage>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Gamepage gamepage = new Gamepage();
				gamepage.setGamepageID(rs.getInt(1));
				gamepage.setGamepageTitle(rs.getString(2));
				gamepage.setUserID(rs.getString(3));
				gamepage.setGamepageDate(rs.getString(4));
				gamepage.setGamepageContent(rs.getString(5));
				gamepage.setGamepageAvailable(rs.getInt(6));
				list.add(gamepage);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return list; // 데이터베이스 오류
	}
	
	public boolean nextPage(int pageNumber) {
		String SQL = "SELECT * FROM GamePage where gamepageID < ? AND gamepageAvailable = 1";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return false; // 데이터베이스 오류
	}
	
}
