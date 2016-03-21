package it.soprasteria.pianificazione.v2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
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
import it.soprasteria.pianificazione.v2.bean.V2Bean;
import it.soprasteria.pianificazione.v2.bean.V2ConfigBean;
import it.soprasteria.pianificazione.v2.util.DateUtil;

public class DaoImpl extends JdbcDaoSupport implements Dao {
	
	private static final Logger LOG = Logger.getLogger(DaoImpl.class);

	@Override
	public List<EmployeeBean> getAllEmployees() {
		List<EmployeeBean> empl = getJdbcTemplate().query("SELECT * FROM risorse", new RowMapper<EmployeeBean>() {
			public EmployeeBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				EmployeeBean emplo = new EmployeeBean();
				String nameSurname;
				emplo.setName(rs.getString(2));
				emplo.setSurname(rs.getString(3));
				nameSurname = emplo.getName() + " " + emplo.getSurname();
				emplo.setNameSurname(nameSurname);
				emplo.setBadgeNumber(Integer.toString(rs.getInt("matricola")));
				return emplo;
			}
		});
		return empl;
	}

	@Override
	public List<ProjectBean> getAllProject(final int businessUnit) {
		List<ProjectBean> prog = getJdbcTemplate().query("SELECT * FROM progetti WHERE business_unit = ?", new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
				pstm.setInt(1, businessUnit);
			}
		}, new RowMapper<ProjectBean>() {
			public ProjectBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				ProjectBean proj = new ProjectBean();
				proj.setIdProject(rs.getLong("id_progetto"));
				proj.setDescription(rs.getString("progetto"));
				proj.setCustomer(rs.getString("cliente"));
				proj.setCurrency(rs.getString("valuta"));
				proj.setType(rs.getString("attività"));
				proj.setBusinessUnit(businessUnit);
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
			public RecordV2Bean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				RecordV2Bean v2b = new RecordV2Bean();
				v2b.setMonth(month);
				v2b.setIdRecord(rs.getLong("id_unione"));
				v2b.setIdProject(rs.getLong("id_progetto"));
				v2b.setBadgeNumber(Integer.toString(rs.getInt("matricola")));
				v2b.setCons0(rs.getInt("consolidato_1"));
				v2b.setProd0(rs.getInt("prodotto_1"));
				v2b.setCons1(rs.getInt("consolidato_2"));
				v2b.setProd1(rs.getInt("prodotto_2"));
				v2b.setCons2(rs.getInt("consolidato_3"));
				v2b.setProd2(rs.getInt("prodotto_3"));
				v2b.setPrice(rs.getInt("tariffa"));
				v2b.setNome(rs.getString("nome_risorsa"));
				v2b.setCognome(rs.getString("cognome_risorsa"));
				v2b.setCurrency(rs.getString("valuta"));
				v2b.setProjectDesc(rs.getString("desc_progetto"));
				v2b.setEmployeeDesc(rs.getString("nome_risorsa")+" "+rs.getString("cognome_risorsa"));
				v2b.setBusinessUnit(rs.getInt("business_unit"));
				v2b.setActivityType(rs.getString("attività"));
				
				return v2b;
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
				p.setType(rs.getString("attività"));
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
			public RecordV2Bean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				RecordV2Bean rv = new RecordV2Bean();
				rv.setIdRecord(rs.getLong("id_unione"));
				rv.setMonth(rs.getInt("mese"));
				rv.setCons0(rs.getInt("consolidato_1"));
				rv.setProd0(rs.getInt("prodotto_1"));
				rv.setCons1(rs.getInt("consolidato_2"));
				rv.setProd1(rs.getInt("prodotto_2"));
				rv.setCons2(rs.getInt("consolidato_3"));
				rv.setProd2(rs.getInt("prodotto_3"));
				rv.setIdProject(rs.getLong("id_progetto"));
				rv.setBadgeNumber(Integer.toString(rs.getInt("matricola")));
				rv.setCurrency(rs.getString("valuta"));
				rv.setCustomer(rs.getString("cliente"));
				rv.setProjectDesc(rs.getString("desc_progetto"));
				rv.setBusinessUnit(rs.getInt("business_unit"));
				rv.setActivityType(rs.getString("attività"));
				rv.setPrice(rs.getInt("tariffa"));
				rv.setNome(rs.getString("nome_risorsa"));
				rv.setCognome(rs.getString("cognome_risorsa"));
				return rv;

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
				ps.setLong(i++, rec.getIdRecord());
				return ps;
			}
		});
	}

	@Override
	public void insert(final RecordV2Bean rec) {
		
		final StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO u_progetti_risorse (mese,id_progetto,matricola,id_user,nome_risorsa,cognome_risorsa,desc_progetto,attività,valuta,cliente)");
		sb.append("  VALUES(?,?,?,?,?,?,?,?,?,?)");
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				int i = 1;
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				ps.setInt(i++, rec.getMonth());
				ps.setLong(i++, rec.getIdProject() == null ? 0 : rec.getIdProject());
				ps.setInt(i++, Integer.parseInt(rec.getBadgeNumber()));
				// TODO
				// sistemare, cablato nome utente
				ps.setString(i++, "Admin");
				ps.setString(i++, rec.getNome());
				ps.setString(i++, rec.getCognome());
				ps.setString(i++, rec.getProjectDesc());
				ps.setString(i++, rec.getActivityType());
				ps.setString(i++, rec.getCurrency());
				ps.setString(i++, rec.getCustomer());
//				Date now = new Date();
//				ps.setTimestamp(i++, new java.sql.Timestamp(now.getTime()));
				
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
	public void updateTable(final Long id, final String colname, final Integer value) {

		final StringBuilder sb = new StringBuilder();

		sb.append("UPDATE  u_progetti_risorse");
		sb.append(" SET ").append(colname).append(" = ?");
		sb.append(" WHERE id_unione = ?");

		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				int i = 1;
				ps.setInt(i++, value);
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
			public RecordV2Bean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				RecordV2Bean rv = new RecordV2Bean();
				rv.setIdRecord(rs.getLong("id_unione"));
				rv.setMonth(rs.getInt("mese"));
				rv.setCons0(rs.getInt("consolidato_1"));
				rv.setProd0(rs.getInt("prodotto_1"));
				rv.setCons1(rs.getInt("consolidato_2"));
				rv.setProd1(rs.getInt("prodotto_2"));
				rv.setCons2(rs.getInt("consolidato_3"));
				rv.setProd2(rs.getInt("prodotto_3"));
				rv.setIdProject(rs.getLong("id_progetto"));
				rv.setBadgeNumber(Integer.toString(rs.getInt("matricola")));
				rv.setCurrency(rs.getString("valuta"));
				rv.setCustomer(rs.getString("cliente"));
				rv.setProjectDesc(rs.getString("desc_progetto"));
				rv.setBusinessUnit(rs.getInt("business_unit"));
				rv.setActivityType(rs.getString("attività"));
				rv.setPrice(rs.getInt("tariffa"));
				rv.setNome(rs.getString("nome_risorsa"));
				rv.setCognome(rs.getString("cognome_risorsa"));				
				return rv;
			}
		});

		StringBuilder insertSql = new StringBuilder();
		insertSql.append("INSERT INTO u_progetti_risorse");
		insertSql.append(" (mese, id_progetto, id_risorsa, consolidato_1, consolidato_2, consolidato_3, prodotto_1, prodotto_2, prodotto_3,");
		insertSql.append(" id_user, tariffa, matricola, nome_risorsa, cognome_risorsa, valuta, cliente, desc_progetto, business_unit, attività)");
		insertSql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

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
				pstm.setString(i++, "Admin");
				pstm.setInt(i++, bean.getPrice());
				pstm.setString(i++, bean.getBadgeNumber());
				pstm.setString(i++, bean.getNome());
				pstm.setString(i++, bean.getCognome());
				pstm.setString(i++, bean.getCurrency());
				pstm.setString(i++, bean.getCustomer());
				pstm.setString(i++, bean.getProjectDesc());
				pstm.setInt(i++, bean.getBusinessUnit());
				pstm.setString(i++, bean.getActivityType());
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
			public V2Bean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				V2Bean rv = new V2Bean();
				rv.setMonth(rs.getInt("mese"));
				rv.setUser(rs.getString("id_user"));
				rv.setEditable(rs.getInt("editable"));
				return rv;
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
		Object[] params = new Object[] { nextMonth, username, 0 };
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
			public V2Bean mapRow(ResultSet rs, int rowNumb) throws SQLException {

				V2Bean result = new V2Bean();

				result.setUser(rs.getString("id_user"));
				result.setMonth(rs.getInt("mese"));
				result.setEditable(rs.getInt("editable"));

				return result;
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
			public V2Bean mapRow(ResultSet rs, int rowNumb) throws SQLException {

				V2Bean result = new V2Bean();

				result.setUser(rs.getString("id_user"));
				result.setMonth(rs.getInt("mese"));
				result.setEditable(rs.getInt("editable"));

				return result;
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
	
	
}