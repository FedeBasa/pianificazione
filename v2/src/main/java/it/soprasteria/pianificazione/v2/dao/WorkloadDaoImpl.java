package it.soprasteria.pianificazione.v2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import it.soprasteria.pianificazione.v2.bean.FerieBean;
import it.soprasteria.pianificazione.v2.bean.WorkloadBean;
import it.soprasteria.pianificazione.v2.bean.WorkloadDetailBean;

public class WorkloadDaoImpl extends JdbcDaoSupport implements WorkloadDao {

	private final int ZERO = 0;
	
	@Override
	public List<WorkloadBean> findWorkload(final int month, final String username) {

		StringBuilder sql = new StringBuilder();
		
		sql.append("select matricola, nome_risorsa, cognome_risorsa, sum(consolidato_1) work1, sum(prodotto_1) rec1, sum(consolidato_2) work2, sum(prodotto_2) rec2, sum(consolidato_3) work3, sum(prodotto_3) rec3");
		sql.append(" from u_progetti_risorse");
		sql.append(" where mese = ?");
		sql.append(" and matricola in (select matricola from u_progetti_risorse where mese = ?  and utente_ins = ?)");
		sql.append(" group by matricola, nome_risorsa, cognome_risorsa");

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
				bean.setName(rs.getString("nome_risorsa"));
				bean.setSurname(rs.getString("cognome_risorsa"));
				bean.setWork1(rs.getInt("work1"));
				bean.setRecognized1(rs.getInt("rec1"));
				bean.setWork2(rs.getInt("work2"));
				bean.setRecognized2(rs.getInt("rec2"));
				bean.setWork3(rs.getInt("work3"));
				bean.setRecognized3(rs.getInt("rec3"));
				bean.setFerie1(ZERO);
				bean.setFerie2(ZERO);
				bean.setFerie3(ZERO);
		
				List<FerieBean> ferieListBean = getFerie(month, rs.getString("matricola"));
				
				if(ferieListBean.size() > ZERO) {
					FerieBean ferieBean = ferieListBean.get(ZERO);
					
					bean.setFerie1(ferieBean.getFerie_1());
					bean.setFerie2(ferieBean.getFerie_2());
					bean.setFerie3(ferieBean.getFerie_3());
				}
				
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

	@Override
	public void updateFerieTable(final WorkloadBean workloadBean, final int month, final String colname, final Integer value, final String username) {
		
		List<FerieBean> listFerieBean = getFerie(month, workloadBean.getBadgeNumber());
		
		if(listFerieBean.size() != 0) {
					
			final StringBuilder sb = new StringBuilder();

			sb.append("UPDATE risorse_ferie");
			sb.append(" SET ").append(colname).append(" = ?");
			sb.append(" , utente_mod = ?");
			sb.append(" , data_mod = ?");

			getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
					PreparedStatement ps = conn.prepareStatement(sb.toString());
					int i = 1;
					ps.setInt(i++, value);
					ps.setString(i++, username);
					ps.setTimestamp(i++, new Timestamp(new Date().getTime()));
					return ps;
				}
			});
			
		}
		else {
			
			StringBuilder insertSql = new StringBuilder();
			insertSql.append(" INSERT INTO risorse_ferie (mese, matricola, nome, cognome, utente_ins, data_ins, ").append(colname).append(" )");
			insertSql.append("VALUES");
			insertSql.append(" (?, ?, ?, ?, ?, ?, ?)");
			
			Timestamp timestamp = new Timestamp(new Date().getTime());
			
			Object[] params = new Object[] { month, workloadBean.getBadgeNumber(), workloadBean.getName(), workloadBean.getSurname(), username, timestamp, value };
			int[] types = new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.INTEGER};
			getJdbcTemplate().update(insertSql.toString(), params, types);

		}
				
	}
	
	private List<FerieBean> getFerie(final int month, final String matricola) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("select ferie_1, ferie_2, ferie_3");
		sql.append(" from risorse_ferie");
		sql.append(" where mese = ?");
		sql.append(" and matricola = ?");
		
		return getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				
				int i = 1;
				pstm.setInt(i++, month);
				pstm.setString(i, matricola);
			}
		}, new RowMapper<FerieBean>() {
			public FerieBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				
				FerieBean bean = new FerieBean();
				bean.setFerie_1(rs.getInt("ferie_1"));
				bean.setFerie_2(rs.getInt("ferie_2"));
				bean.setFerie_3(rs.getInt("ferie_3"));
				
				return bean;
			}
		});
	}
	
}
