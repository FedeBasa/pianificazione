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
import it.soprasteria.pianificazione.v2.util.V2StatusKeys;

public class PmDaoImpl extends JdbcDaoSupport implements PmDao {

	private static final Logger LOG = Logger.getLogger(PmDaoImpl.class);

	@Override
	public List<PmBean> verifyV2() {

		StringBuilder sb = new StringBuilder();

		sb.append("SELECT *");
		sb.append(" FROM users");
		sb.append(" WHERE profilo = ?");

		List<PmBean> pmList = getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, "pm");
			}
		}, new RowMapper<PmBean>() {
			@Override
			public PmBean mapRow(ResultSet rs, int rowNum) throws SQLException {

				PmBean pm = new PmBean();

				pm.setPm(rs.getString("nome") + " " + rs.getString("cognome"));
				pm.setUsername(rs.getString("username"));
				return pm;
			}
		});

		return pmList;

	}

	@Override
	public List<PmBean> verifyStatus(final Integer month, final PmDao dao) {

		List<PmBean> result = new ArrayList<PmBean>();

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
					ps.setInt(i++, month);
				}
			}, new RowMapper<Integer[]>() {
				@Override
				public Integer[] mapRow(ResultSet rs, int rowNum) throws SQLException {
					return new Integer[]{rs.getInt("business_unit"), rs.getInt("editable")};
				}

			});
			
			StringBuilder stato = new StringBuilder();
			if (statusList != null) {
				
				if (!statusList.isEmpty()) {
					for (Integer[] status : statusList) {
						if (status[1].intValue() == V2StatusKeys.OPEN) {
							stato.append(status[0]).append(": Da Validare");
						} else if (status[1].intValue() == V2StatusKeys.VALIDATE) {
							stato.append(status[0]).append(": Validato");
						} else if (status[1].intValue() == V2StatusKeys.CLOSE) {
							stato.append(status[0]).append(": Chiuso");
						}
						stato.append(" ");
					}
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

}