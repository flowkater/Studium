package com.model;

import android.widget.ImageView;

public class ThumbImageInfo
{
  private String id;
  private String thum_img;
  private boolean checkedState;
  
  public String getId()
  {
    return id;
  }
  public void setId(String id)
  {
    this.id = id;
  }
 
  public boolean getCheckedState()
  {
    return checkedState;
  }
  public void setCheckedState(boolean checkedState)
  {
    this.checkedState = checkedState;
  }
public String getThum_img() {
	return thum_img;
}
public void setThum_img(String thum_img) {
	this.thum_img = thum_img;
}

}
