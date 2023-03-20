package com.thinktalentindia.CrudeProject.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thinktalentindia.CrudeProject.model.Employee;
import com.thinktalentindia.CrudeProject.service.EmployeeBo;

@RestController
@RequestMapping("/emp")
public class EmployeeController {
	
	@Autowired
	private EmployeeBo employeeBo;
	
	@GetMapping("/")
	public String test() {
		return "test";
	}
	
	@PostMapping(value = "/add", consumes = "application/json",produces="application/json")
	public Map<String, Object> add(HttpServletResponse resp, @RequestBody Employee e) {
		return addUpdate(resp, e);
	}

	private Map<String, Object> addUpdate(HttpServletResponse resp, Employee p) {	
		Map<String, Object> mResp = new HashMap<String, Object>();
		resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		try{
			mResp = employeeBo.addUpdate(p);
			if(!mResp.get("message").equals(EmployeeBo.FAILED)); {
				resp.setStatus(HttpServletResponse.SC_OK);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return mResp;
	}

	@PostMapping(value = "/update")
	public Map<String, Object> update(HttpServletResponse resp, @RequestBody Employee e) {
		return addUpdate(resp, e);
	}
	
	@GetMapping(value = "/all")
	public List<Employee> findAll(HttpServletResponse resp) {
		resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		List<Employee> emps = new ArrayList<>();
		emps = employeeBo.findAll();
		if (null!=emps && emps.size() > 0) {
			resp.setStatus(HttpServletResponse.SC_OK);
		}
		return emps;
	}

	@GetMapping(value = "/id/{id}")
	public Map<String, Object> findById(HttpServletResponse resp, @PathVariable("id") Long id) {
		Map<String, Object> mResp = new HashMap<String, Object>();
		resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		try {
			if (id == null || id == 0) {
				mResp.put("message", EmployeeBo.NO_IDS);
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return mResp;
			}

			Employee employee = employeeBo.findById(id);

			if (employee != null) {
				mResp.put("result", employee);
				resp.setStatus(HttpServletResponse.SC_OK);
			} else {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mResp;
	}

	@DeleteMapping(value = "/remove")
	public Map<String, Object> remove(HttpServletResponse resp, @RequestParam("ids") String ids) {
		Map<String, Object> mResp = new HashMap<String, Object>();
		resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		try {
			mResp = employeeBo.remove(ids);

			if (mResp.get("message").equals(EmployeeBo.DELETE)) {
				resp.setStatus(HttpServletResponse.SC_OK);
			} else if (mResp.get("message").equals(EmployeeBo.NO_IDS)) {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mResp;
	}
	@GetMapping("/image")
	public ResponseEntity<Resource> getImage(@RequestParam String url) throws IOException {
		ByteArrayResource resource = new ByteArrayResource(url.getBytes("UTF-8"));
		return ResponseEntity.ok().contentLength(resource.contentLength())
				.contentType(MediaType.parseMediaType("image/jpeg")).body(resource);
	}

	@PostMapping("/image/upload")
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Path filePath = Paths.get("src/main/resources/images/" + fileName);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		return ResponseEntity.ok("File uploaded successfully");
	}

	@GetMapping("/image/{filename}")
	public ResponseEntity<Resource> getImageByFileName(@PathVariable String filename) throws IOException {
		Path filePath = Paths.get("src/main/resources/images/" + filename);
		Resource resource = new UrlResource(filePath.toUri());
		return ResponseEntity.ok().contentLength(resource.contentLength())
				.contentType(MediaType.parseMediaType("image/jpeg")).body(resource);
	}

	@GetMapping("/image/render/{filename}")
	public ResponseEntity<Resource> renderImage(@PathVariable String filename) throws IOException {
		Path filePath = Paths.get("src/main/resources/images/" + filename);
		Resource resource = new UrlResource(filePath.toUri());
		return ResponseEntity.ok().contentLength(resource.contentLength())
				.contentType(MediaType.parseMediaType("image/jpeg")).body(resource);
	}

}