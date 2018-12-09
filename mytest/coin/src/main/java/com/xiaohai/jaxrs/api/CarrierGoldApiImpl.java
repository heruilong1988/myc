/** 
* @version 
* @author xiaohai
* @date Aug 10, 2018 10:26:10 PM 
* 
*/ 
package com.xiaohai.jaxrs.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/** 
 * @version 
 * @author xiaohai
 * @date Aug 10, 2018 10:26:10 PM
 * 
 */
@Component
@RequestMapping("/carrier")
public class CarrierGoldApiImpl {
	
	@POST
	@Consumes({"application/json; charset=UTF-8"})
	@Produces({"application/json; charset=UTF-8"})
//	@Path("/triangle")
	@RequestMapping(value = "/triangle" , method = RequestMethod.POST)
	public void triangle() {
		System.out.println("这里是请求进来的，三角套利");
	}
}


