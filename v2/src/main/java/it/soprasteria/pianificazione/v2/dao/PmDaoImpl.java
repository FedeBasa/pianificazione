package it.soprasteria.pianificazione.v2.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import it.soprasteria.pianificazione.v2.bean.PmBean;

public class PmDaoImpl extends JdbcDaoSupport implements PmDao {

	
	private static final Logger LOG = Logger.getLogger(PmDaoImpl.class);
	
	@Override
	public List<PmBean> verifyV2() {
		
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT *" );
		sb.append(" FROM users");
		sb.append(" WHERE profilo = 'pm'");
		
		List<PmBean> pmList = getJdbcTemplate().query(sb.toString(), new RowMapper<PmBean>(){
		@Override
		public PmBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			PmBean pm = new PmBean();
			
			pm.setPm(rs.getString("nome")+" "+rs.getString("cognome"));
			pm.setUsername(rs.getString("username"));
			return pm;
		}
	});
		
	return pmList;
		
	}

	@Override
	public List<PmBean> verifyStatus(final Integer month, final PmDao dao ) {
		
		List<PmBean> pmList = new ArrayList<PmBean>();
		
		for(int i = 0; i< dao.verifyV2().size()-1 ; i++){
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT id_user,editable,mese");
		sb.append(" FROM v2 ");
		sb.append(" WHERE username = ? AND month = ?");
		
		 pmList.add((PmBean) getJdbcTemplate().query(sb.toString(),new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				int i = 1;
				ps.setString(i++, dao.verifyV2().get(i).getUsername());
				ps.setInt(i++, month);
			}
		}, new RowMapper<PmBean>(){
			@Override
			public PmBean mapRow(ResultSet rs, int rowNum) throws SQLException {
				PmBean pm = new PmBean();
				
				pm.setMese(rs.getInt("mese"));
				pm.setPm(rs.getString("id_user"));
				pm.setStato(String.valueOf(rs.getInt("stato")));
				return pm;
			}
		
		}));	 
		};
		
		return pmList;
	}

}
