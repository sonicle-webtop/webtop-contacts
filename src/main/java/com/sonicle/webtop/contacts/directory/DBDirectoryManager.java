/* 
 * Copyright (C) 2014 Sonicle S.r.l.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by
 * the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED
 * WORK IN WHICH THE COPYRIGHT IS OWNED BY SONICLE, SONICLE DISCLAIMS THE
 * WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA.
 *
 * You can contact Sonicle S.r.l. at email address sonicle[at]sonicle[dot]com
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License version 3.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License
 * version 3, these Appropriate Legal Notices must retain the display of the
 * Sonicle logo and Sonicle copyright notice. If the display of the logo is not
 * reasonably feasible for technical reasons, the Appropriate Legal Notices must
 * display the words "Copyright (C) 2014 Sonicle S.r.l.".
 */
package com.sonicle.webtop.contacts.directory;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.text.*;

public class DBDirectoryManager implements DirectoryManager {

	public enum DataType {

		NUMERIC,
		STRING,
		DATE
	}

	static HashMap<Locale, DateFormat> ss_dateFormatters = new HashMap<Locale, DateFormat>();
	static HashMap<Locale, DateFormat> sm_dateFormatters = new HashMap<Locale, DateFormat>();
	static HashMap<Locale, DateFormat> sl_dateFormatters = new HashMap<Locale, DateFormat>();
	static HashMap<Locale, DateFormat> ms_dateFormatters = new HashMap<Locale, DateFormat>();
	static HashMap<Locale, DateFormat> mm_dateFormatters = new HashMap<Locale, DateFormat>();
	static HashMap<Locale, DateFormat> ml_dateFormatters = new HashMap<Locale, DateFormat>();
	static HashMap<Locale, DateFormat> ls_dateFormatters = new HashMap<Locale, DateFormat>();
	static HashMap<Locale, DateFormat> lm_dateFormatters = new HashMap<Locale, DateFormat>();
	static HashMap<Locale, DateFormat> ll_dateFormatters = new HashMap<Locale, DateFormat>();
	static HashMap<Locale, DateFormat> s_dateFormatters = new HashMap<Locale, DateFormat>();
	static HashMap<Locale, DateFormat> m_dateFormatters = new HashMap<Locale, DateFormat>();
	static HashMap<Locale, DateFormat> l_dateFormatters = new HashMap<Locale, DateFormat>();

	Calendar enCalendar = Calendar.getInstance(new Locale("en"));

	DirectoryType dirType;
	boolean iscontact = false;
	String id;
	String description;
	String jdbcDriver;
	String jdbcURL;
	String user;
	String pass;
	String table;
	String filter;
	ArrayList<String> fixedinsertfields = new ArrayList<String>();
	ArrayList<String> fixedinsertvalues = new ArrayList<String>();
	ArrayList<String> clickFields = new ArrayList<String>();
	boolean isAllClickFields = false;
	ArrayList<String> searchFields = new ArrayList<String>();
	String mailField;
	String faxField = null;
	String firstNameField;
	String lastNameField;
	String companyField;
	ArrayList<String> fields = new ArrayList<String>();
	ArrayList<String> realfields = new ArrayList<String>();
	ArrayList<String> orderfields = new ArrayList<String>();
	ArrayList<String> defaultvalues = new ArrayList<String>();
	ArrayList<ActionData> actions = new ArrayList<ActionData>();
	HashMap<String, ActionData> actionfields = new HashMap<String, ActionData>();
	ArrayList<DirectoryGroup> groups = new ArrayList<DirectoryGroup>();
	HashMap<String, LookupData> lookups = new HashMap<String, LookupData>();
	boolean isList[] = null;
	int listColumnCount = 0;
	DataType fieldtypes[] = null;
	int fieldsizes[] = null;
	ArrayList<String> keyfields = new ArrayList<String>();
	DataType keyfieldtypes[] = null;
	boolean write = false;
	ArrayList<String> filterGroups = new ArrayList<String>();
	ArrayList<HashMap<String, String>> filterGroupsDescriptions = new ArrayList<HashMap<String, String>>();

	private DataSource datasource = null;

	public DBDirectoryManager(DirectoryType dirType, String id, String description, DataSource datasource,
			String table, String fields, String lookups, String clickfields, String actionfields, String searchfields,
			String mailfield, String faxfield, String firstNameField, String lastNameField, String companyField,
			String keyfields, String filter, String orderfields, String fixedinserts) throws SQLException {
		this.dirType = dirType;
		this.id = id;
		this.description = description;
		this.datasource = datasource;
		this.table = table;
		this.mailField = mailfield.toUpperCase();
		if (faxfield != null) {
			this.faxField = faxfield.toUpperCase();
		}
		this.firstNameField = firstNameField.toUpperCase();
		this.lastNameField = lastNameField.toUpperCase();
		this.companyField = companyField.toUpperCase();
		this.filter = filter;
		int fieldindex = 0;
		StringTokenizer gst = new StringTokenizer(fields, "|");
		while (gst.hasMoreTokens()) {
			String sgroup = gst.nextToken();
			int ix1 = sgroup.indexOf('{');
			int ix2 = sgroup.indexOf('}');
			String groupname = sgroup.substring(0, ix1).trim();
			String gfields = sgroup.substring(ix1 + 1, ix2);
			boolean visible = true;

			if (groupname.charAt(0) == '!') {
				groupname = groupname.substring(1);
				visible = false;
			}
			DirectoryGroup group = new DirectoryGroup(groupname);
			group.setVisible(visible);
			group.setStartIndex(fieldindex);
			StringTokenizer st = new StringTokenizer(gfields, ",");
			while (st.hasMoreTokens()) { //check for any specified alias too
				String xtoken = st.nextToken().trim();
				String token = xtoken.toUpperCase();
				int ix = token.indexOf(' ');
				String realfield = token;
				String alias = token;
				String defaultvalue = null;
				if (ix > 0) {
					realfield = token.substring(0, ix);
					int ixx = token.indexOf(' ', ix + 1);
					if (ixx < 0) {
						alias = token.substring(ix + 1).trim();
					} else {
						alias = alias = token.substring(ix + 1, ixx).trim();
						defaultvalue = xtoken.substring(ixx + 1);
					}
				}

				if (alias.charAt(0) == '+') {
					alias = alias.substring(1);
				}
				this.fields.add(alias);
				this.realfields.add(realfield);
				this.defaultvalues.add(defaultvalue);
				fieldindex++;
			}
			group.setEndIndex(fieldindex - 1);
			groups.add(group);
		}

		isList = new boolean[realfields.size()];
		listColumnCount = 0;
		for (int i = 0; i < isList.length; ++i) {
			String s = realfields.get(i);
			if (s.charAt(0) == '+') {
				realfields.set(i, s.substring(1));
				isList[i] = true;
				++listColumnCount;
			} else {
				isList[i] = false;
			}
		}

		if (clickfields != null) {
			if (clickfields.trim().equals("*")) {
				isAllClickFields = true;
			} else {
				StringTokenizer st = new StringTokenizer(clickfields, ",");
				while (st.hasMoreTokens()) {
					this.clickFields.add(st.nextToken().toUpperCase());
				}
			}
		}

		if (searchfields != null) {
			StringTokenizer st = new StringTokenizer(searchfields, ",");
			while (st.hasMoreTokens()) {
				this.searchFields.add(st.nextToken().toUpperCase());
			}
		}

		if (keyfields != null) {
			StringTokenizer st = new StringTokenizer(keyfields, ",");
			while (st.hasMoreTokens()) {
				this.keyfields.add(st.nextToken().toUpperCase());
			}
		}

		if (orderfields != null) {
			StringTokenizer st = new StringTokenizer(orderfields, ",");
			while (st.hasMoreTokens()) {
				this.orderfields.add(st.nextToken().toUpperCase());
			}
		}

		if (fixedinserts != null) {
			StringTokenizer st = new StringTokenizer(fixedinserts, ",");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				StringTokenizer st2 = new StringTokenizer(token, "=");
				String field = st2.nextToken();
				String value = st2.nextToken();
				fixedinsertfields.add(field);
				fixedinsertvalues.add(value);
			}
		}

		if (lookups != null) {
			StringTokenizer st = new StringTokenizer(lookups, "|");
			while (st.hasMoreTokens()) {
				String lookup = st.nextToken();
				int x1 = lookup.indexOf('{');
				int x2 = lookup.indexOf('}');
				String fieldname = lookup.substring(0, x1).trim().toUpperCase();
				String data = lookup.substring(x1 + 1, x2).trim();
				StringTokenizer st2 = new StringTokenizer(data, ",");
				LookupData ld = new LookupData();
				ld.table = st2.nextToken().trim();
				ld.filter = st2.nextToken().trim();
				ld.keyfield = st2.nextToken().trim();
				ld.viewfield = st2.nextToken().trim();
				this.lookups.put(fieldname, ld);
			}
		}

		if (actionfields != null) {
			StringTokenizer st = new StringTokenizer(actionfields, "|");
			while (st.hasMoreTokens()) {
				String action = st.nextToken();
				int x1 = action.indexOf('{');
				int x2 = action.indexOf('}');
				String fieldname = action.substring(0, x1).trim().toUpperCase();
				String data = action.substring(x1 + 1, x2).trim();
				StringTokenizer st2 = new StringTokenizer(data, ",");
				ActionData ad = new ActionData();
				ad.actionname = action;
				ad.fieldname = fieldname;
				ad.fieldvalue = st2.nextToken().trim();
				ad.target = st2.nextToken().trim();
				ad.url = st2.nextToken().trim();
				String str = st2.nextToken().trim();
				StringTokenizer st3 = new StringTokenizer(str, ":");
				while (st3.hasMoreTokens()) {
					ad.fieldlink.add(st3.nextToken());
				}
				this.actions.add(ad);
				this.actionfields.put(fieldname, ad);
			}
		}

		saveTypesAndSizes();
	}

	public String getId() {
		return id;
	}

	public DirectoryType getType() {
		return dirType;
	}

	public DataType getType(String field) {
		int ix = fields.indexOf(field);
		if (ix >= 0) {
			return fieldtypes[ix];
		}

		ix = keyfields.indexOf(field);
		if (ix >= 0) {
			return keyfieldtypes[ix];
		}

		return DataType.STRING;
	}

	public int getSize(String field) {
		return fieldsizes[fields.indexOf(field)];
	}

	public String getDescription() {
		return description;
	}

	public List<String> getRealFields() {
		return realfields;
	}

	public List<String> getFields() {
		return fields;
	}

	public List<String> getKeyFields() {
		return keyfields;
	}

	public List<String> getClickFields() {
		return clickFields;
	}

	public List<String> getSearchFields() {
		return searchFields;
	}

	public boolean isClickField(String field) {
		if (isAllClickFields) {
			return fields.contains(field.toUpperCase());
		}
		return clickFields.contains(field.toUpperCase());
	}

	public boolean isSearchField(String field) {
		return searchFields.contains(field.toUpperCase());
	}

	public boolean isLookupField(String field) {
		return lookups.containsKey(field.toUpperCase());
	}

	public boolean isActionField(String field) {
		return actionfields.containsKey(field);
	}

	public ActionData getActionData(String field) {
		return actionfields.get(field);
	}

	public String getMailField() {
		return mailField;
	}

	public String getFaxField() {
		return faxField;
	}

	public String getFirstNameField() {
		return firstNameField;
	}

	public String getLastNameField() {
		return lastNameField;
	}

	public String getCompanyField() {
		return companyField;
	}

	public boolean isWritable() {
		return write;
	}

	public void setWritable(boolean b) {
		write = b;
	}

	public boolean isContact() {
		return iscontact;
	}

	public void setIsContact(boolean b) {
		iscontact = b;
	}

	public boolean isListField(int fieldIndex) {
		return isList[fieldIndex];
	}

	public int getListColumnCount() {
		return listColumnCount;
	}

	public DirectoryResult lookup(String pattern, Locale locale, boolean isAnd, boolean caseSensitive) {
		return lookup(pattern, locale, isAnd, caseSensitive, null);
	}

	public DirectoryResult lookup(String pattern, Locale locale, boolean isAnd, boolean caseSensitive, List<String> filterGroupValues) {
		ArrayList<String> vsf = searchFields;
		ArrayList<String> vp = new ArrayList<String>();
		for (int i = 0; i < vsf.size(); ++i) {
			vp.add(pattern);
		}
		return lookup(vsf, vp, locale, isAnd, caseSensitive, filterGroupValues, false);
	}

	public DirectoryResult lookup(String searchfield, String pattern, Locale locale, boolean isAnd, boolean caseSensitive) {
		return lookup(searchfield, pattern, locale, isAnd, caseSensitive, null);
	}

	public DirectoryResult lookup(String searchfield, String pattern, Locale locale, boolean isAnd, boolean caseSensitive, List<String> filterGroupValues) {
		ArrayList<String> vsf = new ArrayList<String>();
		ArrayList<String> vp = new ArrayList<String>();
		vsf.add(searchfield);
		vp.add(pattern);
		return lookup(vsf, vp, locale, isAnd, caseSensitive, filterGroupValues, false);
	}

	public DirectoryResult lookup(List<String> searchfields, List<String> patterns, Locale locale, boolean isAnd,
			boolean caseSensitive, boolean distinct) {
		return lookup(searchfields, patterns, locale, isAnd, caseSensitive, null, distinct);
	}

	public DirectoryResult lookup(List<String> searchfields, List<String> patterns, Locale locale, boolean isAnd,
			boolean caseSensitive, List<String> filterGroupValues, boolean distinct) {
		DirectoryResult result = null;
		Connection conn = null;
		Statement stmt = null;
		Statement stmt2 = null;
		ResultSet rs = null;
		try {
			boolean whereDone = false;
			String sql = "select ";
			if (distinct) {
				sql += "distinct ";
			}
			for (ActionData ad : actions) {
				if (ad.actionname.equals("-") || ad.fieldname.equals("-")) {
					continue; //skip child action links
				}
				sql += "'" + ad.fieldvalue + "' as " + ad.fieldname + ",";
			}
			int vcolumns = fields.size();
			for (int i = 0; i < vcolumns; ++i) {
				if (i > 0) {
					sql += ",";
				}
				String realfield = realfields.get(i);
				String alias = fields.get(i);
				sql += realfield + " as " + alias;
			}
			for (String keyfield : keyfields) {
				if (!realfields.contains(keyfield)) {
					sql += "," + keyfield;
				}
			}
			sql += " from " + table;
			int sfields = searchfields.size();
			String ppattern = "";
			if (sfields > 0) {
				for (int i = 0; i < sfields; ++i) {
					String searchfield = searchfields.get(i);
					String realfield = searchfield;
					int ix = fields.indexOf(searchfield);
					if (ix >= 0) {
						realfield = realfields.get(ix);
					}
					String pattern = patterns.get(i);
					if (!caseSensitive) {
						pattern = pattern.toUpperCase();
					}
					if (!whereDone) {
						sql += " where (";
						whereDone = true;
					} else {
						if (isAnd) {
							sql += " and ";
						} else {
							sql += " or ";
						}
					}
					if (pattern.equals("#")) {
						sql += " " + realfield + " IS NULL or LENGTH(" + realfield + ")=0 ";
					} else {
						DataType type = DataType.STRING;
						if (ix >= 0) {
							type = fieldtypes[ix];
						} else {
							ix = keyfields.indexOf(realfield);
							if (ix >= 0) {
								type = keyfieldtypes[ix];
							}
						}

						if (type == DataType.STRING) {
							if (pattern.indexOf('%') >= 0) {
								//a space is treated as multiple and likes
								String pts[] = pattern.split(" ");
								boolean first = true;
								String lsql = "";
								for (String pt : pts) {
									pt = pt.trim();
									if (pt.length() == 0) {
										continue;
									}
									if (!first) {
										lsql += " AND ";
									}
									if (caseSensitive) {
										if (realfield.equalsIgnoreCase("COMPANY")) {
											lsql += " company in (select customer_id from public.customers where description ilike '" + pt + "')";
										} else {
											lsql += " " + realfield + " like ";
										}
									} else {
										if (realfield.equalsIgnoreCase("COMPANY")) {
											lsql += " company in (select customer_id from public.customers where description ilike '" + pt + "')";
										} else {
											lsql += " upper(" + realfield + ") like ";
										}
									}
									if (!realfield.equalsIgnoreCase("COMPANY")) {
										lsql += "'" + pt + "'";
									}
									first = false;
								}
								if (lsql.length() > 0) {
									sql += " ( " + lsql + " ) ";
								}
							} else {
								if (caseSensitive) {
									sql += " " + realfield + "=";
								} else {
									sql += " upper(" + realfield + ")=";
								}
								sql += "'" + pattern + "'";
							}
						} else {
							sql += " " + realfield + "=" + pattern;
						}
					}
					ppattern = pattern;
				}
				sql += ")";

			}
			if (filter != null) {
				if (whereDone) {
					sql += " and ";
				} else {
					sql += " where ";
					whereDone = true;
				}
				sql += filter;
			}
			if (filterGroupValues != null) {
				for (int i = 0; i < filterGroupValues.size(); ++i) {
					if (!whereDone) {
						sql += " where ";
						whereDone = true;
					} else {
						sql += " and ";
					}
					String fieldname = filterGroups.get(i);
					String fieldvalue = filterGroupValues.get(i);
					sql += " " + fieldname + "='" + fieldvalue + "'";
				}
			}
			/*
			if (iscontact) {
				String whereiddomain = "";
				String wherelogin = "";
				if (!iddomain.equals("")) {
					whereiddomain = " and iddomain='" + this.iddomain + "'";
				}
				if (!login.equals("")) {
					wherelogin = " and \"login\"='" + this.login + "'";
				}
				sql += " and (category in (select category from contacts_category where checked='TRUE' " + whereiddomain + wherelogin + "))";
			}
			*/
			if (orderfields.size() > 0) {
				sql += " order by ";
				for (int i = 0; i < orderfields.size(); ++i) {
					if (i > 0) {
						sql += ",";
					}
					sql += orderfields.get(i);
				}
			}
			conn = datasource.getConnection();
			stmt = conn.createStatement();
			stmt2 = conn.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int allcolumns = rsmd.getColumnCount();
			int mailIndex = 0;
			//find column indexes for name and mail
			for (int i = 1; i <= vcolumns; ++i) {
				String cname = rsmd.getColumnName(i).toUpperCase();
				if (cname.equalsIgnoreCase(mailField)) {
					mailIndex = i - 1;
				}
			}
			result = new DirectoryResult(description, vcolumns, mailIndex, this);
			//store visible column names
			for (int i = 1; i <= vcolumns; ++i) {
				result.setColumn(i - 1, rsmd.getColumnName(i).toUpperCase());
			}
			//store values and keys
			while (rs.next()) {
				DirectoryElement de = new DirectoryElement(this, locale);
				for (int i = 1; i <= allcolumns; ++i) {
					String fieldname = rsmd.getColumnName(i).toUpperCase();
					String value = null;
					if (getType(fieldname) == DataType.DATE) {
						value = formatDateTime(rs.getTimestamp(i), locale);
						if (iscontact) {
							if (rs.getTimestamp(i) != null) {
								value = formatDate(rs.getTimestamp(i), locale);
							}
						}
					} else {
						value = rs.getString(i);
					}
					de.setField(fieldname, value);
					//store lookup value if any
					LookupData ld = lookups.get(fieldname);
					if (ld != null) {
						String sql2 = "select " + ld.viewfield + " from " + ld.table + " where " + ld.keyfield + "='" + value + "'";
						if (ld.filter != null && ld.filter.length() > 0) {
							sql2 += " and (" + ld.filter + ")";
						}
						ResultSet rs2 = stmt2.executeQuery(sql2);
						if (rs2.next()) {
							de.setLookupValue(fieldname, rs2.getString(ld.viewfield));
						}
						rs2.close();
					}
				}
				result.addElement(de);
			}
			rs.close();
			stmt2.close();
			stmt.close();
			conn.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception exc2) {
				}
			}
			if (stmt2 != null) {
				try {
					stmt2.close();
				} catch (Exception exc2) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception exc2) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception exc2) {
				}
			}
		}

		return result;
	}

	public boolean isRealFieldVisible(String field) {
		return realfields.contains(field.toUpperCase());
	}

	public String getAliasField(String realfield) {
		int ix = realfields.indexOf(realfield.toUpperCase());
		return fields.get(ix);
	}

	public String getDefaultValue(String realfield) {
		int ix = realfields.indexOf(realfield.toUpperCase());
		return defaultvalues.get(ix);
	}

	public DirectoryElement createEmptyElement(Locale locale) {
		DirectoryElement de = new DirectoryElement(this, locale);
		for (int i = 0; i < fields.size(); ++i) {
			String field = fields.get(i);
			String defaultvalue = defaultvalues.get(i);
			if (defaultvalue == null) {
				defaultvalue = "";
			}
			de.setField(field, defaultvalue);
		}
		return de;
	}

	private void saveTypesAndSizes() {
		String sql = "select ";
		int vcolumns = realfields.size();
		int kcolumns = keyfields.size();
		for (int i = 0; i < vcolumns; ++i) {
			if (i > 0) {
				sql += ",";
			}
			String realfield = realfields.get(i);
			String alias = fields.get(i);
			sql += realfield + " as " + alias;
		}
		for (int i = 0; i < kcolumns; ++i) {
			String keyfield = keyfields.get(i);
			if (!realfields.contains(keyfield)) {
				sql += "," + keyfield;
			}
		}
		sql += " from " + table + " where 1=0"; //dumb query to get field types
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			con = datasource.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();

			fieldtypes = new DataType[fields.size()];
			fieldsizes = new int[fields.size()];
			keyfieldtypes = new DataType[keyfields.size()];
			for (int i = 1; i <= rsmd.getColumnCount(); ++i) {
				int ctype = rsmd.getColumnType(i);
				DataType type = DataType.NUMERIC;
				switch (ctype) {
					case Types.CHAR:
					case Types.LONGVARCHAR:
					case Types.VARCHAR:
						type = DataType.STRING;
						break;
					case Types.DATE:
					case Types.TIME:
					case Types.TIMESTAMP:
						type = DataType.DATE;
						break;
					default:
						type = DataType.NUMERIC;
						break;
				}
				String colname = rsmd.getColumnName(i).toUpperCase();
				int fx = fields.indexOf(colname);
				String kcolname = colname;
				int kfx = keyfields.indexOf(kcolname);
				if (kfx < 0) {
					kcolname = realfields.get(fx);
					kfx = keyfields.indexOf(kcolname);
				}
				if (fx >= 0) {
					fieldtypes[fx] = type;
					if (type == DataType.NUMERIC) {
						int prec = rsmd.getPrecision(i);
						int dim = rsmd.getScale(i);
						if (prec > 0) {
							dim += prec + 1;
						}
						if (dim == 0) {
							dim = 20;
						}
						fieldsizes[fx] = dim;
					} else if (type == DataType.DATE) {
						fieldsizes[fx] = 25;
					} else {
						fieldsizes[fx] = rsmd.getColumnDisplaySize(i);
					}

				}
				if (kfx >= 0) {
					keyfieldtypes[kfx] = type;
				}
			}
			rs.close();
			stmt.close();
			con.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception exc2) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception exc2) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception exc2) {
				}
			}
		}
	}

	public void update(DirectoryElement de) throws Exception {
		update(null, de);
	}

	public void update(String fixedupdates, DirectoryElement de) throws Exception {
		if (de.getDirectoryManager() != this) {
			throw new Exception("Cannot handle this DirectoryElement (" + de + ")");
		}

		String sqlset = "";
		int ii = 0;
		String lastname = "";
		String firstname = "";
		String company = "";
		boolean blastname = false;
		boolean bfirstname = false;
		boolean bcompany = false;
		for (String field : de.getNewFields()) {
			int ix = fields.indexOf(field);
			String realfield = realfields.get(ix);
			if (ii > 0) {
				sqlset += ",";
			}
			sqlset += realfield + "=";
			DataType ft = fieldtypes[ix];
			if (ft == DataType.STRING) {
				sqlset += "'" + getSQLString(de.getNewField(field)) + "'";
			} else if (ft == DataType.DATE) {
				sqlset += getSQLDateTime(de.getNewField(field), de.getLocale());
			} else {
				sqlset += de.getNewField(field);
			}
			//!!
			if (iscontact) {
				if (realfield.equalsIgnoreCase("lastname")) {
					lastname = nvl(de.getNewField(field));
					blastname = true;
				} else if (realfield.equalsIgnoreCase("firstname")) {
					firstname = nvl(de.getNewField(field));
					bfirstname = true;
				} else if (realfield.equalsIgnoreCase("company")) {
					company = nvl(de.getNewField(field));
					bcompany = true;
				}
			}
			++ii;
		}
		if (fixedupdates != null) {
			if (ii > 0) {
				sqlset += ",";
			}
			sqlset += fixedupdates;
			++ii;
		}
		//!!
		if (iscontact) {
			for (String field : de.getFields()) {
				int ix = fields.indexOf(field);
				if (ix < 0) {
					continue;
				}
				String realfield = realfields.get(ix);
				if (!blastname && realfield.equalsIgnoreCase("lastname")) {
					lastname = nvl(de.getField(field));
				} else if (!bfirstname && realfield.equalsIgnoreCase("firstname")) {
					firstname = nvl(de.getField(field));
				} else if (!bcompany && realfield.equalsIgnoreCase("company")) {
					company = nvl(de.getField(field));
				}
			}
			sqlset += ",searchfield='" + getSQLString(lastname + firstname + getCompanyDescription(company)) + "'";
		}

		//if something to update
		if (ii > 0) {
			String sqlwhere = "";
			for (int i = 0; i < keyfields.size(); ++i) {
				String keyfield = keyfields.get(i);
				if (i > 0) {
					sqlwhere += " AND ";
				}
				sqlwhere += keyfield + "=";
				String field = keyfield;
				if (isRealFieldVisible(field)) {
					field = getAliasField(field);
				}
				if (fieldtypes[i] == DataType.STRING) {
					sqlwhere += "'" + de.getField(field) + "'";
				} else {
					sqlwhere += de.getField(field);
				}
			}
			Connection con = null;
			Statement stmt = null;
			try {
				con = datasource.getConnection();
				stmt = con.createStatement();
				String sql = "update " + table + " set " + sqlset + " where " + sqlwhere;
				if (iscontact) {
					if (con.getMetaData().getDatabaseProductName().equalsIgnoreCase("oracle")) {
						sql = "update " + table + " set LAST_MODIFIED=SYSDATE," + sqlset + " where " + sqlwhere;
					} else if (con.getMetaData().getDatabaseProductName().equalsIgnoreCase("postgresql")) {
						sql = "update " + table + " set LAST_MODIFIED=NOW()," + sqlset + " where " + sqlwhere;
					}
				}
				stmt.execute(sql);
				stmt.close();
			} catch (Exception exc) {
				exc.printStackTrace();
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (Exception exc) {
					}
				}
				if (con != null) {
					try {
						con.close();
					} catch (Exception exc) {
					}
				}
			}
		}
	}

	public void insert(DirectoryElement de) throws Exception {
		if (de.getDirectoryManager() != this) {
			throw new Exception("Cannot handle this DirectoryElement (" + de + ")");
		}

		String sqlset = "";
		ArrayList<String> insfields = new ArrayList<String>();
		ArrayList<String> insvalues = new ArrayList<String>();
		String lastname = "";
		String firstname = "";
		String company = "";
		for (String field : de.getFields()) {
			int ix = fields.indexOf(field);
			String realfield = realfields.get(ix);
			insfields.add(realfield);
			DataType ft = fieldtypes[ix];
			if (ft == DataType.NUMERIC) {
				String num = de.getField(field);
				String snum = getSQLNumber(num);
				insvalues.add(snum);
			} else if (ft == DataType.STRING) {
				insvalues.add("'" + getSQLString(de.getField(field)) + "'");
			} else if (ft == DataType.DATE) {
				insvalues.add(getSQLDateTime(de.getField(field), de.getLocale()));
			} else {
				insvalues.add(de.getField(field));
			}

			//!!!
			if (iscontact) {
				if (realfield.equalsIgnoreCase("lastname")) {
					lastname = nvl(de.getField(field));
				} else if (realfield.equalsIgnoreCase("firstname")) {
					firstname = nvl(de.getField(field));
				} else if (realfield.equalsIgnoreCase("company")) {
					company = nvl(de.getField(field));
				}
			}
		}
		insfields.addAll(fixedinsertfields);
		insvalues.addAll(fixedinsertvalues);
		Connection con = null;
		Statement stmt = null;
		try {
			con = datasource.getConnection();
			stmt = con.createStatement();
			String sql = "insert into " + table + " (";
			int i = 0;
			for (String insfield : insfields) {
				if (i > 0) {
					sql += ",";
				}
				sql += insfield;
				++i;
			}
			//!!!!!
			if (iscontact) {
				sql += ",searchfield,last_modified";
				//!!!!!
			}
			sql += ") values (";
			i = 0;
			for (String insvalue : insvalues) {
				if (i > 0) {
					sql += ",";
				}
				sql += insvalue;
				++i;
			}
			//!!!!
			if (iscontact) {
				if (con.getMetaData().getDatabaseProductName().equalsIgnoreCase("oracle")) {
					sql += ",'" + getSQLString(lastname + firstname + getCompanyDescription(company)) + "',SYSDATE";
				} else if (con.getMetaData().getDatabaseProductName().equalsIgnoreCase("postgresql")) {
					sql += ",'" + getSQLString(lastname + firstname + getCompanyDescription(company)) + "',NOW()";
				}
				//!!!!!
			}
			sql += ")";
			stmt.execute(sql);
			stmt.close();
		} catch (SQLException exc) {
			exc.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception exc) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception exc) {
				}
			}
		}
	}

	public void delete(DirectoryElement de) throws Exception {
		if (de.getDirectoryManager() != this) {
			throw new Exception("Cannot handle this DirectoryElement (" + de + ")");
		}

		String sqlwhere = "";
		for (int i = 0; i < keyfields.size(); ++i) {
			String keyfield = keyfields.get(i);
			if (i > 0) {
				sqlwhere += " AND ";
			}
			sqlwhere += keyfield + "=";
			String field = keyfield;
			if (isRealFieldVisible(field)) {
				field = getAliasField(field);
			}
			if (fieldtypes[i] == DataType.STRING) {
				sqlwhere += "'" + de.getField(field) + "'";
			} else {
				sqlwhere += de.getField(field);
			}
		}
		String sql = "delete from " + table + " where " + sqlwhere;
		Connection con = null;
		Statement stmt = null;
		try {
			con = datasource.getConnection();
			stmt = con.createStatement();
			stmt.execute(sql);
			stmt.close();
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception exc) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception exc) {
				}
			}
		}
	}

	public DirectoryGroup[] getGroups() {
		DirectoryGroup xgroups[] = new DirectoryGroup[groups.size()];
		groups.toArray(xgroups);
		return xgroups;
	}

	public String[] getFields(DirectoryGroup g) {
		int x1 = g.getStartIndex();
		int x2 = g.getEndIndex() + 1;
		String gfields[] = new String[x2 - x1];
		for (int i = 0; i < gfields.length; ++i) {
			gfields[i] = fields.get(x1 + i);
		}
		return gfields;
	}

	private String nvl(String s) {
		if (s == null) {
			return "";
		}
		return s;
	}

	private String getSQLString(String content) {
		if (content == null) {
			return null;
		}
		int ix = 0;
		while ((ix = content.indexOf('\'', ix)) >= 0) {
			String s1 = content.substring(0, ix);
			String s2 = content.substring(ix + 1);
			content = s1 + "''" + s2;
			ix += 2;
		}
		return content;
	}

	private String getSQLNumber(String content) {
		if (content == null) {
			return null;
		}
		if (content.trim().length() == 0) {
			return "0";
		}
		return content;
	}

	private String getSQLDateTime(String content, Locale locale) throws ParseException {
//    content=content.replace(':','.');
		String content2 = content.replace('-', '/');
		String value = null;
		java.util.Date date = null;

		try {
			date = getSSDateFormat(locale).parse(content);
		} catch (ParseException exc) {
		}
		if (date == null) {
			try {
				date = getSSDateFormat(locale).parse(content2);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getSMDateFormat(locale).parse(content);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getSMDateFormat(locale).parse(content2);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getSLDateFormat(locale).parse(content);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getSLDateFormat(locale).parse(content2);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getMSDateFormat(locale).parse(content);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getMSDateFormat(locale).parse(content2);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getMMDateFormat(locale).parse(content);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getMMDateFormat(locale).parse(content2);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getMLDateFormat(locale).parse(content);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getMLDateFormat(locale).parse(content2);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getLSDateFormat(locale).parse(content);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getLSDateFormat(locale).parse(content2);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getLMDateFormat(locale).parse(content);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getLMDateFormat(locale).parse(content2);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getLLDateFormat(locale).parse(content);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getLLDateFormat(locale).parse(content2);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getSDateFormat(locale).parse(content);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getSDateFormat(locale).parse(content2);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getMDateFormat(locale).parse(content);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getMDateFormat(locale).parse(content2);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			try {
				date = getLDateFormat(locale).parse(content);
			} catch (ParseException exc) {
			}
		}
		if (date == null) {
			if (!content2.equals("")) {
				date = getLDateFormat(locale).parse(content2);
			}

		}
		synchronized (enCalendar) {
			if (date != null) {
				enCalendar.setTime(date);
				value = "{ts '" + enCalendar.get(Calendar.YEAR) + "-" + (enCalendar.get(Calendar.MONTH) + 1) + "-"
						+ enCalendar.get(Calendar.DAY_OF_MONTH) + " "
						+ enCalendar.get(Calendar.HOUR_OF_DAY) + ":" + enCalendar.get(Calendar.MINUTE) + ":" + enCalendar.get(Calendar.SECOND)
						+ "'}";
			} else {
				value = "null";
			}
		}
		return value;
	}

	private String formatDate(java.util.Date date, Locale locale) {
		String value = null;
		DateFormat df = getSSDateFormat(locale);
		if (date != null) {
			value = df.format(date);
		}
		return value;
	}

	private String formatDateTime(java.util.Date date, Locale locale) {
		String value = null;
		DateFormat df = getMMDateFormat(locale);
		if (date != null) {
			value = df.format(date);
		}
		return value;
	}

	private DateFormat getSSDateFormat(Locale locale) {
		prepareDateFormat(locale);
		return ss_dateFormatters.get(locale);
	}

	private DateFormat getSMDateFormat(Locale locale) {
		prepareDateFormat(locale);
		return sm_dateFormatters.get(locale);
	}

	private DateFormat getSLDateFormat(Locale locale) {
		prepareDateFormat(locale);
		return sl_dateFormatters.get(locale);
	}

	private DateFormat getMSDateFormat(Locale locale) {
		prepareDateFormat(locale);
		return ms_dateFormatters.get(locale);
	}

	private DateFormat getMMDateFormat(Locale locale) {
		prepareDateFormat(locale);
		return mm_dateFormatters.get(locale);
	}

	private DateFormat getMLDateFormat(Locale locale) {
		prepareDateFormat(locale);
		return ml_dateFormatters.get(locale);
	}

	private DateFormat getLSDateFormat(Locale locale) {
		prepareDateFormat(locale);
		return ls_dateFormatters.get(locale);
	}

	private DateFormat getLMDateFormat(Locale locale) {
		prepareDateFormat(locale);
		return lm_dateFormatters.get(locale);
	}

	private DateFormat getLLDateFormat(Locale locale) {
		prepareDateFormat(locale);
		return ll_dateFormatters.get(locale);
	}

	private DateFormat getSDateFormat(Locale locale) {
		prepareDateFormat(locale);
		return s_dateFormatters.get(locale);
	}

	private DateFormat getMDateFormat(Locale locale) {
		prepareDateFormat(locale);
		return m_dateFormatters.get(locale);
	}

	private DateFormat getLDateFormat(Locale locale) {
		prepareDateFormat(locale);
		return l_dateFormatters.get(locale);
	}

	private void prepareDateFormat(Locale locale) {
		synchronized (ss_dateFormatters) {
			DateFormat df = ss_dateFormatters.get(locale);
			if (df == null) { //save for future re-use
				df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
				ss_dateFormatters.put(locale, df);
				df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, locale);
				sm_dateFormatters.put(locale, df);
				df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG, locale);
				sl_dateFormatters.put(locale, df);
				df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT, locale);
				ms_dateFormatters.put(locale, df);
				df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
				mm_dateFormatters.put(locale, df);
				df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG, locale);
				ml_dateFormatters.put(locale, df);
				df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT, locale);
				ls_dateFormatters.put(locale, df);
				df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM, locale);
				lm_dateFormatters.put(locale, df);
				df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
				ll_dateFormatters.put(locale, df);
				df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
				s_dateFormatters.put(locale, df);
				df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
				m_dateFormatters.put(locale, df);
				df = DateFormat.getDateInstance(DateFormat.LONG, locale);
				l_dateFormatters.put(locale, df);
			}
		}
	}

	public List<LookupRecord> getLookupRecords(String field) throws SQLException {
		LookupData ld = lookups.get(field);
		if (ld == null) {
			return null;
		}
		ArrayList<LookupRecord> data = new ArrayList<LookupRecord>();
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = datasource.getConnection();
			stmt = conn.createStatement();
			String sql = "select " + ld.keyfield + "," + ld.viewfield + " from " + ld.table;
			if (ld.filter != null & ld.filter.length() > 0) {
				sql += " where (" + ld.filter + ")";
			}
			sql += " order by " + ld.viewfield;
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				LookupRecord lr = new LookupRecord();
				lr.keyvalue = rs.getString(ld.keyfield);
				lr.viewvalue = rs.getString(ld.viewfield);
				data.add(lr);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception exc) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception exc) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception exc) {
				}
			}
		}
		rs.close();
		stmt.close();
		conn.close();
		return data;
	}

	public void addFilterGroup(String field, HashMap<String, String> descriptions) {
		filterGroups.add(field);
		filterGroupsDescriptions.add(descriptions);
	}

	public boolean hasFilterGroups() {
		return filterGroups.size() > 0;
	}

	public List<String> getFilterGroups() {
		return filterGroups;
	}

	public List<String> getFilterGroupValues(int index) {
		ArrayList<String> values = new ArrayList<String>();
		if (index < filterGroups.size()) {
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				String fieldname = filterGroups.get(index);
				String sql = "select " + fieldname;
				sql += " from " + table;
				if (filter != null) {
					sql += " where " + filter;
				}
				sql += " group by " + fieldname;
				conn = datasource.getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					String value = rs.getString(fieldname);
					values.add(value);
				}
			} catch (Exception exc) {
				exc.printStackTrace();
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (Exception exc) {
					}
				}
				if (stmt != null) {
					try {
						stmt.close();
					} catch (Exception exc) {
					}
				}
				if (conn != null) {
					try {
						conn.close();
					} catch (Exception exc) {
					}
				}
			}

		}
		return values;
	}

	public HashMap<String, String> getFilterGroupDescriptions(int index) {
		return filterGroupsDescriptions.get(index);
	}

	public String getCompanyDescription(String customer_id) {

		Connection con = null;
		Statement stmt = null;
		ResultSet rset = null;
		String description = customer_id;
		try {
			if (description != null && !description.equals("")) {
				String query = "select description from public.customers where customer_id='" + customer_id + "' and (status is null or status!='D')";
				con = datasource.getConnection();
				stmt = con.createStatement();
				rset = stmt.executeQuery(query);
				if (rset.next()) {
					description = rset.getString("description");
				}
			}
		} catch (SQLException exc) {
			exc.printStackTrace();
		} finally {
			if (rset != null) {
				try {
					rset.close();
				} catch (Exception exc) {
				}
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception exc) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception exc) {
				}
			}
		}
		return description;

	}

}
