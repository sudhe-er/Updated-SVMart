package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Product;
import model.ProductStock;

public class ProductDAOImpl implements ProductDAO {
	public Connection connection;

	public ProductDAOImpl() throws ClassNotFoundException {
		connection = DAOFactory.getProductDAO();
		if (connection != null)
			System.out.println("connected");
	}

	@Override
	public boolean logIn(String username, String password) {
		try {
			String query = "SELECT * FROM users1729 WHERE username = ? AND password = ?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);

			ResultSet resultSet = preparedStatement.executeQuery();

			boolean isValidUser = resultSet.next();

			resultSet.close();
			preparedStatement.close();
			connection.close();

			return isValidUser;

		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public int addUser(String username, String password) {
		int rowsInserted;
		try {
			String query = "INSERT INTO users1729 (username, password) VALUES (?, ?)";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);

			rowsInserted = preparedStatement.executeUpdate();
			preparedStatement.close();
			connection.close();
			return rowsInserted;
		} catch (Exception e) {
		}

		return 0;
	}

	public List<List<Object>> getProductList() {

		List<List<Object>> productList = new ArrayList<>();

		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM get_product_details()");
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				// Create a list to hold Product and ProductStock objects for each row
				List<Object> row = new ArrayList<>();

				// Populate Product object
				Product product = new Product(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4),
						rs.getString(5), rs.getString(6));
				row.add(product);

				// Populate ProductStock object
				ProductStock productStock = new ProductStock(rs.getInt(1), rs.getString(7), rs.getDouble(8),
						rs.getInt(9), rs.getDouble(10));
				row.add(productStock);

				// Add the populated row to the productList
				productList.add(row);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return productList;
	}

	@Override
	public boolean getServiceableRegions(int pincode, int pctid) throws SQLException {
	    int result = 0;
	    String query = "SELECT checkServiceableRegion(?, ?)";
	    try (CallableStatement cs = connection.prepareCall(query)) {
	        cs.setInt(1, pincode);
	        cs.setInt(2, pctid);
	        try (ResultSet rs = cs.executeQuery()) {
	            if (rs.next()) {
	                result = rs.getInt(1);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;
	    }
	    System.out.println("this is servivuged " + result);
	    return result > 0;
	}


	public double getGst(int pid) throws SQLException {

		double result = 0;
	    String query = "SELECT get_gst_percentage(?)";
	    try (CallableStatement cs = connection.prepareCall(query)) {
	        cs.setInt(1, pid);
	        
	        try (ResultSet rs = cs.executeQuery()) {
	            if (rs.next()) {
	                result = rs.getDouble(1);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e;
	    }
	    System.out.println("this is servivuged " + result);
	    return result;

	}

}
