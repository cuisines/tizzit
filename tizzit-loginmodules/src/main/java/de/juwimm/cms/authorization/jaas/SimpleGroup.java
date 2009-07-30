/**
 * Copyright (c) 2009 Juwi MacMillan Group GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.juwimm.cms.authorization.jaas;

import java.security.Principal;
import java.security.acl.Group;
import java.util.*;

/**
 * <p>
 * Title: ConQuest
 * </p>
 * <p>
 * Description: Enterprise Content Management
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: JuwiMacMillan Group
 * </p>
 * 
 * @author <a href="s.kulawik@juwimm.com">Sascha-Matthias Kulawik</a>
 * @version 1.0 An implementation of Group that manages a collection of
 *          Principal objects based on their hashCode() and equals() methods.
 *          This class is not thread safe.
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
public class SimpleGroup extends SimplePrincipal implements Group {
	private static final long serialVersionUID = 3718708181789549104L;

	private HashMap members;

	public SimpleGroup(String groupName) {
		super(groupName);
		members = new HashMap(3);
	}

	/**
	 * Adds the specified member to the group.
	 * 
	 * @param user
	 *            the principal to add to this group.
	 * @return true if the member was successfully added, false if the principal
	 *         was already a member.
	 */
	public boolean addMember(Principal user) {
		boolean isMember = members.containsKey(user);
		if (!isMember) {
			members.put(user, user);
		}
		return !isMember;
	}

	/**
	 * Returns true if the passed principal is a member of the group. This
	 * method does a recursive search, so if a principal belongs to a group
	 * which is a member of this group, true is returned. A special check is
	 * made to see if the member is an instance of
	 * org.jboss.security.AnybodyPrincipal or org.jboss.security.NobodyPrincipal
	 * since these classes do not hash to meaningful values.
	 * 
	 * @param member
	 *            the principal whose membership is to be checked.
	 * @return true if the principal is a member of this group, false otherwise.
	 */
	public boolean isMember(Principal member) {
		// First see if there is a key with the member name
		boolean isMember = members.containsKey(member);
		if (!isMember) { // Check the AnybodyPrincipal & NobodyPrincipal
							// special cases
			isMember = (member instanceof AnybodyPrincipal);
			if (!isMember) {
				if (member instanceof NobodyPrincipal) {
					return false;
				}
			}
		}
		if (!isMember) { // Check any Groups for membership
			Collection values = members.values();
			Iterator iter = values.iterator();
			while (!isMember && iter.hasNext()) {
				Object next = iter.next();
				if (next instanceof Group) {
					Group group = (Group) next;
					isMember = group.isMember(member);
				}
			}
		}
		return isMember;
	}

	/**
	 * Returns an enumeration of the members in the group. The returned objects
	 * can be instances of either Principal or Group (which is a subinterface of
	 * Principal).
	 * 
	 * @return an enumeration of the group members.
	 */
	public Enumeration members() {
		return Collections.enumeration(members.values());
	}

	/**
	 * Removes the specified member from the group.
	 * 
	 * @param user
	 *            the principal to remove from this group.
	 * @return true if the principal was removed, or false if the principal was
	 *         not a member.
	 */
	public boolean removeMember(Principal user) {
		Object prev = members.remove(user);
		return prev != null;
	}

	@Override
	public String toString() {
		StringBuffer tmp = new StringBuffer(getName());
		tmp.append("(members:");
		Iterator iter = members.keySet().iterator();
		while (iter.hasNext()) {
			tmp.append(iter.next());
			tmp.append(',');
		}
		tmp.setCharAt(tmp.length() - 1, ')');
		return tmp.toString();
	}

}
