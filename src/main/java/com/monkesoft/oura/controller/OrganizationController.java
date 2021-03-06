package com.monkesoft.oura.controller;


import com.github.pagehelper.Page;
import com.monkesoft.oura.OURADataResponse;
import com.monkesoft.oura.OURAPageResponse;
import com.monkesoft.oura.OURAResponse;
import com.monkesoft.oura.entity.OrgUserVO;
import com.monkesoft.oura.entity.OrganizationInfo;
import com.monkesoft.oura.service.IOrganizationService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api-rest/org")
@Api(tags = "组织操作接口")
public class OrganizationController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IOrganizationService orgService;

    /**
     * 列表获取所有组织，不分层级
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return
     */
    @GetMapping(path = {"/",""})
    @ApiOperation(value="获取组织列表", notes="分页获取所有组织列表")
    public OURAPageResponse<List<OrganizationInfo>> getOrgs(@RequestParam(required = false, defaultValue = "1") int pageNum,
                                                            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        OURAPageResponse<List<OrganizationInfo>> response = new OURAPageResponse<>();
        try {
            Page<OrganizationInfo> page = orgService.getOrgs(pageNum,pageSize);
            response.buildPage(page).setData(page.getResult()).success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.fail().setDesc(e.getMessage());
        }
        return response;
    }

    /**
     * 分页获取子组织列表，若不传分页参数则返回所有
     * @param parentId 父组织ID
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return
     */
    @GetMapping(path = {"/subs/{parentId}","/subs/{parentId}/"})
    @ApiOperation(value="获取子组织列表", notes="根据父组织ID获取其子组织详细列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父ID", paramType = "path", required = true),
            @ApiImplicitParam(name = "pageNum", value = "页码", paramType = "query", defaultValue = "1", required = true),
            @ApiImplicitParam(name = "pageSize", value = "每页数量,0表示全部", paramType = "query", defaultValue = "10",required = true)
    })
    public OURAPageResponse<List<OrganizationInfo>> getSubOrgs(@PathVariable String parentId,
                                                                     @RequestParam(required = false, defaultValue = "1") int pageNum,
                                                                     @RequestParam(required = false, defaultValue = "10") int pageSize) {
        OURAPageResponse<List<OrganizationInfo>> response = new OURAPageResponse<>();
        try {
            Page<OrganizationInfo> page = orgService.getSubOrgs(parentId, pageNum, pageSize);
            response.buildPage(page).setData(page.getResult()).success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.fail().setDesc(e.getMessage());
        }
        return response;
    }

    @GetMapping(path = {"/user/{userId}","/user/{userId}/"})
    @ApiOperation(value="获取用户所属组织列表", notes="根据用户ID获取其所属组织详细列表")
    @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "path", required = true)
    public OURADataResponse<List<OrgUserVO>> getOrgsOfUser(@PathVariable String userId) {
        OURADataResponse<List<OrgUserVO>> response = new OURADataResponse<>();
        try {
            response.setData(orgService.getOrgsOfUser(userId)).success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.fail().setDesc(e.getMessage());
        }
        return response;
    }

    @PostMapping(path = {"/user","/user/"})
    @ApiOperation(value="组织中添加用户", notes="向某一个组织中批量添加人员，同时指定职务")
    public OURAResponse addUsersToOrg(@RequestBody List<OrgUserVO> orgUserVOList) {
        OURAResponse response = new OURAResponse();
        try {
            orgService.addUsersToOrg(orgUserVOList);
            response.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.fail().setDesc(e.getMessage());
        }
        return response;
    }

    @DeleteMapping(path = {"/user","/user/"})
    @ApiOperation(value="组织中删除用户", notes="从某一个组织中删除一个指定人员")
    public OURAResponse removeUserFromOrg(@RequestParam String orgId, @RequestParam String userId) {
        OURAResponse response = new OURAResponse();
        try {
            orgService.removeUserFromOrg(orgId, userId);
            response.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.fail().setDesc(e.getMessage());
        }
        return response;
    }

    /**
     * 查询单个组织的详情
     * @param orgId
     * @return
     */
    @GetMapping(path = {"/{orgId}","/{orgId}/"})
    @ApiOperation(value="获取组织信息", notes="根据ID获取某个组织的详细数据")
    @ApiImplicitParam(name = "orgId", value = "组织ID", paramType = "path", required = true)
    public OURADataResponse<OrganizationInfo> getOrgById(@PathVariable String orgId) {
        OURADataResponse<OrganizationInfo> response = new OURADataResponse<>();
        try {
            response.setData(orgService.getOrgById(orgId)).success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.fail().setDesc(e.getMessage());
        }
        return response;
    }

    /**
     * 增加组织
     * @param org
     * @return
     */
    @PostMapping(path = {"/", ""})
    public OURAResponse insertOrg(@RequestBody OrganizationInfo org) {
        OURAResponse response = new OURAResponse();
        try {
            orgService.insertOrg(org);
            response.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.fail().setDesc(e.getMessage());
        }
        return response;
    }

    /**
     * 更新组织
     * @param org
     * @return
     */
    @PutMapping(path = {"/", ""})
    public OURAResponse updateOrg(@RequestBody OrganizationInfo org) {
        OURAResponse response = new OURAResponse();
        try {
            orgService.updateOrg(org);
            response.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.fail().setDesc(e.getMessage());
        }
        return response;
    }

    @DeleteMapping(path = {"/{orgId}","/{orgId}/"})
    public OURAResponse deleteOrg(@PathVariable String orgId) {
        OURAResponse response = new OURAResponse();
        try {
            orgService.deleteOrg(orgId);
            response.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.fail().setDesc(e.getMessage());
        }
        return response;
    }

    @PutMapping(path = {"/{orgId}/status/{status}","/{orgId}/status/{status}/"})
    public OURAResponse updateOrgStatus(@PathVariable String orgId,@PathVariable int status) {
        OURAResponse response = new OURAResponse();
        try {
            orgService.updateOrgStatus(orgId,status);
            response.success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.fail().setDesc(e.getMessage());
        }
        return response;
    }


}
