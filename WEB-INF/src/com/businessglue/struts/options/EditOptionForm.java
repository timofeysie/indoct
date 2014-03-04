/*
 * Copyright 2000-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.businessglue.struts.options;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;


/**
 * Form bean for the user registration page.
 *
*/
public final class EditOptionForm extends ValidatorForm implements Serializable 
{
    private String action = null;
       
    private String edit_option = null;

    public String getAction() 
    {
	return action;
    }

    public void setAction(String action) 
    {
        this.action = action;
    }

    public String getEditOption() 
    {
       return edit_option;	
    }
    
    public void setEditOption(String edit_option) 
    {
       	this.edit_option = edit_option;
    }
    
        
    /**
     * Reset all properties to their default values.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) 
    {
       action = null;
       edit_option = null;
    }

}
