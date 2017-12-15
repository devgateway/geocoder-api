/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.devgateway.geocoder.web;

import org.devgateway.geocoder.domain.Activity;
import org.devgateway.geocoder.repositories.ActivityRepository;
import org.devgateway.geocoder.request.SearchRequest;
import org.devgateway.geocoder.service.XmlImport;
import org.devgateway.geocoder.web.filterstate.ActivityFilterState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@CrossOrigin
public class ActivityController {
    Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    XmlImport xmlImport;

    @Autowired
    ActivityRepository activityRepository;


    @RequestMapping(value = "/import", method = RequestMethod.POST, consumes = {"multipart/form-data"})
    public ResponseEntity importXmlFile(@RequestPart("file") final MultipartFile uploadfile,
                                        @RequestPart(name = "autoGeocode", required = false) final String autoGeocode) {
        final Boolean auto = Boolean.valueOf(autoGeocode);

        try {
            xmlImport.process(uploadfile.getInputStream(), "en", auto);

            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error while importing file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while importing file");
        }
    }

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public Page<Activity> getActivityLists(SearchRequest searchRequest) {
        final Pageable pageRequest = new PageRequest(searchRequest.getPage(), 10, Sort.Direction.ASC, "id");
        final ActivityFilterState activityFilterState = new ActivityFilterState(searchRequest);

        return activityRepository.findAll(activityFilterState.getSpecification(), pageRequest);
    }

    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public Activity getActivityById(@PathVariable Long id, @RequestParam String lan) {
        return activityRepository.findOne(id);
    }

}
