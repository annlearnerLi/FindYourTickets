package rpc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;

import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchItem() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		// 用JSON array
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));
		String term = request.getParameter("term");
		
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			List<Item> items = connection.searchItems(lat, lon, term);
			JSONArray array = new JSONArray();
			for(Item cur : items) {
				array.put(cur.toJSONObject());
			}
			RpcHelper.writeJsonArray(response, array);
		}catch (Exception e) {
	  		   e.printStackTrace();
	    }finally {
	  		 connection.close();
        }
		
//		response.setContentType("application/json");
//		PrintWriter writer = response.getWriter();
//		JSONArray array = new JSONArray();
//		try {
//			array.put(new JSONObject().put("username", "abcd"));
//			array.put(new JSONObject().put("username", "1234"));	
//		}catch(JSONException e) {
//			e.printStackTrace();
//		}
//		RpcHelper.writeJsonArray(response, array);
//		writer.print(array);
//		writer.close();
		
		// 用JSON
//		response.setContentType("application/json");
//		PrintWriter writer = response.getWriter();
//		if(request.getParameter("username") != null) {
//			String username = request.getParameter("username");
//			JSONObject obj = new JSONObject();
//			try {
//				obj.put("username", username);
//			}catch(JSONException e){
//				e.printStackTrace();
//			}
//			writer.print(obj);	
//		}
//		writer.close();
		
		
		// 加入html
// 		PrintWriter writer = response.getWriter();
//		 if(request.getParameter("username") != null) {
//			 String username = request.getParameter("username");
// 			 writer.println("<html><body>");
//		     writer.println("<h1>Hello"+ username+"</h1>");
//		     writer.println("</body></html>");
//			 
//		 }
//  可以传入http

//		writer.println("<html><body>");
//		writer.println("<h1>Hello word</h1>");
//		writer.println("</body></html>");
//		
//		writer.close();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
