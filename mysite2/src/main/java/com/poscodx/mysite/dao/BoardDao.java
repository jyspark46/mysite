package com.poscodx.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.poscodx.mysite.vo.BoardVo;

public class BoardDao {
	// write
	public int findMaxGroupNo() {
		int result = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			String sql = "select ifnull(max(g_no), 1) from board";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("error " + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}

				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error " + e);
			}
		}
		
		return result;
	}
	
	// write, reply
	public int insert(BoardVo vo) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"insert into board(no, title, contents, reg_date, hit, g_no, o_no, depth, user_no) " +
					"values(null, ?, ?, sysdate(), ?, ?, ?, ?, ?)");
			// PreparedStatement pstmt2 = conn.prepareStatement("select last_insert_id() from dual");
		) {
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setInt(3, vo.getHit());
			pstmt.setInt(4, vo.getgNo());
			pstmt.setInt(5, vo.getoNo());
			pstmt.setInt(6, vo.getDepth());
			pstmt.setLong(7, vo.getUserNo());
			result = pstmt.executeUpdate();
	
			//ResultSet rs = pstmt2.executeQuery();
			//vo.setNo(rs.next() ? rs.getLong(1) : null);
			//rs.close();
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}

		return result;
	}
	
	// list
	public List<BoardVo> findAllSearch(String searchValue, int first, int second) {
		List<BoardVo> result = new ArrayList<>();

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			String sql = "select a.no, a.title, a.contents, a.reg_date, a.depth, a.hit, b.no as user_no, b.name "
					+ "from board a, user b "
					+ "where a.user_no = b.no and a.title like '%" + searchValue + "%' "
					+ "order by a.g_no desc, a.o_no asc limit ?, ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, first);
			pstmt.setInt(2, second);

			rs = pstmt.executeQuery();

			while(rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				String regDate = rs.getString(4);
				int depth = rs.getInt(5);
				int hit = rs.getInt(6);
				Long userNo = rs.getLong(7);
				String userName = rs.getString(8);

				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setRegDate(regDate);
				vo.setDepth(depth);
				vo.setHit(hit);
				vo.setUserNo(userNo);
				vo.setUserName(userName);

				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	// delete
	public int deleteByNo(Long no) {
		int result = 0;
		
		try (
			Connection conn = getConnection();	
			PreparedStatement pstmt = conn.prepareStatement("delete from board where no = ?");
		) {
			pstmt.setLong(1, no);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}
		
		return result;		
	}
	
	// delete
	public Boolean delete(Long no) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean result = false;

		try {
			conn = getConnection();
			String sql = "delete from board where no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no);
			int count = pstmt.executeUpdate();
			result = count == 1;
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	// view, modify, reply
	public BoardVo findByNo(Long no2) {
		BoardVo vo = null;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			String sql = "select a.no, a.title, a.contents, a.reg_date, a.hit, a.g_no, a.o_no, a.depth, b.no, b.name "
					   + "from board a, user b "
					   + "where a.user_no = b.no and a.no = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no2);
			rs = pstmt.executeQuery();

			while(rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String content = rs.getString(3);
				String regDate = rs.getString(4);
				int hit = rs.getInt(5);
				int groupNo = rs.getInt(6);
				int orderNo = rs.getInt(7);
				int depths = rs.getInt(8);
				Long userNo = rs.getLong(9);
				String userName = rs.getString(10);

				vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(content);
				vo.setRegDate(regDate);
				vo.setHit(hit);
				vo.setgNo(groupNo);
				vo.setoNo(orderNo);
				vo.setDepth(depths);
				vo.setUserNo(userNo);
				vo.setUserName(userName);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return vo;
	}
	
	// view
	public void updateHit(Long no) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();
			String sql = "update board set hit = hit + 1 where no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, no);

			int count = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// view
	public BoardVo updateView(Long no, String title, String contents) {
		BoardVo vo = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean result = false;

		try {
			conn = getConnection();
			String sql = "update board set title = ?, contents = ? where no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, contents);
			pstmt.setLong(3, no);
			int count = pstmt.executeUpdate();
			result = count == 1;
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return vo;
	}
	
	// reply
	public static int findHasSameDepth(int gNo, int depth) {
		int countSameDepth = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			String sql = "select count(*) from board where g_no = ? and depth = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, gNo);
			pstmt.setLong(2, depth);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				countSameDepth = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("error " + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error " + e);
			}
		}
		
		return countSameDepth;
	}
	
	// reply
	public static int findHasPlusOneDepth(int gNo, int depth) {
		int countPlusOneDepth = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			String sql = "select count(*) from board where g_no = ? and depth = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, gNo);
			pstmt.setLong(2, (depth+1));
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				countPlusOneDepth = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("error " + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error " + e);
			}
		}
		
		return countPlusOneDepth;
	}
	
	// reply
	public boolean setOrder(int gNo, int oNo, int depth) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean result=false;
		
		try {
			conn = getConnection();
			String sql = "update board "
					   + "set o_no = o_no + 1 "
					   + "where g_no = ? and o_no > ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, gNo);
			pstmt.setLong(2, oNo);

			int count = pstmt.executeUpdate();
			result = count == 1;
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	// paging
	public int findAllCount() {
		int result = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();

			String sql = "select count(*) from board";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	public List<BoardVo> findAll(int first, int second) {
		List<BoardVo> result = new ArrayList<>();
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
				"select a.no ,  a.title  , a.contents , a.reg_date, a.depth , a.hit , b.no as user_no , b.name" +
				"from board a, user b" + 
				"where a.user_no = b.no" + 
				"order by a.g_no desc, a.o_no asc" +
				"limit ? , ?");
			ResultSet rs = pstmt.executeQuery();
		) {
			pstmt.setInt(1, first);
			pstmt.setInt(2, second);
			
			while(rs.next()) {
				Long no = rs.getLong(1);
				String title = rs.getString(2);
				String contents = rs.getString(3);
				String regDate = rs.getString(4);
				int depth = rs.getInt(5);
				int hit = rs.getInt(6);
				Long userNo = rs.getLong(7);
				String userName = rs.getString(8);

				BoardVo vo = new BoardVo();
				vo.setNo(no);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setRegDate(regDate);
				vo.setDepth(depth);
				vo.setHit(hit);
				vo.setUserNo(userNo);
				vo.setUserName(userName);

				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("Error:" + e);
		}	
		
		return result;
	}
	
	public Boolean insertReply(BoardVo vo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean result = false;

		try {
			conn = getConnection();
			String sql = "insert into board(no, title, contents, reg_date, hit, g_no, o_no, depth, user_no) "
					   + "values(null, ?, ?, sysdate(), ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setInt(3, vo.getHit());
			pstmt.setInt(4, vo.getgNo());
			pstmt.setInt(5, vo.getoNo());
			pstmt.setInt(6, vo.getDepth());
			pstmt.setLong(7, vo.getUserNo());
			
			int count = pstmt.executeUpdate();
			result = count == 1;
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	public boolean replyUpdate(int groupNo, int orderNo) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		boolean result = false;

		try {
			conn = getConnection();
			String sql = "update board set o_no = ? + 1 where g_no = ? and o_no >= ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, orderNo);
			pstmt.setInt(2, groupNo);
			pstmt.setInt(3, orderNo);

			int count = pstmt.executeUpdate();
			result = count == 1;
		} catch (SQLException e) {
			System.out.println("error " + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error " + e);
			}
		}
		
		return result;
	}
	
	public int findMaxOrderNo() {
		int result = 0;

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = getConnection();
			String sql = "select max(o_no) from board";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();

			if(rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("error " + e);
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}

				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error " + e);
			}
		}
		
		return result;
	}

	private static Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			
			String url = "jdbc:mariadb://192.168.0.206:3306/webdb?charset=utf8";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		} 
		
		return conn;
	}
}