package it.soprasteria.pianificazione.v2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
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
import it.soprasteria.pianificazione.v2.util.V2StatusKeys;

@Qualifier
public class DaoImpl extends JdbcDaoSupport implements Dao {

	private static final Logger LOG = Logger.getLogger(DaoImpl.class);

	@Override
	public List<EmployeeBean> getAllEmployees() {
		List<EmployeeBean> empl = getJdbcTemplate().query("SELECT * FROM risorse", new RowMapper<EmployeeBean>() {
			public EmployeeBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				EmployeeBean emplo = new EmployeeBean();
				emplo.setName(rs.getString("nome"));
				emplo.setSurname(rs.getString("cognome"));
				emplo.setBadgeNumber(rs.getString("matricola"));
				// TODO valorizzare utente_ins, data_ins				
				return emplo;
			}
		});
		return empl;
	}

	@Override
	public List<ProjectBean> getAllProject() {
		List<ProjectBean> prog = getJdbcTemplate().query("SELECT * FROM progetti", new RowMapper<ProjectBean>() {
			public ProjectBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				ProjectBean proj = new ProjectBean();
				enrichProjectBean(rs, proj);
				return proj;
			}

		});
		return prog;
	}

	@Override
	public List<ProjectBean> findProjectsByBusinessUnit(final int businessUnit) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM progetti");
		sql.append(" WHERE business_unit = ?");

		List<ProjectBean> prog = getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {

				int i = 1;
				pstm.setInt(i, businessUnit);
			}
		}, new RowMapper<ProjectBean>() {
			public ProjectBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				ProjectBean proj = new ProjectBean();
				enrichProjectBean(rs, proj);
				return proj;
			}
		});
		return prog;
	}

	@Override
	public List<RecordV2Bean> getV2(final int month, final int businessUnit, final String user) {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT *");
		sb.append(" FROM u_progetti_risorse");
		sb.append(" WHERE mese = ?");
		sb.append(" AND business_unit = ?");
		if (user != null) {
			sb.append(" AND id_user = ?");
		}

		return getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {

				int i = 1;
				pstm.setInt(i++, month);
				pstm.setInt(i++, businessUnit);
				if (user != null) {
					pstm.setString(i, user);
				}
			}
		}, new RowMapper<RecordV2Bean>() {
			@Override
			public RecordV2Bean mapRow(ResultSet rs, int rowNum) throws SQLException {

				RecordV2Bean bean = new RecordV2Bean();
				enrichRecordV2Bean(bean, rs);

				return bean;
			}

		});
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
				b.setBadgeNumber(rs.getString("matricola"));
				b.setName(rs.getString("nome"));
				b.setSurname(rs.getString("cognome"));
				b.setBusinessUnit(rs.getString("business_unit"));
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

				enrichRecordV2Bean(bean, rs);

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
		sb.append(" ,costo=?");
		sb.append(" ,tariffa=?");
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
				ps.setInt(i++, rec.getCost());
				ps.setInt(i++, rec.getPrice());
				ps.setString(i++, rec.getNome());
				ps.setString(i++, rec.getCognome());
				ps.setString(i++, rec.getUtenteMod());
				ps.setTimestamp(i++, new java.sql.Timestamp(new Date().getTime()));
				ps.setLong(i, rec.getIdRecord());

				return ps;
			}
		});
	}

	@Override
	public void insert(final RecordV2Bean rec) {

		final StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO u_progetti_risorse (mese,id_progetto,costo,consolidato_1,consolidato_2,consolidato_3,matricola,id_user,nome_risorsa,cognome_risorsa,desc_progetto,desc_custom,attivit�,valuta,cliente,business_unit,utente_ins,data_ins)");
		sb.append("  VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				int i = 1;
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				ps.setInt(i++, rec.getMonth());
				ps.setLong(i++, rec.getIdProject() == null ? 0 : rec.getIdProject());
				ps.setInt(i++, rec.getCost());
				ps.setInt(i++, rec.getCons0());
				ps.setInt(i++, rec.getCons1());
				ps.setInt(i++, rec.getCons2());
				ps.setString(i++, rec.getBadgeNumber());
				ps.setString(i++, rec.getUtenteIns());
				ps.setString(i++, rec.getNome());
				ps.setString(i++, rec.getCognome());
				ps.setString(i++, rec.getProjectDesc());
				ps.setString(i++, rec.getCustomDesc());
				ps.setString(i++, rec.getActivityType());
				ps.setString(i++, "EUR");
				ps.setString(i++, rec.getCustomer());
				ps.setInt(i++, rec.getBusinessUnit());
				ps.setString(i++, rec.getUtenteIns());

				Date now = new Date();
				ps.setTimestamp(i, new java.sql.Timestamp(now.getTime()));

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
	public void deleteEmployeesByBusinessUnit(String businessUnit) {

		StringBuilder sb = new StringBuilder();

		sb.append("DELETE FROM risorse where business_unit = ?");

		getJdbcTemplate().update(sb.toString(), businessUnit);
	}

	
	@Override
	public void deleteAllProjects() {

		StringBuilder sb = new StringBuilder();

		sb.append("DELETE FROM progetti");

		getJdbcTemplate().update(sb.toString());
	}

	@Override
	public void persist(final String businessUnit, final List<EmployeeBean> list) {

		final StringBuilder sb = new StringBuilder();

		sb.append("INSERT INTO risorse(matricola, nome, cognome,utente_ins, data_ins, business_unit)");
		sb.append(" VALUES(?,?,?,?,?,?)");

		final Date now = new Date();

		getJdbcTemplate().batchUpdate(sb.toString(), new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm, int i) throws SQLException {

				EmployeeBean item = list.get(i);

				pstm.setString(1, item.getBadgeNumber());
				pstm.setString(2, item.getName());
				pstm.setString(3, item.getSurname());
				pstm.setString(4, item.getUtenteIns());
				pstm.setTimestamp(5, new java.sql.Timestamp(now.getTime()));
				pstm.setString(6, businessUnit);
			}

			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}

	@Override
	public void persistProject(final List<ProjectBean> list) {

		final StringBuilder sb = new StringBuilder();

		sb.append("INSERT INTO progetti(id_progetto, progetto, cliente,business_unit,utente_ins, data_ins)");
		sb.append(" VALUES(?,?,?,?,?,?)");

		final Date now = new Date();

		getJdbcTemplate().batchUpdate(sb.toString(), new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm, int i) throws SQLException {

				ProjectBean item = list.get(i);

				pstm.setLong(1, item.getIdProject());
				pstm.setString(2, item.getDescription());
				pstm.setString(3, item.getCustomer());
				pstm.setInt(4, item.getBusinessUnit());
				pstm.setString(5, item.getUtenteIns());
				pstm.setTimestamp(6, new java.sql.Timestamp(now.getTime()));
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
				ps.setLong(i, id);
				return ps;
			}
		});
	}

	@Override
	public void updateTable(final Long id, final String colname, final String value, final String username) {

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
				ps.setString(i++, value);
				ps.setString(i++, username);
				ps.setTimestamp(i++, new Timestamp(new Date().getTime()));
				ps.setLong(i, id);
				return ps;
			}
		});
	}

	@Override
	public List<Integer> getMonths(final String username) {

		StringBuilder sb = new StringBuilder();

		sb.append("SELECT mese");
		sb.append(" FROM v2");
		sb.append(" WHERE id_user = ?");
		sb.append(" ORDER BY mese");

		List<Integer> result = getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
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

	public void addProjectsResources(final String username, final int currentMonth, final int nextMonth) {

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
				pstm.setString(i, username);
			}
		}, new RowMapper<RecordV2Bean>() {
			@Override
			public RecordV2Bean mapRow(ResultSet rs, int rowNum) throws SQLException {

				RecordV2Bean bean = new RecordV2Bean();

				enrichRecordV2Bean(bean, rs);

				return bean;
			}
		});

		StringBuilder insertSql = new StringBuilder();
		insertSql.append("INSERT INTO u_progetti_risorse");
		insertSql.append(" (mese, id_progetto, matricola, consolidato_1, consolidato_2, consolidato_3, prodotto_1, prodotto_2, prodotto_3,");
		insertSql.append(" id_user, costo, tariffa, nome_risorsa, cognome_risorsa, valuta, cliente, desc_progetto, desc_custom, business_unit, attivit�, utente_ins, data_ins)");
		insertSql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

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
				pstm.setInt(i++, bean.getCost());
				pstm.setInt(i++, bean.getPrice());
				pstm.setString(i++, bean.getNome());
				pstm.setString(i++, bean.getCognome());
				pstm.setString(i++, bean.getCurrency());
				pstm.setString(i++, bean.getCustomer());
				pstm.setString(i++, bean.getProjectDesc());
				pstm.setString(i++, bean.getCustomDesc());
				pstm.setInt(i++, bean.getBusinessUnit());
				pstm.setString(i++, bean.getActivityType());
				pstm.setString(i++, username);
				pstm.setTimestamp(i, new java.sql.Timestamp(now.getTime()));
			}

			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}

	@Override
	public void createV2(final String username, final Integer lastMonth, final int businessUnit) {
		
		StringBuilder insertSql = new StringBuilder();
		insertSql.append(" INSERT INTO v2 (mese, id_user, editable, business_unit)");
		insertSql.append("VALUES (?, ?, ?, ?)");
		
		int nextMonth = DateUtil.nextMonth(lastMonth);
		int editable = V2StatusKeys.OPEN;
		
		Object[] params = new Object[] { nextMonth, username, editable, businessUnit };
		int[] types = new int[] { Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER };
		getJdbcTemplate().update(insertSql.toString(), params, types);
	}
	
	@Override
	public V2Bean findByMonth(final int month, final int businessUnit, final String username) {

		List<V2Bean> mesi = getJdbcTemplate().query("SELECT * FROM v2 WHERE id_user = ? AND mese = ? and business_unit = ?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {

				int i = 1;
				pstm.setString(i++, username);
				pstm.setInt(i++, month);
				pstm.setInt(i, businessUnit);
			}
		}, new RowMapper<V2Bean>() {
			public V2Bean mapRow(ResultSet rs, int rowNum) throws SQLException {

				V2Bean bean = new V2Bean();

				enrichV2Bean(rs, bean);

				return bean;
			}
		});

		return mesi.get(0);

	}

	@Override
	public List<V2Bean> findByUser(final String username) {

		List<V2Bean> list = getJdbcTemplate().query("SELECT * FROM v2 WHERE id_user = ? ORDER BY mese DESC, business_unit", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				pstm.setString(1, username);
			}
		}, new RowMapper<V2Bean>() {
			public V2Bean mapRow(ResultSet rs, int rowNum) throws SQLException {

				V2Bean bean = new V2Bean();

				enrichV2Bean(rs, bean);

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
	public UserBean findByUsername(final String username) {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT *");
		sb.append(" FROM users");
		sb.append(" WHERE username = ?");
		List<UserBean> userlist = getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				pstm.setString(1, username);
			}
		}, new RowMapper<UserBean>() {
			@Override
			public UserBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				UserBean user = new UserBean();
				enrichUserBean(user, rs);
				return user;
			}
		});

		if (userlist.isEmpty()) {
			return null;
		}
		return userlist.get(0);
	}
	
	public List<String> findBuByDivisione(final String divisione) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT *");
		sb.append(" FROM divisioni");
		sb.append(" WHERE cod_divisione = ?");
		return getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				pstm.setString(1, divisione);
			}
		}, new RowMapper<String>() {
			@Override
			public String mapRow(ResultSet rs, int rowNumb) throws SQLException {
				return rs.getString("bu");
			}
		});
		
	}
	
	private void enrichUserBean(UserBean user, ResultSet rs) throws SQLException {
		user.setUsername(rs.getString("username"));
		user.setRealUsername(rs.getString("username"));
		user.setName(rs.getString("nome"));
		user.setSurname(rs.getString("cognome"));
		user.setProfilo(rs.getString("profilo"));
		user.setFirstlogin(rs.getInt("first_login"));
		user.setDivisione(rs.getString("divisione"));
	}

	private void enrichRecordV2Bean(RecordV2Bean bean, ResultSet rs) throws SQLException {
		bean.setMonth(rs.getInt("mese"));
		bean.setIdRecord(rs.getLong("id_unione"));
		bean.setIdProject(rs.getLong("id_progetto"));
		bean.setBadgeNumber(rs.getString("matricola"));
		bean.setCons0(rs.getInt("consolidato_1"));
		bean.setProd0(rs.getInt("prodotto_1"));
		bean.setCons1(rs.getInt("consolidato_2"));
		bean.setProd1(rs.getInt("prodotto_2"));
		bean.setCons2(rs.getInt("consolidato_3"));
		bean.setProd2(rs.getInt("prodotto_3"));
		bean.setCost(rs.getInt("costo"));
		bean.setPrice(rs.getInt("tariffa"));
		bean.setCustomer(rs.getString("cliente"));
		bean.setNome(rs.getString("nome_risorsa"));
		bean.setCognome(rs.getString("cognome_risorsa"));
		bean.setCurrency(rs.getString("valuta"));
		bean.setProjectDesc(rs.getString("desc_progetto"));
		bean.setCustomDesc(rs.getString("desc_custom"));
		bean.setBusinessUnit(rs.getInt("business_unit"));
		bean.setIdUser(rs.getString("id_user"));
		bean.setActivityType(rs.getString("attivit�"));
		bean.setUtenteIns(rs.getString("utente_ins"));
		Timestamp timestampIns = rs.getTimestamp("data_ins");
		bean.setDataIns(timestampIns == null ? null : new java.util.Date(timestampIns.getTime()));
		bean.setUtenteMod(rs.getString("utente_mod"));
		Timestamp timestampMod = rs.getTimestamp("data_mod");
		bean.setDataMod(timestampMod == null ? null : new java.util.Date(timestampMod.getTime()));
	}

	private void enrichV2Bean(ResultSet rs, V2Bean bean) throws SQLException {

		bean.setMonth(rs.getInt("mese"));
		bean.setUser(rs.getString("id_user"));
		bean.setStato(rs.getInt("editable"));
		bean.setBusinessUnit(rs.getInt("business_unit"));
	}

	private void enrichProjectBean(ResultSet rs, ProjectBean proj) throws SQLException {
		proj.setIdProject(rs.getLong("id_progetto"));
		proj.setDescription(rs.getString("progetto"));
		proj.setCustomer(rs.getString("cliente"));
		proj.setCurrency(rs.getString("valuta"));
		proj.setType(rs.getString("attivit�"));
		proj.setBusinessUnit(rs.getInt("business_unit"));
		// TODO valorizzare utente_ins, data_ins
	}

	@Override
	public int getTotDays(final int month) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT tot_giornate");
		sql.append(" FROM calendar_config");
		sql.append(" WHERE mese = ?");

		List<Integer> totGiornate = getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				int i = 1;
				ps.setInt(i, month);
			}
		}, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				int tot = rs.getInt("tot_giornate");
				return tot;
			}
		});

		return totGiornate.get(0);
	}

	@Override
	public int getConsDays(final String badgeNumber, final String colname, final int mese) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT SUM(" + colname + ") AS " + colname);
		sql.append(" FROM u_progetti_risorse");
		sql.append(" WHERE matricola = ?");
		sql.append(" AND mese = ?");

		List<Integer> consDays = getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				int i = 1;
				ps.setString(i++, badgeNumber);
				ps.setInt(i, mese);
			}
		}, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				int tot = rs.getInt(colname);
				return tot;
			}
		});

		return consDays.get(0);
	}

	@Override
	public void changeStatus(final String user, final int month, final int businessUnit, final int status) {

		final StringBuilder sb = new StringBuilder();
		
		// TODO aggiornare info utente e date
		sb.append("UPDATE v2");
		sb.append(" SET editable= ?");
		sb.append(" WHERE id_user = ?");
		sb.append(" AND mese = ?");
		sb.append(" AND business_unit = ?");
		
		getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				int i = 1;
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				ps.setInt(i++, status);
				ps.setString(i++, user);
				ps.setInt(i++, month);
				ps.setInt(i, businessUnit);
				
				return ps;
			}
		});
	}
	
	@Override
	public int checkChangePassword(final String userid){
		
		StringBuilder sb = new StringBuilder();
			
		sb.append("SELECT first_login");
		sb.append(" FROM users");
		sb.append(" WHERE username = ?");
		
		List<Integer> firstLog= getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				
				ps.setString(1, userid);
				
			}
		},new RowMapper<Integer>(){
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				int firstlogin;
				
				firstlogin = rs.getInt("first_login");
				LOG.debug("FIRSTLOGIN " + firstlogin);
				return firstlogin;
			}
		});
		
		LOG.debug("LOGIN STATUS : "+ firstLog.get(0) );
		
		return firstLog.get(0);
	}
	
	@Override
	public void produceAll(final String user, final int month, final int businessUnit) {

		final StringBuilder sb = new StringBuilder();
		
		// TODO aggiornare info utente e date
		sb.append("UPDATE u_progetti_risorse");
		sb.append(" SET prodotto_1= consolidato_1");
		sb.append(" , prodotto_2= consolidato_2");
		sb.append(" , prodotto_3= consolidato_3");
		sb.append(" WHERE id_user = ?");
		sb.append(" AND mese = ?");
		sb.append(" AND business_unit = ?");
		
		getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				int i = 1;
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				ps.setString(i++, user);
				ps.setInt(i++, month);
				ps.setInt(i, businessUnit);
				
				return ps;
			}
		});
	}

}