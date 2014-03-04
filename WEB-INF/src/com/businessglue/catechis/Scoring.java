package com.businessglue.catechis;

import java.lang.Double;

// used for logging purposes
import javax.servlet.ServletContext;

public class Scoring
{

	private ServletContext context;

	public Scoring()
	{}

	public Scoring(ServletContext _context)
	{
		this.context = _context;
	}

	public String getPercentageScore(double index, double score)
	{
		//  index 7 score 10 string ratio 1.0 ratio 1.0 scorepercentage_score 100.0
		double ratio = (score / index);
		Double d_ratio = new Double(ratio);
		String string_ratio = d_ratio.toString();
		// ratio = f_ratio.intValue();
		Double percentage_score = new Double(ratio * 100);
		context.log("*** Scoring.getPercentageScore: index "+index+" score "+score+" string ratio "+string_ratio+" ratio "+ratio+" scorepercentage_score "+percentage_score);
		String new_score = percentage_score.toString();
		return new_score;
	}
	
	public String getRatioString(int index, int score)
	{
		String ratio = new String(score+"\\"+index);
		return ratio;
	}

}
