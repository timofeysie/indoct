package com.businessglue.struts.options;

import org.apache.struts.action.ActionForm;


/**
 * Edit the <user_id>/<user_id>.options file:
exclude_level0_test
exclude_level1_test
exclude_level2_test
exclude_level3_test
 *
*/
public final class EditELTOptionsForm extends ActionForm
{
    private String exclude_level0_test;
    private String exclude_level1_test;
    private String exclude_level2_test;
    private String exclude_level3_test;

    /** Level 0 */
    public void setExcludeLevel0Test(String _exclude_level0_test) 
    {
        this.exclude_level0_test = _exclude_level0_test;
    }

    public String getExcludeLevel0Test() 
    {
       return exclude_level0_test;	
    }
    
    
    /** Level 1 */
    public void setExcludeLevel1Test(String _exclude_level1_test) 
    {
        this.exclude_level1_test = _exclude_level1_test;
    }

    public String getExcludeLevel1Test() 
    {
       return exclude_level1_test;	
    }
    
    /** Level 2 */
    public void setExcludeLevel2Test(String _exclude_level2_test) 
    {
        this.exclude_level2_test = _exclude_level2_test;
    }

    public String getExcludeLevel2Test() 
    {
       return exclude_level2_test;	
    }
    
    /** Level 3 */
    public void setExcludeLevel3Test(String _exclude_level3_test) 
    {
        this.exclude_level3_test = _exclude_level3_test;
    }

    public String getExcludeLevel3Test() 
    {
       return exclude_level3_test;	
    }

}
