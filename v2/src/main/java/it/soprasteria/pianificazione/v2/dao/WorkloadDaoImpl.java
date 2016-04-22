package it.soprasteria.pianificazione.v2.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import it.soprasteria.pianificazione.v2.bean.WorkloadBean;
import it.soprasteria.pianificazione.v2.bean.WorkloadDetailBean;

public class WorkloadDaoImpl extends JdbcDaoSupport implements WorkloadDao {

	@Override
	public List<WorkloadBean> findWorkload(final int month, final String username) {

		StringBuilder sql = new StringBuilder();
		
		sql.append("select matricola, sum(consolidato_1) work1, sum(prodotto_1) rec1, sum(consolidato_2) work2, sum(prodotto_2) rec2, sum(consolidato_3) work3, sum(prodotto_3) rec3");
		sql.append(" from u_progetti_risorse");
		sql.append(" where mese = ?");
		sql.append(" and matricola in (select matricola from u_progetti_risorse where mese = ?  and utente_ins = ?)");
		sql.append(" group by matricola");

		return getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {

				int i = 1;
				pstm.setInt(i++, month);
				pstm.setInt(i++, month);
				pstm.setString(i, username);
			}
		}, new RowMapper<WorkloadBean>() {
			public WorkloadBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				
				WorkloadBean bean = new WorkloadBean();
				bean.setBadgeNumber(rs.getString("matricola"));
				bean.setWork1(rs.getInt("work1"));
				bean.setRecognized1(rs.getInt("rec1"));
				bean.setWork2(rs.getInt("work2"));
				bean.setRecognized2(rs.getInt("rec2"));
				bean.setWork3(rs.getInt("work3"));
				bean.setRecognized3(rs.getInt("rec3"));
				
				return bean;
			}
		});
	}
	
	@Override
	public List<WorkloadDetailBean> findWorkloadDetails(final int month, final String badgeNumber) {
		
		StringBuilder sql = new StringBuilder();

		sql.append("select matricola, nome_risorsa, cognome_risorsa, desc_progetto, consolidato_1, prodotto_1, consolidato_2, prodotto_2, consolidato_3, prodotto_3, utente_ins");
		sql.append(" from u_progetti_risorse");
		sql.append(" where mese = ?");
		sql.append(" and matricola = ?");

		return getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {

				int i = 1;
				pstm.setInt(i++, month);
				pstm.setString(i, badgeNumber);
			}
		}, new RowMapper<WorkloadDetailBean>() {
			public WorkloadDetailBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				
				WorkloadDetailBean bean = new WorkloadDetailBean();
				bean.setBadgeNumber(rs.getString("matricola"));
				bean.setName(rs.getString("nome_risorsa"));
				bean.setSurname(rs.getString("cognome_risorsa"));
				bean.setDescProgetto(rs.getString("desc_progetto"));
				bean.setCons1(rs.getInt("consolidato_1"));
				bean.setCons2(rs.getInt("consolidato_2"));
				bean.setCons3(rs.getInt("consolidato_3"));
				bean.setProd1(rs.getInt("prodotto_1"));
				bean.setProd2(rs.getInt("prodotto_2"));
				bean.setProd3(rs.getInt("prodotto_3"));
				bean.setUsernameIns(rs.getString("utente_ins"));
				
				return bean;
			}
		});
	}

}
