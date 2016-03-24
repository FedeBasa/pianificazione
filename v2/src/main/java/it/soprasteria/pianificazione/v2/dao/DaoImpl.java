package it.soprasteria.pianificazione.v2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.bean.ProjectBean;
import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;
import it.soprasteria.pianificazione.v2.bean.UserBean;
import it.soprasteria.pianificazione.v2.bean.V2Bean;
import it.soprasteria.pianificazione.v2.bean.V2ConfigBean;
import it.soprasteria.pianificazione.v2.util.DateUtil;
import it.soprasteria.pianificazione.v2.util.SessionHelper;

public class DaoImpl extends JdbcDaoSupport implements Dao {
	
	private static final Logger LOG = Logger.getLogger(DaoImpl.class);

	@Override
	public List<EmployeeBean> getAllEmployees() {
		List<EmployeeBean> empl = getJdbcTemplate().query("SELECT * FROM risorse", new RowMapper<EmployeeBean>() {
			public EmployeeBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				EmployeeBean emplo = new EmployeeBean();
				emplo.setName(rs.getString(2));
				emplo.setSurname(rs.getString(3));
				emplo.setBadgeNumber(Integer.toString(rs.getInt("matricola")));
				return emplo;
			}
		});
		return empl;
	}

	@Override
	public List<ProjectBean> getAllProject() {
		List<ProjectBean> prog = getJdbcTemplate().query("SELECT * FROM progetti",  new RowMapper<ProjectBean>() {
			public ProjectBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				ProjectBean proj = new ProjectBean();
				proj.setIdProject(rs.getLong("id_progetto"));
				proj.setDescription(rs.getString("progetto"));
				proj.setCustomer(rs.getString("cliente"));
				proj.setCurrency(rs.getString("valuta"));
				proj.setType(rs.getString("attivit�"));
				return proj;
			}
		});
		return prog;
	}

	@Override
	public List<RecordV2Bean> getV2(final int month, final String user) {
		List<RecordV2Bean> result = new ArrayList<RecordV2Bean>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT *");
		sb.append(" FROM u_progetti_risorse");
		sb.append(" WHERE mese = ?");
		sb.append(" AND id_user = ?");

		result = getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				pstm.setInt(1, month);
				pstm.setString(2, user);
			}
		}, new RowMapper<RecordV2Bean>() {
			@Override
			public RecordV2Bean mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				RecordV2Bean bean = new RecordV2Bean();
				enrichRecordV2Bean(bean, rs, rowNum);
				
				return bean;
			}

		});
		return result;
	}

	@Override
	public EmployeeBean getEmployee(final String id) {

		List<EmployeeBean> list = getJdbcTemplate().query("SELECT * FROM risorse WHERE matricola = ?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				pstm.setString(1, id);
			}
		}, new RowMapper<EmployeeBean>() {
			public EmployeeBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				EmployeeBean b = new EmployeeBean();
				b.setBadgeNumber(rs.getString(1));
				b.setName(rs.getString(2));
				b.setSurname(rs.getString(3));
				return b;
			}
		});
		
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public ProjectBean getProject(final long id) {

		List<ProjectBean> list = getJdbcTemplate().query("SELECT * FROM progetti WHERE  id_progetto = ?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				pstm.setLong(1, id);
			}
		}, new RowMapper<ProjectBean>() {
			@Override
			public ProjectBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				ProjectBean p = new ProjectBean();
				p.setDescription(rs.getString("progetto"));
				p.setCustomer(rs.getString("cliente"));
				p.setCurrency(rs.getString("valuta"));
				p.setType(rs.getString("attivit�"));
				p.setBusinessUnit(rs.getInt("business_unit"));
				return p;
			}
		});
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);

	}

	@Override
	public RecordV2Bean getRecord(final long id) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT *");
		sb.append(" FROM u_progetti_risorse");
		sb.append(" WHERE id_unione  = ?");

		List<RecordV2Bean> list = getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				pstm.setLong(1, id);
			}
		}, new RowMapper<RecordV2Bean>() {
			public RecordV2Bean mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				RecordV2Bean bean = new RecordV2Bean();
				
				enrichRecordV2Bean(bean, rs, rowNum);
				
				return bean;

			};
		});
		
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public void update(final RecordV2Bean rec) {

		final StringBuilder sb = new StringBuilder();

		sb.append("UPDATE u_progetti_risorse");
		sb.append(" SET matricola=?");
		sb.append(" ,id_progetto=?");
		sb.append(" ,nome_risorsa= ?");
		sb.append(" ,cognome_risorsa= ?");
		sb.append(" ,utente_mod= ?");
		sb.append(" ,data_mod= ?");
		sb.append(" WHERE id_unione = ?");
		getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				int i = 1;
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				ps.setString(i++, rec.getBadgeNumber());
				ps.setLong(i++, rec.getIdProject() == null ? 0 : rec.getIdProject());
				ps.setString(i++, rec.getNome());
				ps.setString(i++, rec.getCognome());
				ps.setString(i++, rec.getUserMod());
				ps.setTimestamp(i++, new java.sql.Timestamp(new Date().getTime()));
				ps.setLong(i++, rec.getIdRecord());
				
				return ps;
			}
		});
	}

	@Override
	public void insert(final RecordV2Bean rec) {
		
		final StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO u_progetti_risorse (mese,id_progetto,matricola,id_user,nome_risorsa,cognome_risorsa,desc_progetto,attivit�,valuta,cliente,business_unit,utente_ins,data_ins)");
		sb.append("  VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				int i = 1;
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				ps.setInt(i++, rec.getMonth());
				ps.setLong(i++, rec.getIdProject() == null ? 0 : rec.getIdProject());
				ps.setInt(i++, Integer.parseInt(rec.getBadgeNumber()));
				ps.setString(i++, rec.getUserIns());
				ps.setString(i++, rec.getNome());
				ps.setString(i++, rec.getCognome());
				ps.setString(i++, rec.getProjectDesc());
				ps.setString(i++, rec.getActivityType());
				ps.setString(i++, rec.getCurrency());
				ps.setString(i++, rec.getCustomer());
				ps.setInt(i++, rec.getBusinessUnit());
				ps.setString(i++, rec.getUserIns());
				
			    Date now = new Date();
				ps.setTimestamp(i++, new java.sql.Timestamp(now.getTime()));
				
				return ps;
			}
		});
	}

	@Override
	public void delete(final long id) {
		
		final StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM u_progetti_risorse");
		sb.append(" WHERE id_unione = ?");
		
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				ps.setLong(1, id);
				return ps;
			}
		});

	}

	@Override
	public void deleteAllEmployees() {

		StringBuilder sb = new StringBuilder();

		sb.append("DELETE FROM risorse");

		getJdbcTemplate().update(sb.toString());
	}

	@Override
	public void persist(final List<EmployeeBean> list) {

		final StringBuilder sb = new StringBuilder();

		sb.append("INSERT INTO risorse(matricola, nome, cognome)");
		sb.append(" VALUES(?,?,?)");

		getJdbcTemplate().batchUpdate(sb.toString(), new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm, int i) throws SQLException {

				EmployeeBean item = list.get(i);

				pstm.setString(1, item.getBadgeNumber());
				pstm.setString(2, item.getName());
				pstm.setString(3, item.getSurname());
			}

			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}

	@Override
	public void updateTable(final Long id, final String colname, final Integer value, final String username) {

		final StringBuilder sb = new StringBuilder();

		sb.append("UPDATE u_progetti_risorse");
		sb.append(" SET ").append(colname).append(" = ?");
		sb.append(" , utente_mod = ?");
		sb.append(" , data_mod = ?");
		sb.append(" WHERE id_unione = ?");

		// TODO
		// fare check sul valore di ritorno
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				int i = 1;
				ps.setInt(i++, value);
				ps.setString(i++, username);
				ps.setTimestamp(i++, new Timestamp(new Date().getTime()));
				ps.setLong(i++, id);
				return ps;
			}
		});
	}

	@Override
	public List<Integer> getMonths(final String username) {
		List<Integer> result = new ArrayList<Integer>();
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT mese");
		sb.append(" FROM v2");
		sb.append(" WHERE id_user = ?");
		sb.append(" ORDER BY mese");

		result = getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				pstm.setString(1, username);
			}
		}, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNumb) throws SQLException {
				Integer mese = rs.getInt("mese");
				return mese;
			}
		});
		return result;
	}

	public List<Integer> getMonthsConfig() {
		List<Integer> result = new ArrayList<Integer>();
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT mese");
		sb.append(" FROM v2_config");
		sb.append(" WHERE enable = 1");
		sb.append(" ORDER BY mese");

		result = getJdbcTemplate().query(sb.toString(), new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNumb) throws SQLException {
				Integer mese = rs.getInt("mese");
				return mese;
			}
		});
		return result;
	}

	private void addProjectsResources(final String username, final int currentMonth, final int nextMonth) {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM u_progetti_risorse");
		sb.append(" WHERE mese = ?");
		sb.append(" AND id_user = ?");
		sb.append(" ORDER BY mese desc");

		final List<RecordV2Bean> list = getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				int i = 1;
				pstm.setInt(i++, currentMonth);
				pstm.setString(i++, username);
			}
		}, new RowMapper<RecordV2Bean>() {
			@Override
			public RecordV2Bean mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				RecordV2Bean bean = new RecordV2Bean();
				
				enrichRecordV2Bean(bean, rs, rowNum);
				
				return bean;
			}
		});

		StringBuilder insertSql = new StringBuilder();
		insertSql.append("INSERT INTO u_progetti_risorse");
		insertSql.append(" (mese, id_progetto, matricola, consolidato_1, consolidato_2, consolidato_3, prodotto_1, prodotto_2, prodotto_3,");
		insertSql.append(" id_user, tariffa, nome_risorsa, cognome_risorsa, valuta, cliente, desc_progetto, business_unit, attivit�, utente_ins, data_ins)");
		insertSql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

		final Date now = new Date();
		
		getJdbcTemplate().batchUpdate(insertSql.toString(), new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement pstm, int index) throws SQLException {
				
				RecordV2Bean bean = list.get(index);
				
				int i = 1;
				pstm.setInt(i++, nextMonth);
				pstm.setLong(i++, bean.getIdProject());
				pstm.setString(i++, bean.getBadgeNumber());
				pstm.setInt(i++, bean.getCons1());
				pstm.setInt(i++, bean.getCons2());
				pstm.setInt(i++, 0);
				pstm.setInt(i++, bean.getProd1());
				pstm.setInt(i++, bean.getProd2());
				pstm.setLong(i++, 0);
				pstm.setString(i++, SessionHelper.getUser().getUsername());
				pstm.setInt(i++, bean.getPrice());
				pstm.setString(i++, bean.getNome());
				pstm.setString(i++, bean.getCognome());
				pstm.setString(i++, bean.getCurrency());
				pstm.setString(i++, bean.getCustomer());
				pstm.setString(i++, bean.getProjectDesc());
				pstm.setInt(i++, bean.getBusinessUnit());
				pstm.setString(i++, bean.getActivityType());
				pstm.setString(i++, username);
				pstm.setTimestamp(i++, new java.sql.Timestamp(now.getTime()));
			}
			
			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}

	@Override
	public void addNextMonth(final String username, final Integer lastMonth) {

		List<V2Bean> result = new ArrayList<V2Bean>();
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT *");
		sb.append(" FROM v2");
		sb.append(" WHERE mese = ?");
		sb.append(" AND id_user = ?");
		sb.append(" ORDER BY mese desc");

		result = getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				int i = 1;
				pstm.setInt(i++, lastMonth);
				pstm.setString(i++, username);
			}
		}, new RowMapper<V2Bean>() {
			@Override
			public V2Bean mapRow(ResultSet rs, int rowNum) throws SQLException {

				V2Bean bean = new V2Bean();
				
				enrichV2Bean(rs, bean, rowNum);
				
				return bean;
			}

		});

		StringBuilder insertSql = new StringBuilder();
		insertSql.append(" INSERT INTO v2 (mese, id_user, editable)");
		insertSql.append(" VALUES (?, ?, ?)");

		int nextMonth = 0;
		int currentMonth = 0;
		
		V2Bean v2 = result.get(0);
		currentMonth = v2.getMonth();
		nextMonth = DateUtil.nextMonth(v2.getMonth());
		
		LOG.info("######### [" + currentMonth + "] [" + nextMonth + "]");
		
		Object[] params = new Object[] { nextMonth, username, 1 };
		int[] types = new int[] { Types.INTEGER, Types.VARCHAR, Types.INTEGER };
		getJdbcTemplate().update(insertSql.toString(), params, types);

		addProjectsResources(username, currentMonth, nextMonth);
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
				ps.setInt(i++, 1);
				ps.setString(i++, user);
				ps.setInt(i++, month);
				return ps;
			}
		});
	}

	@Override
	public V2Bean findByMonth(final int month, final String username) {

		List<V2Bean> mesi = getJdbcTemplate().query("SELECT * FROM v2 WHERE id_user = ? AND mese = ?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				pstm.setString(1, username);
				pstm.setInt(2, month);
			}
		}, new RowMapper<V2Bean>() {
			public V2Bean mapRow(ResultSet rs, int rowNum) throws SQLException {

				V2Bean bean = new V2Bean();

				enrichV2Bean(rs, bean, rowNum);
				
				return bean;
			}
		});

		return mesi.get(0);

	}

	@Override
	public List<V2Bean> findByUser(final String username) {

		List<V2Bean> list = getJdbcTemplate().query("SELECT * FROM v2 WHERE id_user = ? ORDER BY MESE", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				pstm.setString(1, username);
			}
		}, new RowMapper<V2Bean>() {
			public V2Bean mapRow(ResultSet rs, int rowNum) throws SQLException {

				V2Bean bean = new V2Bean();

				enrichV2Bean(rs, bean, rowNum);
				
				return bean;
			}
		});

		return list;

	}

	public V2ConfigBean findConfigByMonth(final int month) {
		
		List<V2ConfigBean> list = getJdbcTemplate().query("SELECT * FROM v2_config WHERE mese = ?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				pstm.setInt(1, month);
			}
		}, new RowMapper<V2ConfigBean>() {
			public V2ConfigBean mapRow(ResultSet rs, int rowNumb) throws SQLException {

				V2ConfigBean result = new V2ConfigBean();

				result.setIdConfig(rs.getInt("id_config"));
				result.setMonth(rs.getInt("mese"));
				result.setEnable(rs.getInt("enable") == 1 ? true : false);

				return result;
			}
		});
		
		if (list.isEmpty()) {
			return null;
		}
		
		return list.get(0);
	}

	@Override
	public UserBean login(final String username ,final String password) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT *");
		sb.append(" FROM users");
		sb.append(" WHERE username = ? AND password = ?");
		List<UserBean> userlist = getJdbcTemplate().query(sb.toString(),new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				pstm.setString(1, username);
				pstm.setString(2, password);
			}
		} ,new RowMapper<UserBean>(){
			@Override
			public UserBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				UserBean user = UserBean.build(rs.getString("profilo"), rs.getString("nome"), rs.getString("cognome"), rs.getString("profilo"));
				return user;
			}
		});
		
		if (userlist.isEmpty()) {
			return null;
		}
		return userlist.get(0);
	}

	private void enrichRecordV2Bean(RecordV2Bean bean, ResultSet rs, int rowNum) throws SQLException {
		bean.setMonth(rs.getInt("mese"));
		bean.setIdRecord(rs.getLong("id_unione"));
		bean.setIdProject(rs.getLong("id_progetto"));
		bean.setBadgeNumber(Integer.toString(rs.getInt("matricola")));
		bean.setCons0(rs.getInt("consolidato_1"));
		bean.setProd0(rs.getInt("prodotto_1"));
		bean.setCons1(rs.getInt("consolidato_2"));
		bean.setProd1(rs.getInt("prodotto_2"));
		bean.setCons2(rs.getInt("consolidato_3"));
		bean.setProd2(rs.getInt("prodotto_3"));
		bean.setPrice(rs.getInt("tariffa"));
		bean.setCustomer(rs.getString("cliente"));
		bean.setNome(rs.getString("nome_risorsa"));
		bean.setCognome(rs.getString("cognome_risorsa"));
		bean.setCurrency(rs.getString("valuta"));
		bean.setProjectDesc(rs.getString("desc_progetto"));
		bean.setBusinessUnit(rs.getInt("business_unit"));
		bean.setId_user(rs.getString("id_user"));
		bean.setActivityType(rs.getString("attivit�"));
		bean.setUserIns(rs.getString("utente_ins"));
		Timestamp timestampIns = rs.getTimestamp("data_ins");
		bean.setDataIns(timestampIns == null ? null : new java.util.Date(timestampIns.getTime()));
		bean.setUserMod(rs.getString("utente_mod"));
		Timestamp timestampMod = rs.getTimestamp("data_mod");
		bean.setDataMod(timestampMod == null ? null : new java.util.Date(timestampMod.getTime()));
	}

	private void enrichV2Bean(ResultSet rs, V2Bean bean, int rowNum) throws SQLException {
		
		bean.setMonth(rs.getInt("mese"));
		bean.setUser(rs.getString("id_user"));
		bean.setEditable(rs.getInt("editable") == 1 ? true : false);
	}
	/*
	@Override
	public List<V2Bean> getV2ToApprove(final String username) {
		List<V2Bean> v2List = getJdbcTemplate().query("SELECT * FROM v2 WHERE id_user = ?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				pstm.setString(1, username);
			}
		}, new RowMapper<V2Bean>() {
			public V2Bean mapRow(ResultSet rs, int rowNum) throws SQLException {

				V2Bean bean = new V2Bean();

				enrichV2Bean(rs, bean, rowNum);
				
				return bean;
				
			}
		});
		
		return v2List;
	}
	*/
	@Override
	public List<V2Bean> getV2Config() {
		List<V2Bean> v2List = getJdbcTemplate().query("SELECT * FROM v2_config", new RowMapper<V2Bean>() {
			public V2Bean mapRow(ResultSet rs, int rowNum) throws SQLException {

				V2Bean bean = new V2Bean();

				bean.setMonth(rs.getInt("mese"));
				bean.setUser("");
				bean.setEditable(rs.getInt("enable") == 1 ? true : false);
				
				return bean;
				
			}
		});
		
		return v2List;
	}
	
	@Override
	public void updateMonthsStatus(final int month, final boolean enable) {

		final StringBuilder sb = new StringBuilder();
		sb.append("UPDATE v2");
		sb.append(" SET editable= ?");
		sb.append(" WHERE mese = ?");
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				int i = 1;
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				ps.setInt(i++, enable ? 0 : 1);
				ps.setInt(i++, month);
				return ps;
			}
		});
	}
	
	@Override
	public void updateV2ConfigStatus(final int month, final boolean enable) {

		final StringBuilder sb = new StringBuilder();
		sb.append("UPDATE v2_config");
		sb.append(" SET enable= ?");
		sb.append(" WHERE mese = ?");
		
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				int i = 1;
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				ps.setInt(i++, enable ? 0 : 1);
				ps.setInt(i++, month);
				return ps;
			}
		});
	}

}