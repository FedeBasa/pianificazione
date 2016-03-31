package it.soprasteria.pianificazione.v2.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import it.soprasteria.pianificazione.v2.bean.UserBean;

public class PmDaoImpl extends JdbcDaoSupport implements PmDao {

	@Override
	public List<UserBean> verifyV2(final Integer month) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT id_user,editable,business_unit");
		sb.append(" FROM v2");
		sb.append(" WHERE mese = ?");
		
		List<UserBean> list = getJdbcTemplate().query(sb.toString(),new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				int i = 1;
				ps.setInt(i++,month);
			}
		},new RowMapper<UserBean>(){
			@Override
			public UserBean mapRow(ResultSet rs, int rowNum) throws SQLException {
				 UserBean bean = new UserBean();
				 bean.setProfilo(rs.getString("id_user"));
				 if(rs.getInt("editable")==50){
					 bean.setActive("Validato");
				 }else if(rs.getInt("editable")==10){
					 bean.setActive("Chiuso");
				 }else if(rs.getInt("editable")==100){
					 bean.setActive("Aperto, da validare");
				 }
				 bean.setBu(rs.getInt("business_unit"));
				return bean;
			}
		});
		
		return list;
		
	}

}
