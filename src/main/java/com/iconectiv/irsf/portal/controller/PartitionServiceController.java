package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.portal.config.CustomerContextHolder;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.PartitionDefinition;
import com.iconectiv.irsf.portal.service.AuditTrailService;
import com.iconectiv.irsf.portal.service.PartitionService;
import com.iconectiv.irsf.util.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Controller
class PartitionServiceController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(PartitionServiceController.class);

	@Autowired
	private PartitionService partitionServ;

    @Autowired
    private AuditTrailService auditService;

	@RequestMapping(value = "/partition/{partitionId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getPartitionDetails(@RequestHeader Map<String, String> header, @PathVariable Integer partitionId) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());
			PartitionDefinition partition = partitionServ.getPartitionDetails(partitionId);
            rv = makeSuccessResult(MessageDefinition.Query_Success, partition);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}


	@RequestMapping(value = "/partitions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getPartitionList(@RequestHeader Map<String, String> header) {
		ResponseEntity<String> rv;
		try {
			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value());

			CustomerContextHolder.setSchema(loginUser.getSchemaName());

			List<PartitionDefinition> partitions = partitionServ.getAllActivePartitions();
			rv = makeSuccessResult(MessageDefinition.Query_Success, partitions);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug(JsonHelper.toPrettyJson(rv));
		}
		return rv;
	}

    @RequestMapping(value = "/partition/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> savePartitionRequest(@RequestHeader Map<String, String> header, @RequestBody String value) {
        ResponseEntity<String> rv;
        try {
        	PartitionDefinition partition = JsonHelper.fromJson(value, PartitionDefinition.class);
            UserDefinition loginUser = getLoginUser(header);
            assertAuthorized(loginUser, PermissionRole.CustAdmin.value());

            CustomerContextHolder.setSchema(loginUser.getSchemaName());
            partitionServ.savePartition(loginUser, partition);
            rv = makeSuccessResult(MessageDefinition.Save_Partition_Success, partition);
        } catch (SecurityException e) {
            rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            rv = makeErrorResult(e);
        }

        if (log.isDebugEnabled()) {
            log.debug(JsonHelper.toJson(rv));
        }
        return rv;
    }

    @RequestMapping(value = "/partition/export", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> exportPartitionRequest(@RequestHeader Map<String, String> header, @RequestBody PartitionDefinition partition) {
        ResponseEntity<String> rv;
        try {
            UserDefinition loginUser = getLoginUser(header);
            assertAuthorized(loginUser, PermissionRole.CustAdmin.value());

            if (partition.getId() == null) {
                throw new AppException("Missing partition Id");
            }
            CustomerContextHolder.setSchema(loginUser.getSchemaName());
            partitionServ.exportPartition(loginUser, partition);
            rv = makeSuccessResult(MessageDefinition.Generating_Partition_Dataset_Success);
        } catch (SecurityException e) {
            rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            rv = makeErrorResult(e);
        }

        if (log.isDebugEnabled()) {
            log.debug(JsonHelper.toJson(rv));
        }
        return rv;
    }

    @RequestMapping(value = "/partition/refresh", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> refreshPartitionRequest(@RequestHeader Map<String, String> header, @RequestBody PartitionDefinition partition) {
        ResponseEntity<String> rv;
        try {
            UserDefinition loginUser = getLoginUser(header);
            assertAuthorized(loginUser, PermissionRole.CustAdmin.value());

            if (partition.getId() == null) {
                throw new AppException("Missing partition Id");
            }
            CustomerContextHolder.setSchema(loginUser.getSchemaName());
            partitionServ.refreshPartition(loginUser, partition);
            rv = makeSuccessResult(MessageDefinition.Generating_Partition_Dataset_Success);
        } catch (SecurityException e) {
            rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            rv = makeErrorResult(e);
        }

        if (log.isDebugEnabled()) {
            log.debug(JsonHelper.toJson(rv));
        }
        return rv;
    }
}
