/*******************************************************************************
 * Copyright (c) 1999-2011 Percussion Software.
 * 
 * Permission is hereby granted, free of charge, to use, copy and create derivative works of this software and associated documentation files (the "Software") for internal use only and only in connection with products from Percussion Software. 
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL PERCUSSION SOFTWARE BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ******************************************************************************/
/*
 * com.percussion.pso.dynreog.assembler ContentMerge.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.dynreorg.assembler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.percussion.services.assembly.IPSAssemblyItem;
import com.percussion.services.assembly.IPSAssemblyResult;
import com.percussion.services.assembly.data.PSAssemblyWorkItem;

import com.percussion.util.IPSHtmlParameters;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashMap;



/**
 * Merges dynamic content into an Assembly Result generated by the
 * Rhythmyx assembler.  
 * 
 * This class will be used by all dynamic assemblers
 * that encapsulate the output from the template (e.g. the Velocity
 * and Binary assemblers).  Dynamic Assemblers which produce a different
 * body (e.g. the Test Assembler) must use a different method.
 * <p>
 * The Assembly Result must contain a <code>DeliveryItem</code> bound
 * to <code>$dynapub</code>. 
 * <p>
 * The Assembly Result will be returned unmodified if:
 * <ul>
 * <li>The Debug Assembler was specified
 * <li>Preview mode is used (sys_context is 0 or missing)
 * </ul>    
 *
 * @author DavidBenua
 *
 */
public class ContentMerge
{
   /**
    * Logger for this class
    */
   private static final Log log = LogFactory.getLog(ContentMerge.class);
   
  
   /**
    * Static methods only, never constructed.  
    */
   private ContentMerge()
   {
   }
   
   /**
    * Merges dynamic properties into the Assembly Result.
    *
    * @param result the assembly result from the original assembler
    * @return the merged Assembly Result.
    * @throws Exception when <code>$dynapub</code> was not found, or is not
    * a <code>DeliveryItem</code> 
    */
   public static IPSAssemblyResult merge(IPSAssemblyResult result) 
      throws Exception 
   {
      if(result.isDebug())
      {
         log.debug("Debug Assembly enabled"); 
         return result;
      }
      String contextStr = result.getParameterValue(IPSHtmlParameters.SYS_CONTEXT, null);
      int context;
      if(contextStr == null || contextStr.equals("0"))
      {
         log.debug("Preview Mode");
         context=0;
      } else {
    	 context=1; // Modify if we are checking for contexts > 1
      }
     
   
      IPSAssemblyItem parent = result.getCloneParentItem();
      boolean isSnippet;
      if (parent != null) {
    	  log.debug("Not processing. Item "+ result.getId() + " has parent Item id="+parent.getId());
    	  isSnippet = true;
      } else {
    	  log.debug("No Parent Item Processing Item id="+ result.getId());
    	  isSnippet = false;
      }
     
      
      
      String mimeType = result.getMimeType();
     
      String outputDoc = result.toResultString();
      
      PSAssemblyWorkItem work = (PSAssemblyWorkItem) result; 
     
      if (!isSnippet) {
      String cBlockRegEx="<!--\\s?codeblock(:([^-\\s]*))?(-([0-9]*))?\\s*-->(.*?)<!--\\s?end codeblock\\s?-->";
      String cBlockInsertRegEx="<!--\\s?insert-codeblock:([^-\\s]*)\\s*-->";
     
      Pattern pattern = 
       Pattern.compile(cBlockRegEx,Pattern.DOTALL|Pattern.MULTILINE|Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE);
  
   
        		  
       Matcher matcher = pattern.matcher(outputDoc);
      
      
       int i=0;
       StringBuffer sb = new StringBuffer();
       
   
       HashMap<String,String> locationNames = new HashMap<String,String>();
       while (matcher.find() && i<20) {
    	   i++;
    	   
    	   String locationName=matcher.group(2);
    	   String matchContextStr=matcher.group(4);
    	   String matchContent=matcher.group(5);
    	   int matchContext = -1;
    	   if (matchContextStr!=null && matchContextStr.length()>0) {
    		   matchContext=Integer.valueOf(matchContextStr);
    	   }
    	   if (matchContext<0 || matchContext==context) {
    		   if (locationName!=null && locationName.length()>0) {
    			   
    			   String orignalValue="";
    			   if (locationNames.containsKey(locationName)){
    				   orignalValue=locationNames.get(locationName);
    			   }
    			   locationNames.put(locationName,orignalValue+matchContent);
    			   // store location content
    			
    			   matcher.appendReplacement(sb,"");
    		} else {
    			 matcher.appendReplacement(sb, "$5");
    		   }
    	   } else {
    		   matcher.appendReplacement(sb, "");
    	   }
    	  
       }
       	matcher.appendTail(sb);
       	
       
       pattern = 
           Pattern.compile(cBlockInsertRegEx,Pattern.DOTALL|Pattern.MULTILINE|Pattern.CASE_INSENSITIVE|Pattern.UNICODE_CASE);
       matcher = pattern.matcher(sb);
       
       StringBuffer sb2 = new StringBuffer();
       i=0;
       while (matcher.find() && i<20) {
    	   i++;
    	   String locationName=matcher.group(1);
    	   if (locationNames.containsKey(locationName)){
    		   matcher.appendReplacement(sb2,locationNames.get(locationName));
    	   } else {
    		   matcher.appendReplacement(sb2,"");
    	   }
    	   
       }
            		 
      matcher.appendTail(sb2);
      
          
      	work.setResultData(sb2.toString().getBytes("UTF-8"));
      } else {
    	work.setResultData(outputDoc.getBytes("UTF-8"));
      }
      
      work.setMimeType(mimeType); 
    
      return work;
   }
}