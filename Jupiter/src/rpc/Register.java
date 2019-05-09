package rpc;

import java.io.IOException;
import java.util.jar.JarOutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import db.mysql.MySQLConnection;

/**
 * Servlet implementation class Register
 */
@WebServlet("/register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			JSONObject input = RpcHelper.readJSONObject(request);
			String userId = input.getString("user_id");
			String password = input.getString("password");
			String firstname = input.getString("first_name");
			String lastname = input.getString("last_name");
			
			JSONObject obj = new JSONObject();
			if(connection.registerUser(userId, password, firstname , lastname)) {
				obj.put("status","ok");
			}else {
				obj.put("status", "user already exists");
			}
			RpcHelper.writeJsonObject(response, obj);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			connection.close();
		}
	}

}
