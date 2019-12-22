import pojo.Reservation;

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
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

@WebServlet("/reservations")
public class Reservations extends HttpServlet {

    private static final boolean _W = System.getProperty("os.name").toLowerCase().contains("windows");
    private static String PATHHEAD = "../webapps/404NotFound/" + "reservationsContent/";
    private static final Charset _UTF8 = StandardCharsets.UTF_8;

    public Reservations() {
        super();
        if (!_W) {
            PATHHEAD = PATHHEAD.replace("../", "./");
        }
    }

    private static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, Reservations._UTF8);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        buildPage(response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, String[]> body = request.getParameterMap();
        //printPostRequest();
        Reservation rez = new Reservation(
                Integer.parseInt(formatSA(body.get("agegroup"))),
                1,
                formatSA(body.get("firstname"))+" "+formatSA(body.get("lastname")),
                formatSA(body.get("payment")),
                Integer.parseInt(formatSA(body.get("departId"))),
                Integer.parseInt(formatSA(body.get("fromStop")))
        );
        insertReservation(rez, response);
        doGet(request, response);
    }

    private String formatSA(String[] in) {
        return Arrays.toString(in).replace("[","").replace("]","");
    }

    private void buildPage(HttpServletResponse response) throws IOException {
        PrintWriter OUT = response.getWriter();
        response.setContentType("text/html");
        OUT.println(readFile(PATHHEAD + "header.html"));
        OUT.println(readFile(PATHHEAD + "bodyH1.html"));
        OUT.println(readFile(PATHHEAD + "body.html"));

        queryTable(response);

        OUT.println(readFile(PATHHEAD + "bodyF1.html"));

        queryAgeGroup(response);

        OUT.println(readFile(PATHHEAD + "bodyF2.html"));
        OUT.println(readFile(PATHHEAD + "footer.html"));
    }

    private void queryAgeGroup(HttpServletResponse response) throws IOException {
        //<option value=''>Select One</option>
        PrintWriter OUT = response.getWriter();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            DBConnection.getDBConnection();
            connection = DBConnection.connection;
            String selectSQL = "SELECT ageId, group_name FROM age_group";
            if(connection == null) {
                OUT.println("<option value='1'>401AccessDenied|404NotFound:Adult</option>");
                return;
            }
            preparedStatement = connection.prepareStatement(selectSQL);
            ResultSet rs = preparedStatement.executeQuery();
            OUT.println("<option value=''>Select One</option>");
            while (rs.next()) {
                String id = rs.getString("ageId");
                String name = rs.getString("group_name");
                OUT.println("<option value='"+id+"'>"+name+"</option>");
            }
            rs.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    private void queryTable(HttpServletResponse response) throws IOException{
        /*
            mysql> select * from shuttle_stop;
            +--------+-----------+---------+-----------+
            | stopId | stop_name | routeId | city_name |
            +--------+-----------+---------+-----------+
            |      1 | 10th St   |       2 | Omaha     |
            |      2 | 72nd St   |       2 | Omaha     |
            |      3 | 90th St   |       2 | Omaha     |
            |      4 | 120th St  |       2 | Omaha     |
            |      5 | 72nd St   |       1 | Omaha     |
            |      6 | 9th St    |       3 | Lincoln   |
            |      7 | 27th St   |       3 | Lincoln   |
            |      8 | 70th St   |       3 | Lincoln   |
            |      9 | 27th St   |       1 | Lincoln   |
            +--------+-----------+---------+-----------+
            9 rows in set (0.00 sec)
         */
        PrintWriter OUT = response.getWriter();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            DBConnection.getDBConnection();
            connection = DBConnection.connection;
            if(connection == null) {
                OUT.println("<tr><td>401AccessDenied|404NotFound</td></tr>");
                return;
            }
            String selectSQL = "SELECT fromCityName, fromStopName, fromTime, toCityName, toStopName, toTime, departId FROM (SELECT city_name as fromCityName, stop_name as fromStopName, time as fromTime, departure.departId FROM shuttle_stop INNER JOIN departure on departure.stopId = shuttle_stop.stopId WHERE shuttle_stop.stopId = ? ) as fromStop LEFT JOIN (SELECT city_name as toCityName, stop_name as toStopName, time as toTime FROM shuttle_stop INNER JOIN departure on departure.stopId = shuttle_stop.stopId WHERE shuttle_stop.stopId = ?) as toStop ON fromStop.fromTime < toStop.toTime LEFT JOIN (SELECT city_name as intruderCityName, stop_name as intruderStopName, time as intruderTime FROM shuttle_stop INNER JOIN departure on departure.stopId = shuttle_stop.stopId WHERE shuttle_stop.stopId = ? ) as intruderStop ON intruderStop.intruderTime < toStop.toTime AND intruderStop.intruderTime > fromStop.fromTime WHERE intruderStop.intruderTime IS NULL;";
            preparedStatement = connection.prepareStatement(selectSQL);
            int i = 1;
            preparedStatement.setString(i++, "1"); //id number of the from shuttle stop
            preparedStatement.setString(i++, "6"); //id numbers of the destination shuttle stop
            preparedStatement.setString(i++, "6"); //id numbers of the destination shuttle stop
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String fromCityName = rs.getString("fromCityName");
                String fromStopName = rs.getString("fromStopName");
                String fromTime = rs.getString("fromTime");
                String toCityName = rs.getString("toCityName");
                String toStopName = rs.getString("toStopName");
                String toTime = rs.getString("toTime");
                String departId = rs.getString("departId");

                OUT.println("<tr>");
                OUT.println("<td>"+fromCityName+"</td>");
                OUT.println("<td>"+fromStopName+"</td>");
                OUT.println("<td>"+fromTime+"</td>");
                OUT.println("<td>"+toCityName+"</td>");
                OUT.println("<td>"+toStopName+"</td>");
                OUT.println("<td>"+toTime+"</td>");
                OUT.println("<td><input type='button' value='Book Travel' class='btn btn-primary' data-toggle='modal' data-target='#exampleModal' onclick='bookTravel(1,6,"+departId+")'/></td>");
                OUT.println("</tr>");
            }
            rs.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }
    
    private void insertReservation(Reservation rez, HttpServletResponse response) throws IOException{
        PrintWriter OUT = response.getWriter();
        int lastInsertId = 0;
        int custId = 0;
        String selectSQL1 = "INSERT INTO ticket(amount_of_trips, ageId) VALUES (?, ?)";
        String selectSQL2 = "INSERT INTO customer(name) SELECT ? FROM dual WHERE NOT EXISTS (SELECT name FROM customer WHERE name=?)";
        String selectSQL3 = "SELECT id FROM customer WHERE name=?";
        String selectSQL4 = "INSERT INTO transaction (ticketId, custId, time, amount_paid, status) SELECT ?, ?, NOW(), price, ? FROM age_group WHERE ageId=?";
        String selectSQL5 = "INSERT INTO reservation (transId, departId, toStopId) VALUES (?, ?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            DBConnection.getDBConnection();
            connection = DBConnection.connection;
            if(connection == null) {
                OUT.println("401AccessDenied|404NotFound");
                return;
            }
            int i = 1;
            ResultSet rs;
            preparedStatement = connection.prepareStatement(selectSQL1, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(i++, rez.getAmount_of_trips());
            preparedStatement.setInt(i--, rez.getAgeId());
            OUT.println(preparedStatement.executeUpdate());
            rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                lastInsertId = rs.getInt(1);
            }

            preparedStatement = connection.prepareStatement(selectSQL2);
            preparedStatement.setString(i++, rez.getCustName());
            preparedStatement.setString(i--, rez.getCustName());
            OUT.println(preparedStatement.executeUpdate());

            preparedStatement = connection.prepareStatement(selectSQL3);
            preparedStatement.setString(i, rez.getCustName());
            rs = preparedStatement.executeQuery();
            if(rs.next()) {
                custId = rs.getInt("id");
            }

            preparedStatement = connection.prepareStatement(selectSQL4, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(i++, lastInsertId);
            preparedStatement.setInt(i++, custId);
            preparedStatement.setString(i++, rez.getPay_status());
            preparedStatement.setInt(i++, rez.getAgeId());
            OUT.println(preparedStatement.executeUpdate());
            rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                lastInsertId = rs.getInt(1);
            }

            i = 1;
            preparedStatement = connection.prepareStatement(selectSQL5);
            preparedStatement.setInt(i++, lastInsertId);
            preparedStatement.setInt(i++, rez.getDepartureId());
            preparedStatement.setInt(i++, rez.getToStopId());
            OUT.println(preparedStatement.executeUpdate());

            rs.close();
            preparedStatement.close();
            connection.close();
            OUT.println("INSERT_SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null)
                    preparedStatement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
    }

    private void printPostRequest(HttpServletRequest request, HttpServletResponse response) throws IOException{
        PrintWriter OUT = response.getWriter();
        OUT.println("post request made");
        OUT.println("request: " + request.toString());
        Enumeration<String> ghn = request.getHeaderNames();
        while(ghn.hasMoreElements()) {
            String key = ghn.nextElement();
            String value = request.getHeader(key);
            OUT.println(key+": "+value);
        }

        OUT.println("---------------------------BODY:");
        Map<String, String[]> body = request.getParameterMap();
        for(String key : body.keySet()) {
            for(String value : body.get(key)) {
                OUT.println(key + ": " + value);
            }
        }
    }
}
