package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.entities.Department;
import model.entities.Seller;

public class SellerRepository {

	private Connection conn;

	public SellerRepository(Connection conn) {
		super();
		this.conn = conn;
	}

	public void insert(Seller seller) {
		PreparedStatement st = null;
		String sql = "INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES (?,?,?,?,?)";
		try {
			st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, seller.getName());
			st.setString(2, seller.getEmail());
			st.setDate(3, java.sql.Date.valueOf(seller.getBirthDate()));
			st.setDouble(4, seller.getBaseSalary());
			st.setInt(5, seller.getDepartment().getId());

			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				var rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					seller.setId(id);
				}
				DB.closeResultSet(rs);
			}
			if (rowsAffected == 0) {
				throw new SQLException("No rows affected");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			DB.closeStatement(st);

		}
	}

	public List<Seller> findAll() {
		Statement st = null;
		ResultSet rs = null;
		List<Seller> list = new ArrayList<>();
		String sql = "SELECT s.Id, s.Name AS SellerName, s.Email, s.BirthDate, s.BaseSalary, d.Id AS DepartmentId, d.Name AS DepartmentName FROM seller s JOIN department d ON s.DepartmentId = d.Id";

		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				Department department = new Department(rs.getInt("DepartmentId"), rs.getString("DepartmentName"));
				LocalDate birthDate = rs.getDate("BirthDate").toLocalDate();
				Seller seller = new Seller(rs.getString("SellerName"), rs.getString("Email"), birthDate,
						rs.getDouble("BaseSalary"), department);
				seller.setId(rs.getInt("Id"));
				list.add(seller);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException("Error fetching all sellers: " + e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	public Seller findById(int id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "SELECT s.Id, s.Name AS SellerName, s.Email, s.BirthDate, s.BaseSalary, d.Id AS DepartmentId, d.Name AS DepartmentName FROM seller s JOIN department d ON s.DepartmentId = d.Id WHERE s.id = ?";

		try {
			st = conn.prepareStatement(sql);
			st.setInt(1, id);
			try {
				rs = st.executeQuery();
				if (rs.next()) {
					Department department = new Department(rs.getInt("DepartmentId"), rs.getString("DepartmentName"));
					LocalDate birthDate = rs.getDate("BirthDate").toLocalDate();
					Seller seller = new Seller(rs.getString("SellerName"), rs.getString("Email"), birthDate,
							rs.getDouble("BaseSalary"), department);
					seller.setId(rs.getInt("Id"));
					return seller;
				}
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}

		} catch (SQLException e) {
			throw new DbException("Error fetching seller by id: " + e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return null;
	}
}
