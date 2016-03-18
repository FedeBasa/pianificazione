package it.soprasteria.pianificazione.v2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.bean.ProjectBean;
import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;
import it.soprasteria.pianificazione.v2.bean.V2Bean;

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
		List<ProjectBean> prog = getJdbcTemplate().query("SELECT * FROM progetti WHERE business_unit = ?",new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
                        pstm.setInt(1,businessUnit);				
			}
		}, new RowMapper<ProjectBean>() {
			public ProjectBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				ProjectBean proj = new ProjectBean();
				proj.setIdProject(rs.getLong("id_progetto"));
				proj.setDescription(rs.getString("progetto"));
				proj.setCustomer(rs.getString("cliente"));
				proj.setCurrency(rs.getString("valuta"));
				proj.setType(rs.getString("attivit�"));
				proj.setBusinessUnit(businessUnit);
				LOG.debug(proj.getType());
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
		sb.append(" AND user_id = ?");

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
				v2b.setIdProject(rs.getInt("id_progetto"));
				v2b.setBadgeNumber(Integer.toString(rs.getInt("id_risorsa")));
				v2b.setCons0(rs.getInt("consolidato_1"));
				v2b.setProd0(rs.getInt("prodotto_1"));
				v2b.setCons1(rs.getInt("consolidato_2"));
				v2b.setProd1(rs.getInt("prodotto_2"));
				v2b.setCons2(rs.getInt("consolidato_3"));
				v2b.setProd2(rs.getInt("prodotto_3"));
				v2b.setPrice(rs.getInt("tariffa"));
				return v2b;
			}
		});
		return result;
	}

	@Override
	public EmployeeBean getEmployee(String id) {
		LOG.debug("MATRICOLAAA  :" + id);

		List<EmployeeBean> b1 = getJdbcTemplate().query("SELECT * FROM risorse WHERE matricola = " + id, new RowMapper<EmployeeBean>() {
			public EmployeeBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				EmployeeBean b = new EmployeeBean();
				b.setBadgeNumber(rs.getString(1));
				b.setName(rs.getString(2));
				b.setSurname(rs.getString(3));
				return b;
			}
		});
		LOG.debug("Lunghezza: " + b1.size());
		if (!b1.isEmpty()) {
			LOG.debug("Questo e' il primo elemento " + b1.get(0));
		}
		return b1.get(0);

	}

	@Override
	public ProjectBean getProject(long id) {
		LOG.debug("IDENTIFICATIVO  :" + id);
		List<ProjectBean> p1 = getJdbcTemplate().query("SELECT * FROM progetti WHERE  id_progetto = " + id, new RowMapper<ProjectBean>() {
			@Override
			public ProjectBean mapRow(ResultSet rs, int rowNumb) throws SQLException {
				ProjectBean p = new ProjectBean();
				p.setDescription(rs.getString("progetto"));
				p.setCustomer(rs.getString("cliente"));
				p.setCurrency(rs.getString("valuta"));
				p.setType(rs.getString("attivit�"));
				return p;
			}
		});
		LOG.debug("Lungezza :" + p1.size());
		if (!p1.isEmpty()) {
			LOG.debug("Questo � il primo elemento " + p1.get(0));
		}
		return p1.get(0);

	}

	@Override
	public RecordV2Bean getRecord(final long id) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT *");
		sb.append(" FROM u_progetti_risorse");
		sb.append(" WHERE id_unione  = ?");

		List<RecordV2Bean> l1 = getJdbcTemplate().query(sb.toString(), new PreparedStatementSetter() {
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
				rv.setIdProject(rs.getInt("id_progetto"));
				rv.setBadgeNumber(Integer.toString(rs.getInt("id_risorsa")));
				LOG.debug("*******TARIFFAAAAAAAAAAAAAAAA *****************" + rs.getInt("tariffa"));
				rv.setPrice(rs.getInt("tariffa"));
				return rv;

			};
		});
		return l1.get(0);
	}

	@Override
	public void update(final RecordV2Bean rec) {

		final StringBuilder sb = new StringBuilder();
		sb.append("UPDATE u_progetti_risorse");
		sb.append(" SET id_progetto=?,consolidato_1= ?,prodotto_1= ?,consolidato_2= ?,prodotto_2= ?,consolidato_3= ?,prodotto_3= ?,tariffa= ?");
		sb.append(" WHERE id_unione = ?");
		getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				int i = 1;
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				ps.setLong(i++, rec.getIdProject());
				ps.setInt(i++, rec.getCons0());
				ps.setInt(i++, rec.getProd0());
				ps.setInt(i++, rec.getCons1());
				ps.setInt(i++, rec.getProd1());
				ps.setInt(i++, rec.getCons2());
				ps.setInt(i++, rec.getProd2());
				ps.setInt(i++, rec.getPrice());
				ps.setLong(i++, rec.getIdRecord());
				LOG.debug(rec.getIdRecord());
				return ps;
			}
		});
	}

	@Override
	public void insert(final RecordV2Bean rec) {
		final StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO u_progetti_risorse (mese,id_progetto,id_risorsa,consolidato_1," + "prodotto_1,consolidato_2,prodotto_2,consolidato_3," + "prodotto_3,user_id,tariffa)");
		sb.append("  VALUES(?,?,?,?,?,?,?,?,?,?,?)");
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				int i = 1;
				PreparedStatement ps = conn.prepareStatement(sb.toString());
				ps.setInt(i++, rec.getMonth());
				ps.setLong(i++, rec.getIdProject());
				ps.setInt(i++, Integer.parseInt(rec.getBadgeNumber()));
				ps.setInt(i++, rec.getCons0());
				ps.setInt(i++, rec.getProd0());
				ps.setInt(i++, rec.getCons1());
				ps.setInt(i++, rec.getProd1());
				ps.setInt(i++, rec.getCons2());
				ps.setInt(i++, rec.getProd2());
				
				// TODO
				// sistemare, cablato nome utente
				ps.setString(i++, "Admin");
				ps.setInt(i++, rec.getPrice());
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
	public List<RecordV2Bean> findAllV2(){
		List<RecordV2Bean> result = new ArrayList<RecordV2Bean>();
		StringBuilder sb = new StringBuilder();
		
		// TODO
		// recuperare l'elenco dei v2 in modo diverso

		sb.append("SELECT *");
		sb.append(" FROM u_progetti_risorse");
		sb.append(" GROUP BY mese");
       result = getJdbcTemplate().query(sb.toString(), new RowMapper<RecordV2Bean>(){
    	 @Override
    	public RecordV2Bean mapRow(ResultSet rs, int rowNumb) throws SQLException {
    		 RecordV2Bean rv = new RecordV2Bean();
    		 rv.setMonth(rs.getInt("mese"));
    		 LOG.debug(rv.getMonth());
    		return rv;
    	}
       });
		return result;
	}

//
//	@Override
//	public List<Integer> getMonths(String user){
//		List<Integer> result = new ArrayList<Integer>();
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("SELECT DISTINCT mese");
//		sb.append(" FROM u_progetti_risorse");
//		sb.append(" WHERE user_id = '" + user + "'");
//		sb.append(" ORDER BY mese");
//		
//       result = getJdbcTemplate().query(sb.toString(), new RowMapper<Integer>(){
//    	 @Override
//    	public Integer mapRow(ResultSet rs, int rowNumb) throws SQLException {
//    		 Integer mese = rs.getInt("mese");
//    		return mese;
//    	}
//       });
//		return result;
//	}

	//recupera la lista dei mesi
	@Override
	public List<Integer> getMonths(String user){
		List<Integer> result = new ArrayList<Integer>();
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT mese");
		sb.append(" FROM v2");
		sb.append(" WHERE user = '" + user + "'");
		sb.append(" ORDER BY mese");
		
       result = getJdbcTemplate().query(sb.toString(), new RowMapper<Integer>(){
    	 @Override
    	public Integer mapRow(ResultSet rs, int rowNumb) throws SQLException {
    		 Integer mese = rs.getInt("mese");
    		return mese;
    	}
       });
		return result;
	}
	
	//recupera solo l'ultimo mese
	public Integer getLastMonth(List<Integer> mesi) {
		return mesi.get(mesi.size() -1);
	}
	
	//controlla se il mese � editabile per l'utente corrente
	public int checkEditable(final String user, final int month) {
		
		List<Integer> mesi = getJdbcTemplate().query("SELECT editable FROM v2 WHERE user = ? AND mese = ?",new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement pstm) throws SQLException {
                        pstm.setString(1, user);
                        pstm.setInt(2, month);
			}
		}, new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int rowNumb) throws SQLException {			
				int editable = (rs.getInt("editable"));
				return editable;
			}
		});
		
		return mesi.get(0);

	}
	
	//controllo che venga aggiunto solo il mese seguente
	public boolean checkMonth(int mese) {
		
		//recupero il mese 
		Date date= new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		int currentMonth = cal.get(Calendar.MONTH);
		int currentYear = cal.get(Calendar.YEAR);
		
		String meseString = "" + mese;
		int lastYear= Integer.parseInt(meseString.substring(0, 4));
		int lastMonth = Integer.parseInt(meseString.substring(4, 6));
		currentMonth++;
		
		
		if(currentMonth == lastMonth && currentYear == lastYear) {			
			return true;
		}
		
		return false;
	}
	
	//replica le risorse del mese precedente per il nuovo mese
	@Override
	public void addProjectsResources(String user, int currentMonth, int nextMonth) {
		
		List<RecordV2Bean> result = new ArrayList<RecordV2Bean>();
		StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM u_progetti_risorse WHERE mese = '"+ currentMonth +"' AND user_id = '" + user + "' ORDER BY mese desc");
			
			result = getJdbcTemplate().query(sb.toString(), new RowMapper<RecordV2Bean>(){
				 @Override
			    	public RecordV2Bean mapRow(ResultSet rs, int rowNumb) throws SQLException {
			    		 RecordV2Bean rv = new RecordV2Bean();
			    		 rv.setMonth(rs.getInt("mese"));
			    		 rv.setIdProject(rs.getLong("id_progetto"));
			    		 rv.setBadgeNumber(rs.getString("id_risorsa"));
			    		 rv.setCons0(rs.getInt("consolidato_1"));
			    		 rv.setCons1(rs.getInt("consolidato_2"));
			    		 rv.setCons2(rs.getInt("consolidato_3"));
			    		 rv.setProd0(rs.getInt("prodotto_1"));
			    		 rv.setProd1(rs.getInt("prodotto_2"));
			    		 rv.setProd2(rs.getInt("prodotto_3"));
			    		 rv.setPrice(rs.getInt("tariffa"));
			    		return rv;
			    	}
			       });
			
			String insertSql =
					  "INSERT INTO u_progetti_risorse (" +
					  " mese, " +
					  " id_progetto, " +
					  " id_risorsa, " +
					  " consolidato_1, " +
					  " consolidato_2," + 
					  " consolidato_3, " +
					  " prodotto_1," + 
					  " prodotto_2, " +
					  " prodotto_3," + 	
					  " user_id, " +
					  " tariffa)" +
					  "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
			for (RecordV2Bean v2 : result) {
				
			Object[] params = new Object[] { nextMonth, v2.getIdProject(), v2.getBadgeNumber(), v2.getCons1(), v2.getCons2(), 0, v2.getProd1(), v2.getProd2(), 0, "Admin", v2.getPrice() };
			int[] types = new int[] { Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.INTEGER };
			int row = getJdbcTemplate().update(insertSql, params, types);
			System.out.println(row + " row inserted.");
			}
	}
	
	//aggiunge il nuovo mese
	@Override
	public void addNextMonth(String user) {
		
		List<V2Bean> result = new ArrayList<V2Bean>();
		StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM v2 WHERE mese = '"+ getLastMonth(getMonths(user)) +"' AND user = '" + user + "' ORDER BY mese desc");
			
			result = getJdbcTemplate().query(sb.toString(), new RowMapper<V2Bean>(){
				 @Override
			    	public V2Bean mapRow(ResultSet rs, int rowNumb) throws SQLException {
			    		 V2Bean rv = new V2Bean();
			    		 rv.setMonth(rs.getInt("mese"));
			    		 rv.setUser(rs.getString("user"));
			    		 rv.setEditable(rs.getInt("editable"));
			    		return rv;
			    	}
			       });
			
			String insertSql =
					  "INSERT INTO v2 (" +
					  " mese, " +
					  " user, " +
					  " editable)" +
					  "VALUES (?, ?, ?)";
	
			int nextMonth = 0;
			int currentMonth = 0;
			for (V2Bean v2 : result) {
			currentMonth = v2.getMonth();
			nextMonth = getNextMonth(v2.getMonth());
			Object[] params = new Object[] { nextMonth, user, 0 };
			int[] types = new int[] { Types.INTEGER, Types.VARCHAR, Types.INTEGER };
			int row = getJdbcTemplate().update(insertSql, params, types);
			LOG.info((row + " row inserted."));
			}
			
			addProjectsResources(user, currentMonth, nextMonth);
	}
	
	//calcola il mese successivo
	private int getNextMonth(int month) {
		
		String meseString = "" + month;
		String stringMese = "";
		int anno = Integer.parseInt(meseString.substring(0, 4));
		int mese = Integer.parseInt(meseString.substring(4, 6));
		if(mese < 12) {
			mese++;
		}
		else if(mese == 12){
			mese = 1;
			anno++;
		}
		if(mese < 10) {
			stringMese = stringMese + 0;
		}
		
		stringMese = stringMese + mese;
		
		
		return Integer.parseInt(anno + stringMese);
	}
	
	@Override
	public void setEditable(final String user, final int month) {
		
		LOG.info("##############SET EDITABLE");
		LOG.info("############## USER: " + user);
		LOG.info("############## MESE: " + month);
		final StringBuilder sb = new StringBuilder();
		sb.append("UPDATE v2");
		sb.append(" SET editable= ?");
		sb.append(" WHERE user = ? AND");
		sb.append(" mese = ?");
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
		
}
