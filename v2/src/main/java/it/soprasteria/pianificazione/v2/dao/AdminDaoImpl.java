package it.soprasteria.pianificazione.v2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import it.soprasteria.pianificazione.v2.bean.PmBean;
import it.soprasteria.pianificazione.v2.bean.V2Bean;
import it.soprasteria.pianificazione.v2.util.DateUtil;
import it.soprasteria.pianificazione.v2.util.V2StatusKeys;

@Qualifier
public class AdminDaoImpl extends JdbcDaoSupport implements AdminDao {

	private static final Logger LOG = Logger.getLogger(AdminDaoImpl.class);

/*
	@Override
	public List<PmBean> verifyStatus(final Integer month, final AdminDao dao) {

		List<PmBean> result = new ArrayList<>();

		final List<PmBean> userList = dao.verifyV2();

		for (final PmBean user : userList) {
			
			StringBuilder sb = new StringBuilder();

			sb.append("SELECT business_unit, editable");
			sb.append(" FROM v2 ");
			sb.append(" WHERE id_user = ? AND mese = ?");

			List<Integer[]> statusList = getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					int i = 1;
					ps.setString(i++, user.getUsername());
					ps.setInt(i, month);
				}
			}, new RowMapper<Integer[]>() {
				@Override
				public Integer[] mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new Integer[]{rs.getInt("business_unit"), rs.getInt("editable")};
				}

			});
			
			StringBuilder stato = new StringBuilder();
			if (statusList != null && !statusList.isEmpty()) {
				
				for (Integer[] status : statusList) {
					
					stato.append(getStato(status));

					stato.append(" ");
				}
			}
			if (stato.length() == 0) {
				stato.append("Da Aprire");
			}
			user.setStato(stato.toString());
			
			result.add(user);
		}

		return result;
	}
	
	private String getStato(Integer[] status) {
		
		if (status[1].intValue() == V2StatusKeys.OPEN) {
			return status[0] + ": Da Validare";
		} else if (status[1].intValue() == V2StatusKeys.VALIDATE) {
			return status[0] + ": Validato";
		} else if (status[1].intValue() == V2StatusKeys.CLOSE) {
			return status[0] + ": Chiuso";
		}
		return "";
	}
	*/
	
	@Override
	public List<Integer> getMonthsConfig() {
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT mese");
		sb.append(" FROM v2_config");
		sb.append(" ORDER BY mese");

		return getJdbcTemplate().query(sb.toString(), new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNumb) throws SQLException {
				Integer mese = rs.getInt("mese");
				return mese;
			}
		});
	}

	@Override
	public void addNextConfigMonth(final Integer lastMonth) {

		StringBuilder insertSql = new StringBuilder();
		insertSql.append(" INSERT INTO v2_config (mese, enable)");
		insertSql.append(" VALUES (?, ?)");

		int nextMonth = DateUtil.nextMonth(lastMonth);

		Object[] params = new Object[] { nextMonth, V2StatusKeys.OPEN };
		int[] types = new int[] { Types.INTEGER, Types.INTEGER };
		getJdbcTemplate().update(insertSql.toString(), params, types);

	}

	@Override
	public void updateEditable(final String user, final int month) {

		final StringBuilder sb = new StringBuilder();
		sb.append("UPDATE v2");
		sb.append(" SET editable= ?");
		sb.append(" WHERE id_user = ?");
		sb.append(" AND mese = ?");
		getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				int i = 1;
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				ps.setInt(i++, V2StatusKeys.OPEN);
				ps.setString(i++, user);
				ps.setInt(i, month);
				return ps;
			}
		});
	}
	
	@Override
	public void updateMonthsStatus(final int month, final int enable) {

		final StringBuilder sb = new StringBuilder();
		sb.append("UPDATE v2");
		sb.append(" SET editable= ?");
		sb.append(" WHERE mese = ?");
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				int i = 1;
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				ps.setInt(i++, enable);
				ps.setInt(i, month);
				return ps;
			}
		});
	}

	@Override
	public void updateV2ConfigStatus(final int month, final int enable) {

		final StringBuilder sb = new StringBuilder();
		sb.append("UPDATE v2_config");
		sb.append(" SET enable= ?");
		sb.append(" WHERE mese = ?");

		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				int i = 1;
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				ps.setInt(i++, enable);
				ps.setInt(i, month);
				return ps;
			}
		});
	}

	@Override
	public List<V2Bean> getV2Config() {
		List<V2Bean> v2List = getJdbcTemplate().query("SELECT * FROM v2_config order by mese desc", new RowMapper<V2Bean>() {
			public V2Bean mapRow(ResultSet rs, int rowNum) throws SQLException {

				V2Bean bean = new V2Bean();

				bean.setMonth(rs.getInt("mese"));
				bean.setUser("");
				bean.setStato(rs.getInt("enable"));

				return bean;

			}
		});

		return v2List;
	}

}