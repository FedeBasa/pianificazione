package it.soprasteria.pianificazione.v2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import it.soprasteria.pianificazione.v2.bean.EmployeeBean;
import it.soprasteria.pianificazione.v2.bean.ProjectBean;
import it.soprasteria.pianificazione.v2.bean.RecordV2Bean;
import it.soprasteria.pianificazione.v2.bean.UserBean;
import it.soprasteria.pianificazione.v2.bean.V2Bean;
import it.soprasteria.pianificazione.v2.dao.AdminDao;
import it.soprasteria.pianificazione.v2.dao.Dao;
import it.soprasteria.pianificazione.v2.util.DateUtil;
import it.soprasteria.pianificazione.v2.util.V2StatusKeys;

public class V2Service {
	
	private static final String CONSOLIDATO_3 = "consolidato_3";

	private static final String CONSOLIDATO_2 = "consolidato_2";

	private static final String CONSOLIDATO_1 = "consolidato_1";

	private static final Logger LOG = Logger.getLogger(V2Service.class);

	@Autowired
	private Dao dao;
	
	@Autowired
	private AdminDao adminDao;
	
	@Autowired
	private CalendarConfigService calendarConfigService;
	
	@Autowired
	private EmployeeService employeeService;

	public V2Bean findByMonth(int month, int businessUnit, String username) {

		return dao.findByMonth(month, businessUnit, username);
	}

	public List<RecordV2Bean> getV2(int month, int businessUnit, String user) {

		return dao.getV2(month, businessUnit, user);
	}

	public RecordV2Bean getRecord(Long id) {

		return dao.getRecord(id);
	}

	private void completeRecord(RecordV2Bean item) {
		String bn = item.getBadgeNumber();
		if (bn != null && bn.length() > 0 && bn.length() <= 6) {
			EmployeeBean eb = dao.getEmployee(bn);
			if (eb != null) {
				item.setNome(eb.getName());
				item.setCognome(eb.getSurname());
			}
		} else {
			
			item.setBadgeNumber(UUID.randomUUID().toString());
			String employeeDesc = item.getEmployeeDesc();
			int index = employeeDesc.lastIndexOf(" ");
			if (index > -1) {
				item.setNome(employeeDesc.substring(index+1));
				item.setCognome(employeeDesc.substring(0, index));
			} else {
				item.setCognome(employeeDesc);
			}
		}
		Long id = item.getIdProject();
		ProjectBean prb = dao.getProject(id);
		item.setProjectDesc(prb.getDescription());
		item.setCustomer(prb.getCustomer());
		item.setBusinessUnit(prb.getBusinessUnit());
	}

	public void updateRecord(RecordV2Bean record) {
		completeRecord(record);
		record.setCost(0);
		record.setPrice(0);
		dao.update(record);
	}

	public void insertRecord(RecordV2Bean record) {
		
		completeRecord(record);
		
		String badgeNumber = record.getBadgeNumber();
		EmployeeBean employeeBean = employeeService.findByBadgeNumber(badgeNumber);
		if (employeeBean == null) {
			record.setCost(999);
		}
		
		int month = record.getMonth();
		record.setCons0(getLeftDays(badgeNumber, month, CONSOLIDATO_1));
		record.setCons1(getLeftDays(badgeNumber, month, CONSOLIDATO_2));
		record.setCons2(getLeftDays(badgeNumber, month, CONSOLIDATO_3));
		
		dao.insert(record);
	}

	public void deleteRecord(Long id) {
		dao.delete(id);
	}

	public boolean v2Update(Long id, int month, String colname, Integer value, String username) {
		

		if (value < 0 && !colname.startsWith("prodotto_")) {
			return false;
		}
		RecordV2Bean recordV2Bean = dao.getRecord(id);
		String badgeNumber = recordV2Bean.getBadgeNumber();
		if ("costo".equals(colname)) {
			
			EmployeeBean employeeBean = employeeService.findByBadgeNumber(badgeNumber);
			if (employeeBean != null) {
				return false;
			}
			
		} else if (!"tariffa".equals(colname) && !"costo".equals(colname) && !colname.startsWith("prodotto_")) {
		
			int checkValue = value - getColvalue(recordV2Bean, colname);
			
			int leftDays = getLeftDays(badgeNumber, month, colname);
			
			LOG.debug("LEFTDAYS" + leftDays);
			LOG.debug("CHECKVALUE" + checkValue);
			
			if (checkValue > leftDays) {
				return false;
			}
		}		
		
		dao.updateTable(id, colname, value, username);
		
		return true;
	}

	private int getColvalue(RecordV2Bean recordV2Bean, String colname) {
		
		if(CONSOLIDATO_1.equals(colname)) {
			return recordV2Bean.getCons0();
		}
		if(CONSOLIDATO_2.equals(colname)) {
			return recordV2Bean.getCons1();
		}
		if(CONSOLIDATO_3.equals(colname)) {
			return recordV2Bean.getCons2();
		}
		
		return 0;
	}
	
	public List<Integer> getMonths(String user) {
		List<Integer> monthsList = dao.getMonths(user);

		return monthsList;
	}

	public List<V2Bean> findByUser(String username) {

		return dao.findByUser(username);
	}

	@Transactional
	public boolean addNextMonth(UserBean userBean) {

		List<Integer> list = dao.getMonths(userBean.getUsername());
		List<Integer> listConfig = adminDao.getMonthsConfig();

		Integer lastMonth;
		Integer lastMonthConfig;
		boolean check;

		if (list.isEmpty()) {
			check = !listConfig.isEmpty();
			lastMonth = DateUtil.previousMonth(listConfig.get(0));
		} else {
			lastMonth = list.get(list.size() - 1);
			lastMonthConfig = listConfig.get(listConfig.size() - 1);
			check = lastMonth.intValue() < lastMonthConfig.intValue();
		}

		if (check) {
			
			int nextMonth = DateUtil.nextMonth(lastMonth);

			List<String> buList = userBean.getBuList();
			for(String bu : buList) {
				dao.createV2(userBean.getUsername(), lastMonth, Integer.parseInt(bu));
			}
			
			dao.addProjectsResources(userBean.getUsername(), lastMonth, nextMonth);
			
			LOG.info("aggiunto nuovo mese");
			return true;
		}

		return false;
	}

	public void v2Update(long id, String realColname, String value, String username) {
		dao.updateTable(id, realColname, value, username);
	}

	private int getLeftDays(String badgeNumber, int month, String colname) {
		
		int config = 0;
		if (CONSOLIDATO_2.equals(colname)) {
			config = 1;
		} else if (CONSOLIDATO_3.equals(colname)) {
			config = 2;
		}

		int totDays = calendarConfigService.getConfig(DateUtil.addMonth(month, config));
		int consDays = dao.getConsDays(badgeNumber, colname, month);

		int result = totDays - consDays;

		return result;
	}

	public List<String> changeStatus(String username, int month, int businessUnit, int status) {

		List<String> messageList = new ArrayList<>();
		
		V2Bean v2 = dao.findByMonth(month, businessUnit, username);
		if (status == V2StatusKeys.CLOSED) {
			
			messageList.add("Operazione non permessa");
			
		} else if (status == V2StatusKeys.OPEN) {
			
			if (v2.getStato() == V2StatusKeys.CLOSED) {
				
				messageList.add("Operazione non permessa");
			}
		} else if (status == V2StatusKeys.VALIDATED) {
			
			validateV2(username, month, businessUnit, messageList);
		}
		
		if (messageList.isEmpty()) {
		
			dao.changeStatus(username, month, businessUnit, status);
		}
		
		return messageList;
	}
	
	private void validateV2(String username, int month, int businessUnit, List<String> messageList) {
		
		List<RecordV2Bean> recordList = dao.getV2(month, businessUnit, username);
		
		for (RecordV2Bean record : recordList) {
			
			String employeeDesc = record.getEmployeeDesc();
			
			if (record.getActivityType() == null || record.getActivityType().length() == 0) {
				messageList.add("Specificare attivita' per risorsa - " + employeeDesc);
			}
			if (record.getCurrency() == null || record.getCurrency().length() == 0) {
				messageList.add("Specificare valuta per risorsa - " + employeeDesc);
			}
			if (record.getPrice() == null || record.getPrice() == 0) {
				messageList.add("Specificare tariffa per risorsa - " + employeeDesc);
			}

			EmployeeBean employee = dao.getEmployee(record.getBadgeNumber());
			if (employee == null) {
				if (record.getCost() == 0 || record.getCost() == 999) {
					messageList.add("Specificare costo per risorsa - " + employeeDesc);
				}
			}
			if ("TRAINING".equals(record.getProjectDesc()) || "PRESALES".equals(record.getProjectDesc())) {
				if (record.getCustomDesc() == null || record.getCustomDesc().length() == 0) {
					messageList.add("Specificare descrizione progetto per risorsa - " + employeeDesc);
				}
			}
		}		
	}
	
	public void produceAll(String user, int month, int businessUnit) {
		
		dao.produceAll(user, month, businessUnit);
	}
	
}