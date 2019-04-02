package com.tony.test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.dongnaoedu.springcloud.RibbonSampleApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RibbonSampleApplication.class)
public class RibbonTests {
	@Autowired
	private WebApplicationContext context;
	private MockMvc mvc;

	@Before
	public void setUp() {
		this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	@Test
	public void ribbonPropertiesTest() throws Exception {
		// 无任何请求，为了演示负载均衡器的效果
		this.mvc.perform(get("/loadbalance/properties")).andExpect(status().isOk());
		this.mvc.perform(get("/loadbalance/properties")).andExpect(status().isOk());
		this.mvc.perform(get("/loadbalance/properties")).andExpect(status().isOk());
		this.mvc.perform(get("/loadbalance/properties")).andExpect(status().isOk());
		this.mvc.perform(get("/loadbalance/properties")).andExpect(status().isOk());
		this.mvc.perform(get("/loadbalance/properties")).andExpect(status().isOk());
	}
	
	@Test
	public void ribbonAnnotationTest() throws Exception {
		// 无任何请求，为了演示负载均衡器的效果
		this.mvc.perform(get("/loadbalance/annotation")).andExpect(status().isOk());
		this.mvc.perform(get("/loadbalance/annotation")).andExpect(status().isOk());
		this.mvc.perform(get("/loadbalance/annotation")).andExpect(status().isOk());
		this.mvc.perform(get("/loadbalance/annotation")).andExpect(status().isOk());
		this.mvc.perform(get("/loadbalance/annotation")).andExpect(status().isOk());
		this.mvc.perform(get("/loadbalance/annotation")).andExpect(status().isOk());
	}
	
	@Test
	public void ribbonEurekaTest() throws Exception {
		// 无任何请求，为了演示负载均衡器的效果
		this.mvc.perform(get("/loadbalance/eureka")).andExpect(status().isOk());
		this.mvc.perform(get("/loadbalance/eureka")).andExpect(status().isOk());
		this.mvc.perform(get("/loadbalance/eureka")).andExpect(status().isOk());
		this.mvc.perform(get("/loadbalance/eureka")).andExpect(status().isOk());
		this.mvc.perform(get("/loadbalance/eureka")).andExpect(status().isOk());
		this.mvc.perform(get("/loadbalance/eureka")).andExpect(status().isOk());
	}

}
