import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

@WebServlet("/index")
public class HomePage extends HttpServlet {

	private static final boolean _W = System.getProperty("os.name").toLowerCase().contains("windows");
	private static String PATHHEAD = "../webapps/404NotFound/" + "homePageContent/";
	private static final Charset UTF8 = StandardCharsets.UTF_8;

	private Map<String, String[]> body = null;

	public HomePage() {
		super();
		if (!_W) {
			PATHHEAD = PATHHEAD.replace("../", "./");
		}
	}

	private static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// String keyword = request.getParameter("keyword");
		// search(keyword, response);
		buildPage(response);
	}

	private void buildPage(HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(readFile(PATHHEAD + "header.html", UTF8));
		out.println(readFile(PATHHEAD + "bodyH1.html", UTF8));

		out.println(readFile(PATHHEAD + "body.html", UTF8));
		if(body != null) {
			queryTable(response);
		}

		out.println(readFile(PATHHEAD + "bodyF1.html", UTF8));
		out.println(readFile(PATHHEAD + "footer.html", UTF8));
	}
	private String formatSA(String[] in) {
		return Arrays.toString(in).replace("[","").replace("]","");
	}
	private void queryTable(HttpServletResponse response) throws ServletException, IOException {
		/*
		 * mysql> describe departure;
		 * +-----------+---------+------+-----+---------+-------+ | Field | Type | Null
		 * | Key | Default | Extra |
		 * +-----------+---------+------+-----+---------+-------+ | stopId | int(11) |
		 * NO | PRI | NULL | | | shuttleId | int(11) | NO | PRI | NULL | | | time | time
		 * | NO | PRI | NULL | | | dayOfWeek | int(11) | NO | PRI | NULL | |
		 * +-----------+---------+------+-----+---------+-------+ 4 rows in set (0.00
		 * sec)
		 */
		PrintWriter out = response.getWriter();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			DBConnection.getDBConnection();
			connection = DBConnection.connection;
			String selectSQL = "SELECT fromCityName, fromStopName, fromTime, toCityName, toStopName, toTime " + "FROM "
					+ "(SELECT city_name as fromCityName, stop_name as fromStopName, time as fromTime "
					+ "FROM shuttle_stop "
					+ "INNER JOIN departure on departure.stopId = shuttle_stop.stopId WHERE shuttle_stop.stopId = ? ) as fromStop "
					+ "LEFT JOIN (" + "SELECT city_name as toCityName, stop_name as toStopName, time as toTime "
					+ "FROM shuttle_stop " + "INNER JOIN departure on departure.stopId = shuttle_stop.stopId "
					+ "WHERE shuttle_stop.stopId = ?) as toStop ON fromStop.fromTime < toStop.toTime " + "LEFT JOIN ("
					+ "SELECT city_name as intruderCityName, stop_name as intruderStopName, time as intruderTime "
					+ "FROM shuttle_stop "
					+ "INNER JOIN departure on departure.stopId = shuttle_stop.stopId WHERE shuttle_stop.stopId = ? ) as intruderStop "
					+ "ON intruderStop.intruderTime < toStop.toTime AND intruderStop.intruderTime > fromStop.fromTime "
					+ "WHERE intruderStop.intruderTime IS NULL;";
			preparedStatement = connection.prepareStatement(selectSQL);
			int i = 1;
			preparedStatement.setString(i++, formatSA(body.get("origin-stop"))); // id number of the from shuttle stop
			preparedStatement.setString(i++, formatSA(body.get("destination-stop"))); // id numbers of the destination shuttle stop
			preparedStatement.setString(i++, formatSA(body.get("destination-stop"))); // id numbers of the destination shuttle stop
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String fromCityName = rs.getString("fromCityName");
				String fromStopName = rs.getString("fromStopName");
				String fromTime = rs.getString("fromTime");
				String toCityName = rs.getString("toCityName");
				String toStopName = rs.getString("toStopName");
				String toTime = rs.getString("toTime");

				out.println("<tr>");
				out.println("<td>" + fromCityName + "</td>");
				out.println("<td>" + fromStopName + "</td>");
				out.println("<td>" + fromTime + "</td>");
				out.println("<td>" + toCityName + "</td>");
				out.println("<td>" + toStopName + "</td>");
				out.println("<td>" + toTime + "</td>");
				out.println("</tr>");
			}
			// out.println("<a href=/webproject/simpleFormSearch.html>Search Data</a>
			// <br>");
			// out.println("</body></html>");
			rs.close();
			preparedStatement.close();
			connection.close();
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} catch (SQLException se2) {
			}
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.body = request.getParameterMap();
		doGet(request, response);
	}

}
