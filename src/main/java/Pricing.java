



import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import beans.AgeGroup;

/**
 * Servlet implementation class MyServlet
 */
@WebServlet("/pricing")
public class Pricing extends HttpServlet {
	private static final long serialVersionUID = 1L;
	       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Pricing() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		List<AgeGroup> ageList = queryAgeGroup();
		
		request.setAttribute("ageList", ageList);
		request.getRequestDispatcher("/pricing.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		processPost(request, response);
		
		//request.getRequestDispatcher("confirmation.jsp").forward(request, response);
		
		//System.out.println(request.getParameter("paymentRadio"));
		//doGet(request, response);
	}
	
	private List<AgeGroup> queryAgeGroup() {
		List<AgeGroup> ageList = new LinkedList<AgeGroup>();
		
		PreparedStatement preparedStatement = null;
		Connection connection = null;
		try {
			DBConnection.getDBConnection();
			connection = DBConnection.connection;
			
			String selectSQL = "SELECT * from age_group ORDER BY lower_age_range IS NULL DESC, lower_age_range ASC;";
			preparedStatement = connection.prepareStatement(selectSQL);
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				int id = rs.getInt("ageId");
				float price = rs.getFloat("price");
				int upperAge = rs.getInt("upper_age_range");
				int lowerAge = rs.getInt("lower_age_range");
				String name = rs.getString("group_name");
				
				ageList.add(new AgeGroup(id, price, upperAge, lowerAge, name));
			}
		} catch (SQLException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return(ageList);
	}
	
	private void processPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int lastInsertId = 0;
		int custId = 0;
		int ticketNum = 0;
		String custName = request.getParameter("firstName") + " " + request.getParameter("lastName");
		
		String selectSQL1 = "INSERT INTO ticket(amount_of_trips, ageId) VALUES (?, ?)";
        String selectSQL2 = "INSERT INTO customer(name) SELECT ? FROM dual WHERE NOT EXISTS (SELECT name FROM customer WHERE name=?)";
        String selectSQL3 = "SELECT id FROM customer WHERE name=?";
        String selectSQL4 = "INSERT INTO transaction (ticketId, custId, time, amount_paid, status) SELECT ?, ?, NOW(), price, ? FROM age_group WHERE ageId=?";
		
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
        	DBConnection.getDBConnection();
        	connection = DBConnection.connection;
        	if (connection == null) {
        		return;
        	}

        	ResultSet rs;
        	preparedStatement = connection.prepareStatement(selectSQL1, PreparedStatement.RETURN_GENERATED_KEYS);
        	preparedStatement.setInt(1, 1); // default 1 trip
        	preparedStatement.setInt(2, Integer.parseInt(request.getParameter("ageGroup")));
        	preparedStatement.executeUpdate();
        	rs = preparedStatement.getGeneratedKeys();
        	if (rs.next()) {
        		lastInsertId = rs.getInt(1);
        		ticketNum = lastInsertId;
        	}
        	
        	preparedStatement = connection.prepareStatement(selectSQL2);
        	preparedStatement.setString(1, custName);
        	preparedStatement.setString(2,  custName);
        	preparedStatement.executeUpdate();
        	
        	preparedStatement = connection.prepareStatement(selectSQL3);
        	preparedStatement.setString(1, custName);
        	rs = preparedStatement.executeQuery();
        	if (rs.next()) {
        		custId = rs.getInt("id");
        	}
        	
        	preparedStatement = connection.prepareStatement(selectSQL4, PreparedStatement.RETURN_GENERATED_KEYS);
        	preparedStatement.setInt(1, lastInsertId);
        	preparedStatement.setInt(2, custId);
        	preparedStatement.setString(3, request.getParameter("paymentRadio"));
        	preparedStatement.setInt(4, Integer.parseInt(request.getParameter("ageGroup")));
        	preparedStatement.executeUpdate();
        	
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	try {
        		preparedStatement.close();
        		connection.close();
        	} catch (Exception e2) {
        		e2.printStackTrace();
        	}
        }
        
        
        request.setAttribute("ticketNumber", ticketNum);
		request.getRequestDispatcher("confirmation.jsp").forward(request, response);
		
		
	}

}
