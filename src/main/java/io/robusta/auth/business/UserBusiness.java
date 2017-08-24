package io.robusta.auth.business;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import io.robusta.auth.dao.MySQLDatabaseConnection;
import io.robusta.auth.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserBusiness {
	private Connection connection;

	public UserBusiness(MySQLDatabaseConnection connection){
		this.connection = connection.getConnection();
	}

	public User findByEmail(String email) {
		try {
			String sql = "SELECT * FROM `users` WHERE email = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1,email);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				return userFactory(resultSet);
			else
				return null;
		} catch (SQLException e){
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)",e);
		}
	}

	private User userFactory(ResultSet resultSet) throws SQLException{

		User user = new User();

		String email = resultSet.getString("email");
		String name = resultSet.getString("name");
		String password = resultSet.getString("password");

		user.setEmail(email);
		user.setName(name);
		user.setPassword(password);

		return user;
	}

	public User findByName(String name) {
		try {
			String sql = "SELECT * FROM `users` WHERE name = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1,name);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				return userFactory(resultSet);
			else
				return null;
		} catch (SQLException e){
			throw new RuntimeException("Impossible de réaliser l(es) opération(s)",e);
		}


	}






	public User createPasswordUser(User user) {

		try {
			String sql = "INSERT INTO users (`name`,`email`,`password`) VALUES (?,?,?)";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1,user.getName());
			statement.setString(2,user.getEmail());
			statement.setString(3,user.getPassword());

			statement.executeUpdate();

			throw new RuntimeException("Aucun user ajouté");
		} catch (SQLException e) {
			throw new RuntimeException("Impossible d'ajouter votre compte",e);
		}


	}




	public void updateUser(User user) {
		if (user.getName() == null)
			throw new RuntimeException("Impossible d'effacer un nom sans nom !!");

		try {
			String sql = "UPDATE users SET `name` = ?, `email` = ?, `password`= ?";
			PreparedStatement statement= connection.prepareStatement(sql);
			statement.setString(1,user.getName());
			statement.setString(2,user.getEmail());
			statement.setString(3,user.getPassword());

			statement.executeUpdate();
		}catch (SQLException e){
			throw new RuntimeException("Impossible de mettre à jour l'utilisateur", e);
		}
	}

	public void deleteUser(User user) {
		try {
			String sql = "DELETE FROM users WHERE name = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, user.getName());
			statement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Impossible de retirer cet utilisateur", e);
		}

	}
	
	

}
