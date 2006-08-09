/*
 * Copyright 2005-2006 Noelios Consulting.
 *
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the "License").  You may not use this file except
 * in compliance with the License.
 *
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.txt
 * See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * HEADER in each file and include the License file at
 * http://www.opensource.org/licenses/cddl1.txt
 * If applicable, add the following below this CDDL
 * HEADER, with the fields enclosed by brackets "[]"
 * replaced with your own identifying information:
 * Portions Copyright [yyyy] [name of copyright owner]
 */

package org.restlet.connector;

import java.util.Arrays;

import org.restlet.Factory;
import org.restlet.data.Protocol;
import org.restlet.data.WrapperMap;

/**
 * Modifiable map of client connectors.
 * @author Jerome Louvel (contact@noelios.com) <a href="http://www.noelios.com/">Noelios Consulting</a>
 */
public class ClientMap extends WrapperMap<String, Client>
{
	/**
	 * Puts a new client connector in the map supporting the given protocol.
	 * @param key The map key.
	 * @param protocol The connector protocol.
	 * @return The added client.
	 */
	public Client put(String key, Protocol protocol)
	{
		return put(key, Factory.getInstance().createClient(Arrays.asList(protocol), null, null));
	}
}
