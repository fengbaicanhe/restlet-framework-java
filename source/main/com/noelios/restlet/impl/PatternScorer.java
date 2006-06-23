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

package com.noelios.restlet.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.restlet.Call;
import org.restlet.AbstractScorer;
import org.restlet.Restlet;
import org.restlet.Router;

/**
 * Router handler based on a URI pattern.
 * @see java.util.regex.Pattern
 * @author Jerome Louvel (contact@noelios.com) <a href="http://www.noelios.com/">Noelios Consulting</a>
 */
public class PatternScorer extends AbstractScorer
{
   /** Obtain a suitable logger. */
   private static Logger logger = Logger.getLogger(PatternScorer.class.getCanonicalName());

   /** The URI pattern. */
   Pattern pattern;

   /**
    * Constructor.
    * @param router The parent router.
    * @param pattern The URI pattern.
    * @param target The Restlet target.
    */
   public PatternScorer(Router router, String pattern, Restlet target)
   {
      super(router, target);
      this.pattern = Pattern.compile(pattern);
   }

   /**
    * Returns the URI pattern.
    * @return The URI pattern.
    */
   public Pattern getPattern()
   {
      return this.pattern;
   }
	
	/**
	 * Returns the score for a given call (between 0 and 1.0).
	 * @param call The call to score.
	 * @return The score for a given call (between 0 and 1.0).
	 */
	public float score(Call call)
	{
		float result = 0F;
		String resourcePath = call.getResourcePath();
		Matcher matcher = getPattern().matcher(resourcePath);
      boolean matched = matcher.lookingAt();
         
      if(logger.isLoggable(Level.FINER))
      {
      	logger.finer("Attempting to match this pattern: " + getPattern().toString() + " >> " + matched);
      }

      if(matched)
      {
      	float totalLength = resourcePath.length();
      	float matchedLength = matcher.end();
      	result = getRouter().getRequiredScore() + (1.0F - getRouter().getRequiredScore()) * (matchedLength/totalLength);
      }

      return result;
	}
	
	/**
	 * Handles the call.
	 * @param call The call to handle.
	 */
	public void handle(Call call)
	{
		String resourcePath = call.getResourcePath();
		Matcher matcher = getPattern().matcher(resourcePath);
      boolean matched = matcher.lookingAt();
         
      if(logger.isLoggable(Level.FINER))
      {
      	logger.finer("Attempting to match this pattern: " + getPattern().toString() + " >> " + matched);
      }

      if(matched)
      {
	      if(logger.isLoggable(Level.FINER))
	      {
	      	logger.finer("A matching target was found");
	      }
	
	      // Updates the paths
	      String oldContextPath = call.getContextPath();
	      String newContextPath = resourcePath.substring(0, matcher.end());
	
	      if(oldContextPath == null)
	      {
	         call.setContextPath(newContextPath);
	      }
	      else
	      {
	         call.setContextPath(oldContextPath + newContextPath);
	      }
	
	      if(logger.isLoggable(Level.FINE))
	      {
	      	logger.fine("New context path: " + call.getContextPath());
	      	logger.fine("New resource path: " + call.getResourcePath());
	      }
	
	      // Updates the matches
	      call.getContextMatches().clear();
	      for(int i = 0; i < matcher.groupCount(); i++)
	      {
	         call.getContextMatches().add(matcher.group(i + 1));
	      }
	
	      if(logger.isLoggable(Level.FINE))
	      {
	      	logger.fine("Delegating the call to the target Restlet");
	      }
	
	      // Invoke the call restlet
	      super.handle(call);
	   }
	}   
}
