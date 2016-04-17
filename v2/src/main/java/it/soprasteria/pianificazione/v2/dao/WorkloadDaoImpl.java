package it.soprasteria.pianificazione.v2.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import it.soprasteria.pianificazione.v2.bean.WorkloadBean;

public class WorkloadDaoImpl extends JdbcDaoSupport implements WorkloadDao {

	@Override
	public List<WorkloadBean> findWorkload(final int month, final String username) {

		StringBuilder sql = new StringBuilder();
		
		sql.append("select matricola, sum(consolidato_1) work1, sum(prodotto_1) rec1, sum(consolidato_2) work2, sum(prodotto_2) rec2, sum(consolidato_3) work3, sum(prodotto_3) rec3");
		sql.append(" from u_progetti_risorse");
		sql.append(" where mese = ?");
		sql.append(" and utente_ins = ?");
		sql.append(" group by matricola");

		List<WorkloadBean> result = getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {

				int i = 1;
				pstm.setInt(i++, month);
				pstm.setString(i++, username);
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

		return result;
	}

}
