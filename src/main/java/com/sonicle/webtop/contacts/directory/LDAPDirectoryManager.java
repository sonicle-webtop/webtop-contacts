/*
* WebTop Groupware is a bundle of WebTop Services developed by Sonicle S.r.l.
* Copyright (C) 2011 Sonicle S.r.l.
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
* FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
* details.
*
* You should have received a copy of the GNU Affero General Public License
* along with this program; if not, see http://www.gnu.org/licenses or write to
* the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA 02110-1301 USA.
*
* You can contact Sonicle S.r.l. at email address sonicle@sonicle.com
*
* The interactive user interfaces in modified source and object code versions
* of this program must display Appropriate Legal Notices, as required under
* Section 5 of the GNU Affero General Public License version 3.
*
* In accordance with Section 7(b) of the GNU Affero General Public License
* version 3, these Appropriate Legal Notices must retain the display of the
* "Powered by Sonicle WebTop" logo. If the display of the logo is not reasonably
* feasible for technical reasons, the Appropriate Legal Notices must display
* the words "Powered by Sonicle WebTop".
*/

package com.sonicle.webtop.contacts.directory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Locale;
import javax.naming.*;
import javax.naming.directory.*;


public class LDAPDirectoryManager implements DirectoryManager {

    String  id;
    String  description;
    String ldapServer=null;
    int ldapPort;
    String searchBase=null;
    Hashtable env=null;
	DirectoryGroup groups[]=new DirectoryGroup[] {new DirectoryGroup("")};

	String nameField="name";
	String mailField="mail";
	ArrayList<String> fields=new ArrayList<String>();

  /**
   *  The LDAP server name or IP address, the port and the search
   *  base must be specified when calling the constructor.
   */
  public LDAPDirectoryManager(String id, String description, String server,int port,String srchB) {

		this.id=id;
		this.description=description;
    ldapServer=server;
    ldapPort=port;
    searchBase=srchB;

    env = new Hashtable(5, 0.75f);

    env.put(Context.INITIAL_CONTEXT_FACTORY,
            "com.sun.jndi.ldap.LdapCtxFactory");

		fields.add(nameField);
		fields.add(mailField);
  }//end constructor



    public String getId() {
            return id;
    }

    public String getDescription() {
            return description;
    }

    public List<String> getFields() {
            return fields;
    }

    public List<String> getKeyFields() {
            return null;
    }

    public boolean isRealFieldVisible(String field) {
      return false;
    }

    public String getAliasField(String realfield) {
      return null;
    }

	public List<String> getSearchFields() {
		return null;
	}

	public String getMailField() {
		return mailField;
	}

        public String getFaxField() {
            return null;
        }

	public String getFirstNameField() {
		return null;
	}

	public String getLastNameField() {
		return null;
	}
	
	public String getCompanyField() {
		return null;
	}

	public boolean isClickField(String field) {
		return field.equalsIgnoreCase(nameField);
	}

	public DirectoryResult lookup(String pattern, Locale locale,boolean isAnd, boolean caseSensitive) {
		return lookup(nameField,pattern, locale, isAnd,caseSensitive);
	}

    public DirectoryResult lookup(String searchfield, String pattern, Locale locale, boolean isAnd, boolean caseSensitive) {
        ArrayList<String> vsf=new ArrayList<String>();
        ArrayList<String> vp=new ArrayList<String>();
        vsf.add(searchfield);
        vp.add(pattern);
        return lookup(vsf,vp, locale, isAnd,caseSensitive,false);
    }

  /**
   * Performs a directory search and returns the results in a Java Vector.
   * The string to search for is passed as parameter.
   */
  public DirectoryResult lookup(List<String> searchfields, List<String> patterns, Locale locale, boolean isAnd, boolean caseSensitive, boolean distinct) {
		String searchfield=searchfields.get(0);
		String pattern=patterns.get(0);
                    if (pattern.length()<5) {
                        DirectoryResult dr=new DirectoryResult(description,2,1,this);
			dr.setColumn(0,nameField);
			dr.setColumn(1,mailField);
                        DirectoryElement de=new DirectoryElement(this,locale);
                        de.setField(nameField,"more chars");
                        de.setField(mailField,"too few chars");
                        dr.addElement(de);
                        return dr;
                    }

    DirectoryResult search_result=null;

    /* Specify host and port to use for directory service */
    env.put(Context.PROVIDER_URL, "ldap://"+ldapServer+":"+ldapPort);

    try {
      /* get a handle to an Initial DirContext */
      DirContext ctx = new InitialDirContext(env);

      /* specify search constraints to search subtree */
      SearchControls constraints = new SearchControls();
      constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);

      /* search for all entries with surname of Jensen */
      NamingEnumeration results
        = ctx.search(searchBase, "(cn=*"+pattern+"*)", constraints);

      search_result=new DirectoryResult(description,2,1,this);
			search_result.setColumn(0,nameField);
			search_result.setColumn(1,mailField);

      /* for each entry add the attributes object into the results vector*/
      while (results != null && results.hasMore()) {
        SearchResult si = (SearchResult)results.next();
				BasicAttributes nameAttrs=new BasicAttributes();
				StringTokenizer st=new StringTokenizer(si.getName(),",");
				while(st.hasMoreTokens()) {
					String entry=st.nextToken();
					StringTokenizer st2=new StringTokenizer(entry,"=");
					String id=st2.nextToken();
					String value=st2.nextToken();
					nameAttrs.put(id,value);
				}

        Attributes attrs = si.getAttributes();
        if (attrs != null) {
					Attribute cnattr=attrs.get("cn");
					Attribute mailattr=attrs.get("mail");
					if (cnattr==null) cnattr=nameAttrs.get("cn");
					if (mailattr==null) mailattr=nameAttrs.get("mail");
					String sname=null;
					String smail=null;
					if (cnattr!=null) sname=(String)cnattr.get();
					if (mailattr!=null) smail=(String)mailattr.get();
					if (sname!=null) {
					  if (sname.startsWith("\"")) {
								sname=sname.substring(1);
								if (sname.endsWith("\"")) {
								  sname=sname.substring(0,sname.length()-1);
								}
					  } else if (sname.startsWith("'")) {
								sname=sname.substring(1);
								if (sname.endsWith("'")) {
								  sname=sname.substring(0,sname.length()-1);
								}
					  }
						DirectoryElement de=new DirectoryElement(this,locale);
						de.setField(nameField,sname);
						de.setField(mailField,smail);
						search_result.addElement(de);
					}
        }//end if

      }//end while

    }
    catch (NamingException e) {
			e.printStackTrace();
      System.err.println("Search failed.");
    }


    //return the search results
    return search_result;

  }//end lookupdir

	public DirectoryType getType() {
		return DirectoryType.GLOBAL;
	}

	public int getSize(String field) {
		return 100;
	}

	public boolean isWritable() {
		return false;
	}

  public boolean isListField(int fieldIndex) {
    return true;
  }

  public int getListColumnCount() {
    return 2;
  }

	public void update(DirectoryElement de) throws Exception {
		throw new Exception("Directory is read only");
	}

	public void insert(DirectoryElement de) throws Exception {
		throw new Exception("Directory is read only");
	}

	public void delete(DirectoryElement de) throws Exception {
		throw new Exception("Directory is read only");
	}

	public DirectoryElement createEmptyElement(Locale locale) {
		DirectoryElement de=new DirectoryElement(this,locale);
		for(String field: fields) de.setField(field,"");
		return de;
	}

	public DirectoryGroup[] getGroups() {
		return groups;
	}

	public String[] getFields(DirectoryGroup g) {
        String f[]=new String[fields.size()];
		return fields.toArray(f);
	}


}//end class
