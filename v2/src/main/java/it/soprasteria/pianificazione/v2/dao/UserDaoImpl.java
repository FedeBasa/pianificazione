package it.soprasteria.pianificazione.v2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import it.soprasteria.pianificazione.v2.bean.UserBean;

@Qualifier
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

	@Override
	public UserBean login(final String username, final String password) {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT *");
		sb.append(" FROM users");
		sb.append(" WHERE username = ? AND password = ?");
		List<UserBean> userlist = getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				pstm.setString(1, username);
				pstm.setString(2, password);
			}
		}, new RowMapper<UserBean>() {
			@Override
			public UserBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				UserBean user = UserBean.build(rs.getString("username"), rs.getString("nome"), rs.getString("cognome"), rs.getString("profilo"));
				user.setPassword(rs.getString("password"));
				user.setFirstlogin(rs.getInt("first_login"));
				return user;
			}
		});

		if (userlist.isEmpty()) {
			return null;
		}
		return userlist.get(0);
	}
	
	@Override
	public void changePassword(final String userId, final String password) {
		final StringBuilder sb = new StringBuilder();

		sb.append("UPDATE users");
		sb.append(" SET password = ?,");
		sb.append(" first_login = 1");
		sb.append(" WHERE username = ?");

		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

				PreparedStatement pstm = con.prepareStatement(sb.toString());
				pstm.setString(1, password);
				pstm.setString(2, userId);

				return pstm;
			}
		});
	}


	@Override
	public UserBean findByUsername(final String username) {
		
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT *");
		sb.append(" FROM users");
		sb.append(" WHERE username = ?");

		List<UserBean> list = getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, username);
			}
		}, new RowMapper<UserBean>() {
			
			@Override
			public UserBean mapRow(ResultSet rs, int rowNum) throws SQLException {

				UserBean bean = new UserBean();
				enrichUserBean(bean, rs);
				return bean;
			}
		});
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	@Override
	public List<UserBean> findByProfilo(final String profilo) {
		
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT *");
		sb.append(" FROM users");
		sb.append(" WHERE profilo = ?");

		return getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, profilo);
			}
		}, new RowMapper<UserBean>() {
			
			@Override
			public UserBean mapRow(ResultSet rs, int rowNum) throws SQLException {

				UserBean bean = new UserBean();
				enrichUserBean(bean, rs);
				return bean;
			}
		});
	}
	
	private void enrichUserBean(UserBean bean, ResultSet rs) throws SQLException {
		
		bean.setUsername(rs.getString("username"));
		bean.setRealUsername(rs.getString("username"));
		bean.setName(rs.getString("nome"));
		bean.setSurname(rs.getString("cognome"));
		bean.setProfilo(rs.getString("profilo"));
	}

}
