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
 * com.percussion.pso.dynreorg.assembler VelocityContentAssembler.java
 *  
 * @author DavidBenua
 *
 */
package com.percussion.pso.dynreorg.assembler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.percussion.services.assembly.IPSAssembler;
import com.percussion.services.assembly.IPSAssemblyItem;
import com.percussion.services.assembly.IPSAssemblyResult;
import com.percussion.services.assembly.impl.plugin.PSVelocityAssembler;

/**
 * Provides a dynamic assembler for Velocity based templates. 
 * This assembler uses the standard <code>ContentMerge</code> routine, which defines
 * how the assembly result in generated. 
 * <p>  
 * All templates that use this assembler must bind <code>$dynapub</code>. 
 * to an object that implements <code>DeliveryItem</code>. This item will
 * carry the metadata and other properties needed for dynamic publishing.  
 *  
 * @author DavidBenua
 * 
 */
public class VelocityContentAssembler extends PSVelocityAssembler
      implements
         IPSAssembler
{
   /**
    * Logger for this class
    */
   private static final Log log = LogFactory.getLog(VelocityContentAssembler.class);
   
   
   /**
    * 
    */
   public VelocityContentAssembler()
   {
      super();
   }

   /**
    * @see com.percussion.services.assembly.impl.plugin.PSAssemblerBase#doAssembleSingle(com.percussion.services.assembly.IPSAssemblyItem)
    */
   @Override
   protected IPSAssemblyResult doAssembleSingle(IPSAssemblyItem item) throws Exception
   {
      IPSAssemblyResult result = super.doAssembleSingle(item); 
      log.debug("Velocity Content Assembler"); 
      return ContentMerge.merge(result);   
   }
   
   
}
