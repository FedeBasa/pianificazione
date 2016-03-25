package it.soprasteria.pianificazione.v2.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import it.soprasteria.pianificazione.v2.bean.EnumBean;

public class EnumDaoImpl extends JdbcDaoSupport implements EnumDao {
	
	
	private static final Logger LOG = Logger.getLogger(EnumDaoImpl.class);
	
	@Override
	public EnumBean findById(final Integer id) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT *");
		sb.append(" FROM enum_config");
		sb.append(" WHERE id =  ?");
		
		List<EnumBean> enumList = getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter(){
			
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				 pstm.setInt(1, id);
			}
			
		},new RowMapper<EnumBean>(){
			
			@Override
			public EnumBean mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				EnumBean enumbean = new EnumBean();
				enrichEnumBean(rs, enumbean);
				return enumbean;
			}

		});
		return enumList.get(0);
	}

	@Override
	public List<EnumBean> findByType(final String tipologia) {
		
		LOG.debug("tipologia :" + tipologia);
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT *");
		sb.append(" FROM enum_config");
		sb.append(" WHERE tipo =  ?");
		
		List<EnumBean> enumList = getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter(){
			
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				 pstm.setString(1, tipologia);
			}
			
		},new RowMapper<EnumBean>(){
			
			@Override
			public EnumBean mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				EnumBean enumbean = new EnumBean();
				enrichEnumBean(rs, enumbean);
				return enumbean;
			}

		});
		return enumList;
	}
	
	public void enrichEnumBean(ResultSet rs, EnumBean enumbean) throws SQLException {
		enumbean.setId(rs.getInt("id"));
		enumbean.setTipologia(rs.getString("tipo"));
		enumbean.setValue(rs.getString("valore"));
	}
	
}
