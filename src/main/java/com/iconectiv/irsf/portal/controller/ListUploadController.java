package com.iconectiv.irsf.portal.controller;

import com.iconectiv.irsf.portal.core.AppConstants;
import com.iconectiv.irsf.portal.core.MessageDefinition;
import com.iconectiv.irsf.portal.core.PermissionRole;
import com.iconectiv.irsf.portal.exception.AppException;
import com.iconectiv.irsf.portal.model.common.UserDefinition;
import com.iconectiv.irsf.portal.model.customer.ListDefinition;
import com.iconectiv.irsf.portal.model.customer.ListUploadRequest;
import com.iconectiv.irsf.portal.repositories.customer.ListDetailsRepository;
import com.iconectiv.irsf.portal.service.FileHandlerService;
import com.iconectiv.irsf.portal.service.ListService;
import com.iconectiv.irsf.util.JsonHelper;
import io.jsonwebtoken.lang.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
public class ListUploadController extends BaseRestController {
	private Logger log = LoggerFactory.getLogger(ListUploadController.class);

	@Autowired
	private ListService listService;
    @Autowired
    private FileHandlerService fileService;
	@Autowired
	private ListDetailsRepository listDetailRepo;


    @Value("${max_list_size:100000}")
    private int maxListSize;

	@RequestMapping(value = "/uploadListFile", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> multipleSave(@RequestHeader Map<String, String> header, @RequestParam("file") MultipartFile file,
	        @RequestParam("listType") String listType, @RequestParam("listName") String listName, @RequestParam("description") String description,
	        @RequestParam("listId") Integer id, @RequestParam("delimiter") String delimiter) {
		ResponseEntity<String> rv;

        Boolean isInitialLoading;

        try {
            if (log.isDebugEnabled()) log.debug("Received list upload request {}, {}, {}, {}, {}", id, listType, listName, description, delimiter);

            Assert.notNull(listName);
            Assert.notNull(delimiter);
            Assert.notNull(listType);

			UserDefinition loginUser = getLoginUser(header);
			assertAuthorized(loginUser, PermissionRole.CustAdmin.value() + "," + PermissionRole.User.value());

            isInitialLoading = id == null;

            log.debug(file.getContentType());

            if (fileService.getFileSize(file) == 0) {
                throw  new AppException(file.getOriginalFilename() + " is empty");
            }

            if (!AppConstants.UploadFileType.contains( file.getContentType() )) {
                String errorMessage = file.getOriginalFilename() + " is NOT ascii file " + file.getContentType();
                log.error(errorMessage);
                throw  new AppException(errorMessage);
            }

            ListDefinition listDef = new ListDefinition();
            listDef.setId(id);
			listDef.setListName(listName);
            listDef.setDescription(description);
            listDef.setType(listType);

            ListUploadRequest uploadReq = new ListUploadRequest();
            uploadReq.setFileName(file.getOriginalFilename());
            uploadReq.setDelimiter(delimiter);
            uploadReq.setCustomerName(loginUser.getCustomerName());
            uploadReq.setData(fileService.getContentAsList(file));

            //check list size
            int currentListSize = 0;
            if (listDef.getId() != null) {
                currentListSize = listDetailRepo.getListSizeByListId(listDef.getId());
            }
            if (currentListSize + uploadReq.getData().size() > maxListSize) {
                throw new AppException(MessageDefinition.ListSizeOverLimitError + maxListSize);
            }

            listService.processListUploadRequest(loginUser, listDef, uploadReq, isInitialLoading);

			rv = makeSuccessResult(MessageDefinition.Process_List_Upload);
		} catch (SecurityException e) {
			rv = makeErrorResult(e, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
		    log.error("error to process lit upload request:", e);
			rv = makeErrorResult(e);
		}

		if (log.isDebugEnabled()) {
			log.debug("rest return: {} ", JsonHelper.toJson(rv));
		}
		return rv;
	}
}
