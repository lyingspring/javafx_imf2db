package importfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * 数据库配置及相关方法
 * @author maoxj
 *
 */
public class oracle_jdbc {

	private Statement stmt = null;

	private ResultSet rs = null;

	private Connection conn = null;

	private PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement

	public oracle_jdbc() throws IOException {
		this.getConnection();
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public void getConnection() throws IOException {
        File file=new File(System.getProperty("user.dir").toString()+"\\oracle.properties");
        if(!file.exists()){
            throw new IOException("找不到数据库配置文件 oracle.properties");
        }
        InputStream inStream = new FileInputStream(file);
        Properties prop = new Properties();
        prop.load(inStream);
        String url=prop.getProperty("url");
        String user=prop.getProperty("user");
        String password=prop.getProperty("password");
        try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			//String url="jdbc:oracle:thin:@10.134.126.12:1521:casidb1";
			//orcl为数据库的SID
			//String user="casi";
			//String password="casiyth";
//			String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl"; // orcl为数据库的SID
//			String user = "jyjhk";
//			String password = "jyjhk";
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			System.out.println("jdbc: 数据库连接失败！请查看数据库配置！");
			System.out.println(e);
		}
	}
	/**
	 * 测试连接
	 * @return
	 */
	public String TestConnection() {
		String ss = null;
		try {

			String sql = "select 1 from dual";// 预编译语句，“？”代表参数
			pre = conn.prepareStatement(sql);// 实例化预编译语句

			rs = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
			while (rs.next()) {
				// 当结果集不为空时
				System.out.println("jdbc: 数据库连接正常！");

			}

		} catch (SQLException e) {
			System.out.println("jdbc: 数据库连接失败！请查看数据库配置！");
			e.printStackTrace();
		} finally {
			this.close(conn, stmt, rs, pre);
		}
		return ss;
	}

	public String getRes() {
		String ss = null;
		try {
			// stmt = conn.createStatement();
			// rs = stmt.executeQuery("select * from tbmeetroomequipment");
			// while (rs.next()) {
			// Tbmeet t = new Tbmeet();
			// t.setId(rs.getLong(1));
			// t.setName(rs.getString(2));
			// t.setEcid(rs.getLong(3));
			// list.add(t);
			// }
			String sql = "select * from aa10 where aaz093=?";// 预编译语句，“？”代表参数
			pre = conn.prepareStatement(sql);// 实例化预编译语句
			pre.setLong(1, 12l);// 设置参数，前面的1表示参数的索引，而不是表中列名的索引
			rs = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
			while (rs.next()) {
				// 当结果集不为空时
				System.out.println(
						"学号:" + rs.getInt("aaz093") + "姓名:" + rs.getString("aaa103") + "姓名1:" + rs.getString(4));
				ss = "学号:" + rs.getInt("aaz093") + "姓名:" + rs.getString("aaa103") + "姓名1:" + rs.getString(4);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			this.close(conn, stmt, rs, pre);
		}
		return ss;
	}

	/**
	 * 直接通过sql语句返回结果  获取字段用大写
	 * @param sql
	 * @return ArrayList<HashMap<String, String>>
	 */
	public ArrayList<HashMap<String, String>> createSQLQuery(String sql) {
		ArrayList<HashMap<String, String>> list_hashmap = new ArrayList<HashMap<String, String>>();
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData data = rs.getMetaData();
			while (rs.next()) {
				HashMap<String, String> map = new HashMap<String, String>();
				for (int i = 1; i <= data.getColumnCount(); i++) {// 数据库里从 1 开始

					String c = data.getColumnName(i);
					String v = rs.getString(c);
					System.out.println(c + ":" + v + "\t");
					map.put(c, v);
				}
				System.out.println("======================");
				list_hashmap.add(map);
			}


		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			this.close(conn, stmt, rs, pre);
		}

		return list_hashmap;
	}

	public int delete(String sql) throws SQLException {
		int number = 0;
		try {
			stmt = conn.createStatement();

			number = stmt.executeUpdate(sql);

			conn.commit();
		} catch (Exception e) {
			System.out.println(e);
			conn.rollback();
			number = 0;
		} finally {
			this.close(conn, stmt, rs, pre);
		}
		return number;
	}

	public int update(String sql) throws SQLException {
		int number = 0;
		try {
			stmt = conn.createStatement();

			number = stmt.executeUpdate(sql);

			conn.commit();
		} catch (Exception e) {
			System.out.println(e);
			conn.rollback();
			number = 0;
		}
//		finally {
//			this.close(conn, stmt, rs, pre);
//		}
		return number;
	}
	public void close(){
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (pre != null) {
				pre.close();
				pre = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void close(Connection conn, Statement stmt, ResultSet rs, PreparedStatement pre) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (pre != null) {
				pre.close();
				pre = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
