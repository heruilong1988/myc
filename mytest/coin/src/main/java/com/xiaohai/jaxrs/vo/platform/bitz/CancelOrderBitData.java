/** 
* @version 
* @author xiaohai
* @date Aug 13, 2018 9:39:01 PM 
* 
*/ 
package com.xiaohai.jaxrs.vo.platform.bitz;

/** 
 * @version 
 * @author xiaohai
 * @date Aug 13, 2018 9:39:01 PM
 * 
 */
public class CancelOrderBitData {

	private CancelOrderBitDataUpdateAssertsData updateAssertsData;
	
	private CancelOrderBitDataAssersInfo assersInfo;

	public CancelOrderBitDataUpdateAssertsData getUpdateAssertsData() {
		return updateAssertsData;
	}

	public void setUpdateAssertsData(
			CancelOrderBitDataUpdateAssertsData updateAssertsData) {
		this.updateAssertsData = updateAssertsData;
	}

	public CancelOrderBitDataAssersInfo getAssersInfo() {
		return assersInfo;
	}

	public void setAssersInfo(CancelOrderBitDataAssersInfo assersInfo) {
		this.assersInfo = assersInfo;
	}
	
}

//"data":{
//  "735707554":{
//      "updateAssetsData":{
//          "bz_lock":-10,
//          "bz_over":"10.0000"
//      },
//      "assetsInfo":{
//          "uid":"2074056",
//          "bz_over":"14.72821700",
//          "bz_lock":"40.00000000",
//          "created":"1533092866",
//          "updated":"1533709696639"
//      }
//  },
//  "735711408":{
//      "updateAssetsData":{
//          "bz_lock":-10,
//          "bz_over":"10.0000"
//      },
//      "assetsInfo":{
//          "uid":"2074056",
//          "bz_over":"24.72821700",
//          "bz_lock":"30.00000000",
//          "created":"1533092866",
//          "updated":"1533709696676"
//      }
//  }
//},

