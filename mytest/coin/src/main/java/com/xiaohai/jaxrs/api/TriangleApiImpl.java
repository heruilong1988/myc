/** 
* @version 
* @author xiaohai
* @date Jul 28, 2018 12:21:21 PM 
* 
*/ 
package com.xiaohai.jaxrs.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xiaohai.jaxrs.pojo.stratefgy.TriangleReq;
import com.xiaohai.jaxrs.service.strategy.TriangleService;

/** 
 * @version 
 * @author xiaohai
 * @date Jul 28, 2018 12:21:21 PM
 * 
 */
@Controller
@RequestMapping("/carrier")
public class TriangleApiImpl {
	
	private static final Logger LOG = LoggerFactory.getLogger(TriangleApiImpl.class);
	
	//@Autowired
	private TriangleService triangleService;


	@POST
	@Consumes({"application/json; charset=UTF-8"})
	@Produces({"application/json; charset=UTF-8"})
	@Path("/triangle")
	public void triangle(TriangleReq req) {
		LOG.info("这里是请求进来的，三角套利");
		triangleService.process(req);
	}
}


