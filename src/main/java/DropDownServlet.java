import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

//import org.json.JSONObject;

@WebServlet("/dropdown")
public class DropDownServlet extends HttpServlet {

	//private List<StopDropDown> stopDDList = new ArrayList<StopDropDown>();

	public DropDownServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// String keyword = request.getParameter("keyword");
		// search(keyword, response);
		// buildPage(response);
		getDropDown(response, request);
	}

	private List<StopDropDown> getDropDown(HttpServletResponse response, HttpServletRequest request)
			throws ServletException, IOException {

		String selectedCity = request.getParameter("originCity");
		System.out.println("47 selectedCity: " + selectedCity);
		return queryDropDown(response, selectedCity);

	}

	private List<StopDropDown> queryDropDown(HttpServletResponse response, String selectedCity)
			throws ServletException, IOException {

		// PrintWriter out = response.getWriter();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		List<StopDropDown> stopDDList = new ArrayList<StopDropDown>();
		JSONArray jsonList = new JSONArray();
		try {
			
			DBConnection.getDBConnection();
			connection = DBConnection.connection;
			String selectSQL = "select stopId, stop_name from shuttle_stop where city_name = ?;";
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setString(1, selectedCity); // City Name Param

			ResultSet rs = preparedStatement.executeQuery();

			//JSONObject jsonSDD = new JSONObject();
			
			while (rs.next()) {
				JSONObject jsonSDD = new JSONObject();
				StopDropDown sdd = new StopDropDown();
				jsonSDD.append(rs.getString("stopId"), rs.getString("stop_name"));
				sdd.setId(rs.getString("stopId"));
				sdd.setName(rs.getString("stop_name"));
				stopDDList.add(sdd);
				jsonList.put(jsonSDD);
//				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//				String json = ow.writeValueAsString(object);
			}
			//System.out.println("74 DD Servlet: " + jsonSDD);
			System.out.println("75 DD Servlet: " + stopDDList.size());
			System.out.println("76 DD Servlet: " + stopDDList.get(0));
			System.out.println("77 DD Servlet: " + stopDDList.get(0).getName());
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			out.print(jsonList);
			out.flush();

			rs.close();
			preparedStatement.close();
			connection.close();

		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return stopDDList;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
