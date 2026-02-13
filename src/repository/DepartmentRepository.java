package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import model.entities.Department;

public class DepartmentRepository {

	private Connection conn;

	public DepartmentRepository(Connection conn) {
		this.conn = conn;
	}

	public void insert(Department department) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, department.getName());
			int rowsAffected = st.executeUpdate();

			if (rowsAffected > 0) {
				var rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					department.setId(id);
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

	public void update(Department department) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE department SET name = ? WHERE ID = ?");
			st.setString(1, department.getName());
			st.setInt(2, department.getId());

			int rowsAffected = st.executeUpdate();
			if (rowsAffected == 0) {
				throw new SQLException("No rows affected. Id may not exist.");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			DB.closeStatement(st);
		}

	}
}
